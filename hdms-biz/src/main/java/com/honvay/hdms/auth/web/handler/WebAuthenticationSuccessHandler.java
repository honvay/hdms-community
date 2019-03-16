/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.web.handler;

import com.honvay.hdms.auth.WebAuthenticationConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public class WebAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		request.getSession().removeAttribute(WebAuthenticationConstant.AUTHENTICATION_FAIL_MESSAGE_KEY);
		request.getSession().removeAttribute(WebAuthenticationConstant.CAPTCHA_AUTHENTICATION_REQUIRED_KEY);
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
