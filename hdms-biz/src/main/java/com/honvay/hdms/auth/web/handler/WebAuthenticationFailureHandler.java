/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.web.handler;

import com.honvay.hdms.auth.WebAuthenticationConstant;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * created on 2018-11-24
 **/
@Component
public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private SettingService settingService;

	public WebAuthenticationFailureHandler() {
		super("/login?error");
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		request.getSession().setAttribute(WebAuthenticationConstant.AUTHENTICATION_FAIL_MESSAGE_KEY, exception.getMessage());
		Setting setting = settingService.get();
		if (setting.getShowCaptcha()) {
			request.getSession().setAttribute(WebAuthenticationConstant.CAPTCHA_AUTHENTICATION_REQUIRED_KEY, true);
		}
		super.onAuthenticationFailure(request, response, exception);
	}
}
