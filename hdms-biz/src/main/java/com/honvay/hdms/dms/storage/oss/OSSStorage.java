/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.storage.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.honvay.hdms.config.properties.StorageConfig;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.storage.Storage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class OSSStorage implements Storage {

	private String endpoint = "*** Provide OSS endpoint ***";
	private String accessKeyId = "*** Provide your AccessKeyId ***";
	private String accessKeySecret = "*** Provide your AccessKeySecret ***";
	private String bucketName = "*** Provide bucket name ***";
	private Long partSize = 5 * 1024 * 1024L;
	private String directory = "";
	private OSSClient client;
	private boolean asyncUpload = true;

	public OSSStorage() {

	}

	public OSSStorage(StorageConfig.OssConfig config, String directory) {
		BeanUtils.copyProperties(config, this);
		if (StringUtils.isNotEmpty(directory) && !directory.endsWith("/")) {
			directory += "/";
		}
		this.directory = directory;
	}

	@Override
	public void store(String directory, String key, File file, Consumer<String> consumer) {
		try {
			if (StringUtils.isNotEmpty(directory) && !directory.endsWith("/")) {
				directory += "/";
			}
			key = this.directory + directory + key;
			if (file.isDirectory() && this.asyncUpload) {
				OSSMultiPartUploader uploader = new OSSMultiPartUploader(this.getClient(), this.bucketName, key, file, consumer);
				//OSSUploader uploader = new OSSUploader(this.getClient(), bucketName, code, file, partSize, callback);
				new Thread(uploader).start();
				// uploader.upload();
			} else if (file.isFile() && file.length() > partSize && this.asyncUpload) {
				OSSUploader uploader = new OSSUploader(this.getClient(), bucketName, key, file, partSize, consumer);
				new Thread(uploader).start();
			} else {
				this.getClient().putObject(bucketName, key, file);
				consumer.accept(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void store(String directory, String key, InputStream input, Consumer<String> consumer) {
		key = this.directory + directory + key;
		this.getClient().putObject(bucketName, key, input);
	}

	@Override
	public InputStream getInputStream(String key) {
		OSSClient ossClient = this.getClient();
		OSSObject ossObject = ossClient.getObject(bucketName, directory + key);
		return ossObject.getObjectContent();
	}

	@Override
	public InputStream getInputStream(String directory, String key) {
		return null;
	}

	@Override
	public Long getSize(String key) {
		OSSClient ossClient = this.getClient();
		key = directory + key;
		ObjectListing list = ossClient.listObjects(bucketName, key);
		List<OSSObjectSummary> summaries = list.getObjectSummaries();
		if (summaries.size() > 0) {
			return summaries.get(0).getSize();
		}
		return null;
	}

	@Override
	public void remove(String key) {
		OSSClient ossClient = this.getClient();
		ossClient.deleteObject(bucketName, this.directory + key);
	}

	@Override
	public void copy(String source, String target) {
		OSSClient ossClient = getClient();
		CopyObjectRequest request = new CopyObjectRequest(bucketName, this.directory + source, bucketName,
				this.directory + target);
		ossClient.copyObject(request);
	}

	@Override
	public String getDownloadURL(Document document) {
		Date expires = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, String.valueOf(document.getId()));
		ResponseHeaderOverrides headers = new ResponseHeaderOverrides();
		// try {
		headers.setContentDisposition("attachment; filename=" + document.getName());
		/*
		 * } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		 */
		headers.setContentEncoding("utf-8");
		headers.setExpires(String.valueOf(1000 * 60 * 15));
		headers.setContentType(document.getContentType());
		request.setExpiration(expires);
		request.setResponseHeaders(headers);
		URL url = this.getClient().generatePresignedUrl(request);
		return url.toString();
	}

	public OSSClient getClient() {
		if (client == null) {
			client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		}
		return client;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Long getPartSize() {
		return partSize;
	}

	public void setPartSize(Long partSize) {
		this.partSize = partSize;
	}

	@Override
	public List<String> listFile(String directory) {
		ObjectListing list = this.getClient().listObjects(this.bucketName, this.directory + directory);
		List<String> keys = new ArrayList<String>();
		List<OSSObjectSummary> summaries = list.getObjectSummaries();
		for (OSSObjectSummary ossObjectSummary : summaries) {
			keys.add(ossObjectSummary.getKey());
		}

		return keys;
	}

	@Override
	public void createDirectory(String directory) {
		this.getClient().putObject(this.bucketName, this.directory + directory, new ByteArrayInputStream(new byte[0]));
	}

	@Override
	public boolean exists(String key) {
		try {
			OSSObject object = this.getClient().getObject(bucketName, this.directory + key);
			object.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAsyncUpload() {
		return asyncUpload;
	}

	public void setAsyncUpload(boolean asyncUpload) {
		this.asyncUpload = asyncUpload;
	}

	@Override
	public String getType() {
		return OSS;
	}

	@Override
	public void store(String key, File file, Consumer<String> consumer) {
		this.store("", key, file, consumer);
	}
}
