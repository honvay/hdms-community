package com.honvay.hdms.login.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.login.model.LoginLogDto;
import com.honvay.hdms.login.model.LoginLogQuery;
import com.honvay.hdms.login.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/login/log")
public class LoginLogController extends BaseController {

	@Autowired
	private LoginLogService loginLogService;

	@RequestMapping("/search")
	public Result<Page<LoginLogDto>> search(LoginLogQuery query){
		return this.success(this.loginLogService.search(query));
	}
}
