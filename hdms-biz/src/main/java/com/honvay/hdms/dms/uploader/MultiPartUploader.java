package com.honvay.hdms.dms.uploader;

import com.honvay.hdms.config.properties.StorageConfig;
import com.honvay.hdms.dms.encryptor.Encryptors;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.model.request.UploadRequest;
import com.honvay.hdms.framework.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author LIQIU
 * created on 2019/3/4
 **/
@Slf4j
public class MultiPartUploader implements Uploader {

	@Getter
	@Setter
	private Storage storage;

	@Getter
	@Setter
	private StorageConfig storageConfig;

	@Getter
	@Setter
	private Encryptors encryptors;

	/**
	 * @param request
	 * @param multipartFile
	 * @param consumer
	 * @return
	 * @throws IOException
	 */
	@Override
	public String upload(UploadRequest request, MultipartFile multipartFile, Consumer<String> consumer) throws IOException {

		//不分片上传
		File temporaryDirectory = storageConfig.getTemporaryDirectory();

		if (request.getChunk() == null) {
			String key = UUID.randomUUID().toString().replaceAll("-", "");
			this.storage.store("file", key, multipartFile.getInputStream(), consumer);
			return key;
		} else {

			//分片上传
			// 当前分片
			int chunk = request.getChunk();
			// 分片总计
			int chunks = request.getChunks();
			String md5 = request.getMd5();

			//新建分片文件夹
			File md5ChunkDirectory = this.getMd5ChunkDirectory(md5, temporaryDirectory);
			if (!md5ChunkDirectory.exists()) {
				if (log.isDebugEnabled()) {
					log.debug("创建分片临时目录，目录：" + md5ChunkDirectory.getAbsolutePath());
				}
				boolean mkdirs = md5ChunkDirectory.mkdirs();
				if (!mkdirs) {
					log.error("创建临时目录失败，目录路径：" + md5ChunkDirectory.getAbsolutePath());
					throw new IllegalStateException("创建临时目录失败");
				}
			}

			if (log.isDebugEnabled()) {
				log.debug("开始保存分片，MD5：" + md5 + "，分片：" + chunk);
			}
			long saveChunkStart = System.currentTimeMillis();
			File chunkFile = new File(md5ChunkDirectory, String.valueOf(chunk));
			if (chunkFile.exists()) {
				boolean delete = chunkFile.delete();
				if (!delete) {
					log.error("删除遗留文件失败，文件路径：" + chunkFile.getAbsolutePath());
					throw new IllegalStateException("无法删除垃圾文件");
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("创建分片文件：" + chunkFile.getPath());
			}
			boolean newFile = chunkFile.createNewFile();
			if (!newFile) {
				log.error("创建分片文件失败，文件路径：" + chunkFile.getAbsolutePath());
				throw new IllegalStateException("创建分片文件失败");
			}
			multipartFile.transferTo(chunkFile);

			if (log.isDebugEnabled()) {
				log.debug("保存分片结束，耗时：" + (System.currentTimeMillis() - saveChunkStart));
			}
			if (checkChunks(md5ChunkDirectory, request.getSize(), chunks)) {
				if (log.isDebugEnabled()) {
					log.debug("开始存储文件，文件路径：" + md5ChunkDirectory.getAbsolutePath());
				}

				File md5File = mergeChunks(md5ChunkDirectory);
				if (log.isDebugEnabled()) {
					log.debug("文件合并完成，文件路径：{}", md5File.getAbsolutePath());
				}
				String key = UUID.randomUUID().toString().replaceAll("-", "");
				this.storage.store("file", key, encryptors.encrypt(new FileInputStream(md5File)), s -> {
					boolean delete = md5File.delete();
					if(!delete){
						log.error("删除临时文件失败：{}",md5File.getAbsolutePath());
					}
					consumer.accept(s);
				});
				if (log.isDebugEnabled()) {
					log.debug("完成存储文件，文件路径：{}", md5File.getAbsolutePath());
				}
				return key;
			}
			return null;
		}
	}

	private File mergeChunks(File md5Directory) throws IOException {
		List<File> files = Arrays.asList(Objects.requireNonNull(md5Directory.listFiles()));
		files.sort((o1, o2) -> {
			if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
				return -1;
			}
			return 1;
		});
		File md5File = new File(storageConfig.getTemporaryDirectory(), md5Directory.getName());
		if (!md5File.exists()) {
			boolean newFile = md5File.createNewFile();
			if (!newFile) {
				throw new IllegalStateException("创建MD5文件失败，MD5文件路径：" + md5File.getAbsolutePath());
			}
		}
		FileOutputStream md5FileOutput = new FileOutputStream(md5File);
		FileChannel md5FileChannel = md5FileOutput.getChannel();
		for (File child : files) {
			FileInputStream childInput = new FileInputStream(child);
			FileChannel childChannel = childInput.getChannel();
			md5FileChannel.transferFrom(childChannel, md5FileChannel.size(), childChannel.size());
			childChannel.close();
			childInput.close();
		}
		md5FileChannel.close();
		md5FileOutput.close();
		FileUtils.deleteDirectory(md5Directory);
		return md5File;
	}

	private boolean checkChunks(File md5Directory, Long size, int chunks) {
		File[] files = md5Directory.listFiles();
		if (chunks != files.length) {
			return false;
		}
		long sizeSum = 0L;
		for (File file : files) {
			sizeSum += file.length();
		}
		return size.compareTo(sizeSum) == 0;
	}

	private File getMd5ChunkDirectory(String md5, File temporaryDirectory) {
		return new File(temporaryDirectory, "/chunks/" + md5);
	}
}
