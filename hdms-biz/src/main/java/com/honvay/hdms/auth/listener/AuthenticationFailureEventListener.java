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
