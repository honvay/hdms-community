/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
	 *
	 * @return
	 */
	String value();

	/**
	 * 验证的参数下标
	 *
	 * @return
	 */
	boolean multiple() default false;

	String delimiter() default ",";

}
