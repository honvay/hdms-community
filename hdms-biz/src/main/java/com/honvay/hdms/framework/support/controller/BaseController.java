package com.honvay.hdms.framework.support.controller;

import com.honvay.hdms.framework.core.protocol.Result;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
public class BaseController {

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	public <T> Result<T> success() {
		return Result.success();
	}

	public <T> Result<T> success(String message) {
		return Result.success(message);
	}

	public <T> Result<T> success(T data) {
		return Result.success(data);
	}

	public <T> Result<T> failure(String message) {
		return Result.fail(message);
	}



}
