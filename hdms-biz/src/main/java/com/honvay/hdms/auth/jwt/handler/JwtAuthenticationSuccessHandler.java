/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.hdms.auth.jwt.store.JwtTokenProvider;
import com.honvay.hdms.framework.core.protocol.Result;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private JwtTokenProvider jwtTokenProvider;

	public JwtAuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String token = jwtTokenProvider.generate(authentication);
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		objectMapper.writeValue(response.getOutputStream(), Result.success("登录成功", token));
		response.getOutputStream().close();
	}
}
