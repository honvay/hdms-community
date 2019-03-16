package com.honvay.hdms.auth.web.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * @author LIQIU
 * created on 2018/12/24
 **/
@Configuration
@AutoConfigureBefore(WebSecurityConfiguration.class)
public class WebSessionConfiguration {

	@Bean
	@ConditionalOnBean(FindByIndexNameSessionRepository.class)
	public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(FindByIndexNameSessionRepository repository) {
		return new SpringSessionBackedSessionRegistry(repository);
	}

	@Bean
	@ConditionalOnMissingBean(SessionRegistry.class)
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
		return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
	}

}
