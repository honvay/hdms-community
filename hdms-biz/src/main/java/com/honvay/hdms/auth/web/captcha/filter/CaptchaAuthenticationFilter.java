/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.web.captcha.filter;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIQIU
 * created on 2018-11-20
 **/
public class CaptchaAuthenticationFilter extends GenericFilterBean {

	public static final String LOGIN_CAPTCHA_SESSION_KEY = "login_captcha";

	public static final String LOGIN_CAPTCHA_PARAM_NAME = "captcha";

	private Map<RequestMatcher, AuthenticationFailureHandler> requestMatcherMap = new HashMap<>();

	public void addRequestMatcher(RequestMatcher requestMatcher, AuthenticationFailureHandler handler) {
		this.requestMatcherMap.put(requestMatcher, handler);
	}

	private AuthenticationFailureHandler requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		for (RequestMatcher matcher : requestMatcherMap.keySet()) {
			if (matcher.matches(request)) {
				return requestMatcherMap.get(matcher);
			}
		}
		return null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		AuthenticationFailureHandler authenticationFailureHandler = requiresAuthentication(request, response);
		if (authenticationFailureHandler == null) {
			chain.doFilter(request, response);
			return;
		}

		Object captcha = request.getSession().getAttribute(LOGIN_CAPTCHA_SESSION_KEY);

		if (captcha == null) {
			chain.doFilter(request, response);
		} else {
			if (!String.valueOf(captcha).equalsIgnoreCase(request.getParameter(LOGIN_CAPTCHA_PARAM_NAME))) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, new InsufficientAuthenticationException("验证码错误"));
			} else {
				chain.doFilter(request, response);
			}
		}
	}
}