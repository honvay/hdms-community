package com.honvay.hdms.auth.jwt.listener;

import com.honvay.hdms.auth.jwt.store.JwtTokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * created on 2018-11-19
 **/
@Slf4j
public class JwtAuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private JwtTokenStore jwtTokenStore;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		jwtTokenStore.save(event.getAuthentication().getName(), event.getAuthentication());
		if (log.isDebugEnabled()) {
			log.debug("Jwt token: [{}] store success", event.getAuthentication().getName());
		}
	}
}
