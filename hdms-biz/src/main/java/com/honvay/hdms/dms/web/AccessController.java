/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.web;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.authentication.annotation.Authentication;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.encryptor.Encryptors;
import com.honvay.hdms.dms.event.DownloadEvent;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.storage.StorageDirectory;
import com.honvay.hdms.dms.token.AccessTokenStore;
import com.honvay.hdms.framework.utils.EncodeUtils;
import com.honvay.hdms.framework.utils.ServletUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 */
@Controller
@RequestMapping("/fs")
public class AccessController {

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private Encryptors encryptors;

	@Autowired
	private Storage storage;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AccessTokenStore accessTokenStore;

	@RequestMapping("/stream")
	public void stream(String token, HttpServletResponse response) throws IOException {
		String code = accessTokenStore.get(token);
		if (code != null) {
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			IOUtils.copy(storage.getInputStream(StorageDirectory.FILE, code), response.getOutputStream());
		}
	}

	@RequestMapping("/office")
	public Object preview(String token, HttpServletRequest request) {
		if (token != null) {
			String officePreviewServer = "http://view.officeapps.live.com/op/view.aspx?src=";
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/fs/stream?token=";
			String url = officePreviewServer + EncodeUtils.urlEncode(basePath + token);
			return "redirect:" + (url);
		}
		return null;
	}

	@RequestMapping("/raw")
	public void raw(String token, HttpServletResponse response) throws Exception {
		String code = accessTokenStore.get(token);
		if (code != null) {
			IOUtils.copy(storage.getInputStream(StorageDirectory.FILE, code), response.getOutputStream());
		}
	}

	@RequestMapping("/download")
	@Authentication(value = PermissionType.DOWNLOAD, multiple = true)
	public void download(@RequestParam Integer id, HttpServletResponse response,
						 AuthenticatedUser authenticatedUser) throws Exception {
		Document document = documentReadService.get(id);
		DownloadEvent downloadEvent = new DownloadEvent(document, authenticatedUser.getId());
		applicationEventPublisher.publishEvent(downloadEvent);
		ServletUtils.setFileDownloadHeader(response, document.getName());
		encryptors.decrypt(storage.getInputStream(StorageDirectory.FILE, document.getCode()), response.getOutputStream());
	}
}
