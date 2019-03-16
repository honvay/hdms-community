package com.honvay.hdms.dms.uploader;

import com.honvay.hdms.dms.model.request.UploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author LIQIU
 * created on 2019/3/3
 **/
public interface Uploader {

	/**
	 * 处理文件上传
	 *
	 * @param request
	 * @param multipartFile
	 * @param consumer
	 * @throws IOException
	 */
	String upload(UploadRequest request, MultipartFile multipartFile, Consumer<String> consumer) throws IOException;

}
