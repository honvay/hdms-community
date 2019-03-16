package com.honvay.hdms.dms.authorize.authentication.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
	
	String operation() default "";
	
	String group() default "";
	
	/**
	 * 权限
	 * @return
	 */
	String value();
	
	/**
	 * 验证的参数下标
	 * @return
	 */
	boolean multiple() default false;
	
	String delimiter() default ",";
	
}
