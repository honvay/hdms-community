/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
	public String errorPage(@PathVariable String code) {
		return "/error/" + code;
	}

	@ResponseBody
	@RequestMapping
	public Result<String> error(@PathVariable String code) {
		return Result.fail(code);
	}

}
