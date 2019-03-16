/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.hdms.framework.core.protocol.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LIQIU
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static ObjectMapper mapper = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<String> resolveMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException ex) {
		return Result.fail("参数错误");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String resolveAccessDeniedException() {
		return "error/401";
	}


	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Result resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		String error = "Request uri : %s  %s  error occurred %s";
		logger.error(String.format(error, request.getRequestURI(), null, ex.toString()));
		ex.printStackTrace();
		return Result.fail(ex.getMessage());
	}

}
