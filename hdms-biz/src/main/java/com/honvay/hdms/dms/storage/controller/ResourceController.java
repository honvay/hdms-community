/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.storage.controller;

import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.storage.StorageDirectory;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Controller
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private Storage storage;

	@RequestMapping("/avatar/{code}")
	public void avatar(@PathVariable String code, HttpServletResponse response) throws IOException {
		@Cleanup
		InputStream avatar = storage.getInputStream(StorageDirectory.AVATAR, code);
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		IOUtils.copy(avatar, response.getOutputStream());
	}

}
