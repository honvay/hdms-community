/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.hdms.framework.core.protocol.Result;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

		exception.printStackTrace();

		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		objectMapper.writeValue(response.getOutputStream(), Result.fail(exception.getMessage()));
		response.getOutputStream().close();
	}
}
