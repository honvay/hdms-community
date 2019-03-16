/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.web;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.config.properties.StorageConfig;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.document.service.DocumentWriteService;
import com.honvay.hdms.dms.model.request.UploadRequest;
import com.honvay.hdms.dms.model.request.VerifyRequest;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.uploader.Uploader;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author LIQIU
 */
@Slf4j
@Controller
public class UploadController extends BaseController {

	@Autowired
	private Storage storage;

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private DocumentWriteService documentWriteService;

	@Autowired
	private StorageConfig storageConfig;

	@Autowired
	private SettingService settingService;

	@Autowired
	private Uploader uploader;

	@Autowired
	private AuthorizeService authorizeService;

	@RequestMapping("/fs/upload")
	@CrossOrigin(methods = {RequestMethod.POST})
	@ResponseBody
	public Object upload(@Valid UploadRequest request, MultipartFile file,
						 @AuthenticationPrincipal AuthenticatedUser user) throws Exception {
		request.setUser(user);
		request.setContentType(file.getContentType());

		Assert.isTrue(request.getName().equals(file.getOriginalFilename()), "文件名称不匹配");

		Document document = null;
		String code = uploader.upload(request, file, s -> {
			if (log.isDebugEnabled()) {
				log.debug("文件：{}，上传成功", file.getOriginalFilename());
			}
		});
		if (code != null) {
			Assert.isTrue(authorizeService.checkPermission(user, request.getParent(), request.getMount(), PermissionType.UPLOAD), "无权限上传文件");
			request.setCode(code);
			document = documentWriteService.createFile(request);
		}
		return this.success(document);
	}

	public File getMd5ChunkDirectory(String md5, File temporaryDirectory) {
		return new File(temporaryDirectory, "/chunks/" + md5);
	}


	/**
	 * 获取已经上传的部分
	 *
	 * @param request
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fs/upload/verify")
	public Object verify(@RequestBody @Valid VerifyRequest request,
						 @AuthenticationPrincipal AuthenticatedUser user) {

		Setting setting = this.settingService.get();
		if (user.getId().equals(request.getMount()) && user.getQuota() != null) {
			Long used = documentReadService.getUsedSpace(user.getId());
			used = used != null ? used : 0L;
			if (DataSize.ofGigabytes(user.getQuota()).compareTo(DataSize.ofBytes(used + request.getSize())) < 0) {
				return this.failure("个人空间不足");
			}
		}
		if (setting.getMaxUploadFileSize() != null) {
			if (DataSize.ofMegabytes(setting.getMaxUploadFileSize()).compareTo(DataSize.ofBytes(request.getSize())) < 0) {
				return this.failure("文件大小超过了限制");
			}
		}
		if (StringUtils.isNotEmpty(setting.getIncludeExtensions())) {
			List<String> extensions = Arrays.asList(setting.getIncludeExtensions().split(","));
			if (!extensions.contains(request.getExt())) {
				return this.failure("该文件类型不能上传");
			}
		}
		if (StringUtils.isNotEmpty(setting.getExcludeExtensions())) {
			List<String> extensions = Arrays.asList(setting.getExcludeExtensions().split(","));
			if (extensions.contains(request.getExt())) {
				return this.failure("该文件类型不能上传");
			}
		}

		Assert.isTrue(authorizeService.checkPermission(user, request.getParent(), request.getMount(), PermissionType.UPLOAD), "无权限上传文件");

		return this.success();
	}

	/**
	 * 获取已经上传的部分
	 *
	 * @param md5
	 * @return
	 */
	@RequestMapping("/fs/upload/checkMd5")
	@ResponseBody
	public Result<Boolean> check(String md5) {
		return this.success(this.documentReadService.getByMd5(md5) != null);
	}

	/**
	 * 获取已经上传的部分
	 *
	 * @param md5
	 * @return
	 */
	@RequestMapping("/fs/upload/chunk/check")
	@ResponseBody
	public Object checkChunk(String md5, String chunk, String chunkSize) {
		File temporaryDirectory = this.storageConfig.getTemporaryDirectory();
		//新建分片文件夹
		File chunkFile = new File(this.getMd5ChunkDirectory(md5, temporaryDirectory), chunk);
		if (chunkFile.exists()) {
			if (Long.valueOf(chunkFile.length()).compareTo(Long.valueOf(chunkSize)) == 0) {
				return this.success();
			}
		}
		return this.failure("分片不存在");
	}
}
