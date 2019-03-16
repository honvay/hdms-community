/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.storage.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 子线程上传（多线程的上传效果提升明显，如果单线程则在for循环中一个个上传即可）
 */
@Slf4j
public class OSSUploader implements Runnable {

	private OSSClient client = null;
	private String key = null;
	private String bucketName = null;
	private Long partSize = 0L;
	private File file;
	private Consumer<String> consumer;
	private int failCount = 0;
	private int retryLimit = 0;

	public OSSUploader(OSSClient client, String bucketName, String key, File file, Long partSize, Consumer<String> consumer) {
		this.client = client;
		this.key = key;
		this.file = file;
		this.bucketName = bucketName;
		this.partSize = partSize;
		this.consumer = this.consumer;
	}

	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	private List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());


	@Override
	public void run() {
		this.upload();
	}

	public void upload() {
		try {
			/*
			 * Claim a upload id firstly
			 */
			String uploadId = claimUploadId();
			//System.out.println("Claiming a new upload id " + uploadId + "\n");
			if (log.isDebugEnabled()) {
				log.debug(String.format("准备开始上传文件到OSS，任务编号：%s", uploadId));
			}

			/*
			 * Calculate how many parts to be divided
			 */
			long fileLength = file.length();
			int partCount = (int) (fileLength / partSize);
			if (fileLength % partSize != 0) {
				partCount++;
			}
			if (partCount > 10000) {
				throw new RuntimeException("Total parts count should not exceed 10000");
			} else {
				if (log.isDebugEnabled()) {
					log.debug(String.format("总共分成[%d]份片段进行上传", partCount));
				}
				//System.out.println("Total parts count " + partCount + "\n");
			}

			/*
			 * Upload multiparts to your bucket
			 */
			//System.out.println("Begin to upload multiparts to OSS from a file\n");
			long uploadStart = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("开始上传文件");
			}
			for (int i = 0; i < partCount; i++) {
				long startPos = i * partSize;
				long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
				executorService.execute(new PartUploader(file, startPos, curPartSize, i + 1, uploadId));
			}

			/*
			 * Waiting for all parts finished
			 */
			executorService.shutdown();
			while (!executorService.isTerminated()) {
				try {
					executorService.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			/*
			 * Verify whether all parts are finished
			 */
			if (partETags.size() != partCount) {
				failCount++;
				if (failCount > retryLimit) {
					throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
				} else {
					this.upload();
				}
				//
			} else {
				if (log.isDebugEnabled()) {
					log.debug(String.format("上传文件成功，文件key为：%s，耗时：%d", key, System.currentTimeMillis() - uploadStart));
				}
				//System.out.println("Succeed to complete multiparts into an object named " + code + "\n");
			}

			/*
			 * View all parts uploaded recently
			 */
			//listAllParts(uploadId);

			/*
			 * Complete to upload multiparts
			 */
			completeMultipartUpload(uploadId);

			/*
			 * Fetch the object that newly created at the step below.
			 */
			// System.out.println("Fetching an object");
			// client.getObject(new GetObjectRequest(bucketName, code), new
			// File(localFilePath));

			/*
			 * Do not forget to shut down the client finally to release all
			 * allocated resources.
			 */
			/*
			 * if (client != null) { client.shutdown(); }
			 */
		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorCode());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class PartUploader implements Runnable {

		private File file;
		private long startPos;

		private long partSize;
		private int partNumber;
		private String uploadId;

		public PartUploader(File file, long startPos, long partSize, int partNumber, String uploadId) {
			this.file = file;
			this.startPos = startPos;
			this.partSize = partSize;
			this.partNumber = partNumber;
			this.uploadId = uploadId;
		}

		@Override
		public void run() {
			InputStream input = null;
			try {
				if (log.isDebugEnabled()) {
					log.debug(String.format("上传分片：%s", String.valueOf(partNumber)));
				}
				input = new FileInputStream(file);
				input.skip(this.startPos);
				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucketName);
				uploadPartRequest.setKey(key);
				uploadPartRequest.setUploadId(this.uploadId);
				uploadPartRequest.setInputStream(input);
				uploadPartRequest.setPartSize(this.partSize);
				uploadPartRequest.setPartNumber(this.partNumber);

				UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
				//System.out.println("Part#" + this.partNumber + " done\n");
				synchronized (partETags) {
					partETags.add(uploadPartResult.getPartETag());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private String claimUploadId() {
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
		return result.getUploadId();
	}

	private void completeMultipartUpload(String uploadId) {
		// Make part numbers in ascending order
		Collections.sort(partETags, new Comparator<PartETag>() {

			@Override
			public int compare(PartETag p1, PartETag p2) {
				return p1.getPartNumber() - p2.getPartNumber();
			}
		});
		/*System.out.println("Completing to upload multiparts\n");*/
		if (log.isDebugEnabled()) {
			log.debug("文件上传结束");
		}
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
				key, uploadId, partETags);
		client.completeMultipartUpload(completeMultipartUploadRequest);
		if (this.consumer != null) {
			this.consumer.accept(key);
		}
	}

	/*private void listAllParts(String uploadId) {
		System.out.println("Listing all parts......");
		ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, code, uploadId);
		PartListing partListing = client.listParts(listPartsRequest);

		int partCount = partListing.getParts().size();
		for (int i = 0; i < partCount; i++) {
			PartSummary partSummary = partListing.getParts().get(i);
			System.out.println("\tPart#" + partSummary.getPartNumber() + ", ETag=" + partSummary.getETag());
		}
		System.out.println();
	}*/

}