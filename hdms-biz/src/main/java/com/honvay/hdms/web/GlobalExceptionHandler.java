/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void resolveMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException ex) throws IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "参数错误");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public void resolveAccessDeniedException(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权限访问");
	}

	@ResponseBody
	@ExceptionHandler({Exception.class})
	public void resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
		String error = "Request uri : %s  %s  error occurred %s";
		logger.error(String.format(error, request.getRequestURI(), null, ex.toString()));
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		ex.printStackTrace();
	}

}
