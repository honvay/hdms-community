/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.web.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
