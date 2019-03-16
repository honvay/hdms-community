package com.honvay.hdms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
@Data
@ConfigurationProperties("hdms.storage")
public class StorageConfig {

	private String location;

	private OssConfig ossConfig;

	private Integer userQuota;

	public File getTemporaryDirectory() {
		return getDirectory("tmp");
	}

	public File getDataDirectory() {
		return getDirectory("data");
	}

	public File getStoreDirectory() {
		return getDirectory("file");
	}

	private File getDirectory(String name) {
		File directory = new File(this.location + "/" + name);
		if (!directory.exists() || !directory.isDirectory()) {
			directory.mkdirs();
		}
		return directory;
	}

	@Data
	public static class OssConfig {
		private String name;
		private String endpoint;
		private String accessKeyId;
		private String accessKeySecret;
		private String bucketName;
	}

}
