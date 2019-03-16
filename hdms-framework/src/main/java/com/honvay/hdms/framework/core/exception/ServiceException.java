package com.honvay.hdms.framework.core.exception;

import com.honvay.hdms.framework.core.ErrorMessage;

/**
 * @author LIQIU
 * created on 2018/12/24
 **/
public class ServiceException extends RuntimeException {

	private String code;

	public ServiceException(String code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public ServiceException(String code, String message) {
		this(code, message, null);
	}

	public ServiceException(ErrorMessage errorMessage) {
		this(errorMessage.getCode(), errorMessage.getMessage(), null);
	}

	public ServiceException(ErrorMessage errorMessage, Throwable throwable) {
		this(errorMessage.getCode(), errorMessage.getMessage(), throwable);
	}

	public ServiceException(ErrorMessage errorMessage, String message) {
		this(errorMessage.getCode(), message, null);
	}

	public ServiceException(ErrorMessage errorMessage, String message, Throwable throwable) {
		this(errorMessage.getCode(), message, throwable);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
