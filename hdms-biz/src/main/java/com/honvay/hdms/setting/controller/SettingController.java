package com.honvay.hdms.setting.controller;

import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/setting")
public class SettingController extends BaseController {

	@Autowired
	private SettingService settingService;

	@PostMapping
	public Result<String> save(@RequestBody @Valid Setting setting) throws Exception {
		Setting setting2 = settingService.get();
		BeanUtils.copyProperties(setting,setting2);
		settingService.update(setting2);
		return success();
	}

	@GetMapping
	public Result<Setting> get() {
		return success(settingService.get());
	}
}
