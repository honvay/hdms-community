package com.honvay.hdms.config;

import com.honvay.hdms.config.properties.StorageConfig;
import com.honvay.hdms.dms.encryptor.Encryptors;
import com.honvay.hdms.dms.encryptor.NullEncryptors;
import com.honvay.hdms.dms.storage.LocalStorage;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.uploader.MultiPartUploader;
import com.honvay.hdms.dms.uploader.Uploader;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
@Configuration
@EnableConfigurationProperties({StorageConfig.class})
public class StorageAutoConfiguration {

	@Autowired
	private SettingService settingService;

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		Setting setting = this.settingService.get();
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//文件最大
		factory.setMaxFileSize(DataSize.ofMegabytes(setting.getMaxUploadFileSize()));
		return factory.createMultipartConfig();
	}

	@Autowired
	private StorageConfig storageConfig;

	@Bean
	public Storage storage() {
		return new LocalStorage(storageConfig.getLocation());
	}

	@Bean
	public Encryptors encryptors() {
		return new NullEncryptors();
	}

	@Bean
	public Uploader multiPartUploader(Storage storage, StorageConfig storageConfig, Encryptors encryptors) {
		MultiPartUploader multiPartUploader = new MultiPartUploader();
		multiPartUploader.setEncryptors(encryptors);
		multiPartUploader.setStorage(storage);
		multiPartUploader.setStorageConfig(storageConfig);
		return multiPartUploader;
	}

}
