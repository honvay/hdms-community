/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * 认证失败逻辑处理，页面登录和Token认证都会走这个逻辑
 *
 * @author LIQIU
 * created on 2018-11-19
 **/
@Component
@Slf4j
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		//只有账号密码登录才回更新登录失败次数
		if (event.getAuthentication().getClass().equals(UsernamePasswordAuthenticationToken.class)) {
			log.info("Authentication failure: " + event.getAuthentication().getName());
		}
	}
}
