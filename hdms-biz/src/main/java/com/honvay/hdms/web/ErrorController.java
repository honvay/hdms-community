package com.honvay.hdms.web;

import com.honvay.hdms.framework.core.protocol.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@RequestMapping("/error/{code}")
public class ErrorController {

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String errorPage(@PathVariable String code){
		return "/error/" + code;
	}

	@ResponseBody
	@RequestMapping
	public Result<String> error(@PathVariable String code){
		return Result.fail(code);
	}

}
