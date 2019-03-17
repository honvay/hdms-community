/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.controller;

import com.honvay.hdms.framework.core.protocol.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LIQIU
 * created on 2018-11-25
 **/
@Slf4j
@Controller
@RequestMapping("/session-invalid")
public class SessionInvalidController {

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public View sessionInvalidPage(HttpServletRequest request) {
		return new RedirectView("/login?session");
	}

	@ResponseBody
	@RequestMapping
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Result<String> sessionInvalid() {
		return Result.success("会话已失效");
	}

}
