/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.storage;

import com.honvay.hdms.dms.document.entity.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface Storage {

	public static final String OSS = "oss";
	public static final String LOCAL = "local";

	/**
	 * 存储文件
	 *
	 * @param key
	 * @param file
	 */

	void store(String key, File file, Consumer<String> consumer) throws IOException;

	/**
	 * @param directory
	 * @param key
	 * @param file
	 * @param consumer
	 */
	void store(String directory, String key, File file, Consumer<String> consumer) throws IOException;

	/**
	 * @param directory
	 * @param key
	 * @param input
	 * @param consumer
	 */
	void store(String directory, String key, InputStream input, Consumer<String> consumer) throws IOException;

	/**
	 * 删除文件
	 *
	 * @param key
	 */
	void remove(String key);

	/**
	 * 复制文件
	 *
	 * @param source
	 * @param target
	 */
	void copy(String source, String target);

	/**
	 * 获取下载链接
	 *
	 * @param document
	 * @return
	 */
	String getDownloadURL(Document document);

	InputStream getInputStream(String key);

	InputStream getInputStream(String directory, String key);

	List<String> listFile(String directory);

	void createDirectory(String directory);

	boolean exists(String path);

	String getType();

	Long getSize(String key);
}
