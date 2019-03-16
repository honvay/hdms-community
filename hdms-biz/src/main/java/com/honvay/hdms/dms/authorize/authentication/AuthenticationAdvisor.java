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
public class AuthenticationAdvisor extends StaticMethodMatcherPointcutAdvisor implements InitializingBean{
	
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
