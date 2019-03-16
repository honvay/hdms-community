package com.honvay.hdms.dms.authorize.authentication.annotation;

public @interface CompositionAuthentication {

	Authentication[] value();
	
	String operation() default "";
}
