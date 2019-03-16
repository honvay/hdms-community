/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.login.controller;

import com.honvay.hdms.auth.WebAuthenticationConstant;
import com.honvay.hdms.auth.web.captcha.filter.CaptchaAuthenticationFilter;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.framework.util.ImageUtils;
import com.honvay.hdms.framework.util.RandomUtils;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 */
@Slf4j
@Controller
public class LoginController extends BaseController {

	@Autowired
	private SettingService settingService;

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request) {
		Setting setting = settingService.get();
		if (setting.getShowCaptcha() && !setting.getShowCaptchaOnError()) {
			request.getSession().setAttribute(WebAuthenticationConstant.CAPTCHA_AUTHENTICATION_REQUIRED_KEY, true);
		}
		return "login";
	}

	@RequestMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = RandomUtils.generateString(settingService.get().getCaptchaLength());
		request.getSession().setAttribute(CaptchaAuthenticationFilter.LOGIN_CAPTCHA_SESSION_KEY, code);
		ImageUtils.outputImage(200, 60, response.getOutputStream(), code);
	}


}
