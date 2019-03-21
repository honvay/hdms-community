/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.web;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.permission.service.PermissionService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.model.AppSetting;
import com.honvay.hdms.model.Environment;
import com.honvay.hdms.notice.service.NoticeService;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import com.honvay.hdms.user.model.UserSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIQIU
 * created on 2019/2/24
 **/
@Controller
public class IndexController {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private NoticeService noticeService;

	@RequestMapping("/index/**")
	public String index() {
		return "index";
	}

	@ResponseBody
	@RequestMapping({"/environment","/api/environment"})
	public Result<Environment> get(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		UserSpace userSpace = new UserSpace();
		userSpace.setUsed(documentReadService.getUsedSpace(authenticatedUser.getId()));
		userSpace.setQuota(DataSize.ofGigabytes(authenticatedUser.getQuota()).toBytes());

		Environment environment = new Environment();
		environment.setUser(authenticatedUser);
		environment.setSpace(userSpace);
		environment.setPermissions(permissionService.list());
		environment.setNotice(noticeService.getAvailable());
		Map<String, Mount> mounts = new HashMap<>(2);
		mounts.put(authenticatedUser.getUserMount().getAlias(), authenticatedUser.getUserMount());
		mounts.put(authenticatedUser.getOrganizationMount().getAlias(), authenticatedUser.getOrganizationMount());
		environment.setMounts(mounts);

		AppSetting appSetting = new AppSetting();
		Setting setting = this.settingService.get();
		if (setting.getIncludeExtensions() != null) {
			appSetting.setIncludeExtensions(Arrays.asList(setting.getIncludeExtensions().split(",")));
		}
		if (setting.getExcludeExtensions() != null) {
			appSetting.setExcludeExtensions(Arrays.asList(setting.getExcludeExtensions().split(",")));
		}
		if (setting.getIncludeContentTypes() != null) {
			appSetting.setIncludeContentTypes(Arrays.asList(setting.getIncludeContentTypes().split("\r\n")));
		}
		if (setting.getExcludeContentTypes() != null) {
			appSetting.setExcludeContentTypes(Arrays.asList(setting.getExcludeContentTypes().split("\r\n")));
		}
		long multiPartSize = setting.getMultiPartSize() != null ? setting.getMultiPartSize() : 15L;
		appSetting.setMultiPartSize(DataSize.ofMegabytes(multiPartSize).toBytes());
		appSetting.setMinLengthOfPassword(setting.getMinLengthOfPassword());
		appSetting.setPasswordStrength(setting.getPasswordStrength());
		appSetting.setMaxUploadFileSize(setting.getMaxUploadFileSize());
		environment.setAppSetting(appSetting);
		return Result.success(environment);
	}

}
