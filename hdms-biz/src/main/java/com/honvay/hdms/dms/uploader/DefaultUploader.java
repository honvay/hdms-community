package com.honvay.hdms.dms.uploader;

import com.honvay.hdms.dms.encryptor.Encryptors;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.model.request.UploadRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author LIQIU
 * created on 2019/3/3
 **/
public class DefaultUploader implements Uploader {

	@Getter
	@Setter
	private Storage storage;

	@Getter
	@Setter
	private Encryptors encryptors;

	@Override
	public String upload(UploadRequest request, MultipartFile multipartFile, Consumer<String> consumer) throws IOException {
		String key = UUID.randomUUID().toString().replaceAll("-", "");
		storage.store("file", key, encryptors.encrypt(multipartFile.getInputStream()), consumer);
		return key;
	}
}
