/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.uploader;

import com.honvay.hdms.dms.encryptor.Encryptors;
import com.honvay.hdms.dms.model.request.UploadRequest;
import com.honvay.hdms.dms.storage.Storage;
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
