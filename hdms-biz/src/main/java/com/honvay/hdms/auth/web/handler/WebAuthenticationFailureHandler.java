package com.honvay.hdms.auth.web.handler;

import com.honvay.hdms.auth.WebAuthenticationConstant;
import com.honvay.hdms.framework.utils.WebUtils;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
