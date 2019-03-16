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
		super.onAuthenticationSuccess(request,response,authentication);
	}
}
