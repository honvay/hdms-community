/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.login.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.framework.utils.WebUtils;
import com.honvay.hdms.login.entity.LoginLog;
import com.honvay.hdms.login.mapper.LoginLogMapper;
import com.honvay.hdms.login.model.LoginLogDto;
import com.honvay.hdms.login.model.LoginLogQuery;
import com.honvay.hdms.login.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LIQIU
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, Integer> implements LoginLogService, ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private LoginLogMapper loginLogMapper;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
		LoginLog log = new LoginLog();
		log.setClient("Web");
		log.setLoginIp(WebUtils.getClientIp());
		log.setUserId(authenticatedUser.getId());
		log.setUserAgent(WebUtils.getUserAgent());
		log.setLoginDate(new Date());
		this.save(log);
	}

	@Override
	public Page<LoginLogDto> search(LoginLogQuery query) {
		query.setStart((query.getPage() - 1) * query.getSize());
		query.setEnd(query.getPage() * query.getSize());
		Page<LoginLogDto> page = new Page<>();
		page.setRecords(this.loginLogMapper.searchLoginLog(query));
		page.setTotal(this.loginLogMapper.countLoginLog(query));
		return page;
	}
}
