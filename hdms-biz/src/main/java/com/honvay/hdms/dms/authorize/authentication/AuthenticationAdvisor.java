/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.authorize.authentication;

import com.honvay.hdms.dms.authorize.authentication.annotation.Authentication;
import com.honvay.hdms.dms.authorize.authentication.annotation.CompositionAuthentication;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class AuthenticationAdvisor extends StaticMethodMatcherPointcutAdvisor implements InitializingBean {

	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;

	/**
	 *
	 */
	private static final long serialVersionUID = 545158457213842666L;

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return (AnnotationUtils.findAnnotation(method, Authentication.class) != null
				|| AnnotationUtils.findAnnotation(method, CompositionAuthentication.class) != null);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setAdvice(authenticationInterceptor);
	}


}
