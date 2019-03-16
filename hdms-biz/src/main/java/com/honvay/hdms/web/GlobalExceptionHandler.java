package com.honvay.hdms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
