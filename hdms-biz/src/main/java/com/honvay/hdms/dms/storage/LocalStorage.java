/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.storage;

import com.honvay.hdms.dms.document.entity.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LIQIU
 */
@Slf4j
public class LocalStorage implements Storage {

	private String location;

	public LocalStorage(String location) {
		this.location = location;
		if (this.location != null && !this.location.endsWith("/")) {
			this.location += "/";
		}
		log.info("LocalStorage init location: {}", this.location);
	}

	@Override
	public void store(String directory, String key, File file, Consumer<String> consumer) throws IOException {
		this.store(directory, key, new FileInputStream(file), consumer);
	}

	@Override
	public void remove(String key) {
		File file = new File(location + StorageDirectory.FILE, key);
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			try {
				if (log.isDebugEnabled()) {
					log.debug("Remove directory: {},path: {}", key, file.getAbsolutePath());
				}
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			if (log.isDebugEnabled()) {
				log.debug("Remove file: {},path: {}", key, file.getAbsolutePath());
			}
		}
	}

	@Override
	public void copy(String source, String target) {
		File sourceFile = new File(location + StorageDirectory.FILE, source);
		File targetFile = new File(location + StorageDirectory.FILE, target);
		try {
			if (log.isDebugEnabled()) {
				log.debug("Copy file:{} to:{}", sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
			}
			FileUtils.copyFile(sourceFile, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDownloadURL(Document document) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getInputStream(String key) {
		try {
			return new FileInputStream(new File(location + StorageDirectory.FILE, key));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public InputStream getInputStream(String directory, String key) {
		try {
			return new FileInputStream(new File(location + directory, key));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> listFile(String directory) {
		File dir = new File(location, directory);
		File[] files = dir.listFiles();
		List<String> keys = new ArrayList<String>();
		for (File file : files) {
			keys.add(file.getName());
		}
		return keys;
	}

	@Override
	public void createDirectory(String directory) {
		File dir = new File(location, directory);
		dir.mkdir();
	}

	@Override
	public boolean exists(String path) {
		return new File(location, path).exists();
	}

	@Override
	public String toString() {
		return "LocalStorage:" + this.location;
	}

	@Override
	public String getType() {
		return LOCAL;
	}

	@Override
	public Long getSize(String key) {
		return new File(location + key).length();
	}

	@Override
	public void store(String directory, String key, InputStream input, Consumer<String> consumer) throws IOException {
		File directoryFile = new File(location + directory);
		if (!directoryFile.exists()) {
			boolean mkdirs = directoryFile.mkdirs();
			if (!mkdirs) {
				throw new UnsupportedOperationException("创建文件失败");
			}
		}
		File dest = new File(location + directory, key);
		if (!dest.exists()) {
			boolean newFile = dest.createNewFile();
			if (!newFile) {
				throw new UnsupportedOperationException("创建文件失败");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Store file:{},path:{}", key, dest.getAbsolutePath());
		}
		FileOutputStream output = new FileOutputStream(dest);
		IOUtils.copy(input, output);
		IOUtils.closeQuietly(input);
		IOUtils.closeQuietly(output);
		if (consumer != null) {
			consumer.accept(key);
		}
	}

	@Override
	public void store(String key, File file, Consumer<String> consumer) throws IOException {
		this.store("", key, file, consumer);
	}
}
