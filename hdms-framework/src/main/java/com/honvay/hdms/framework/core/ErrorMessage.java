package com.honvay.hdms.framework.core;

/**
 * @author LIQIU
 * created on 2018/12/24
 **/
public interface ErrorMessage {

	/**
	 * 获取错误码
	 *
	 * @return 错误码
	 */
	String getCode();

	/**
	 * 获取错误信息
	 *
	 * @return 错误信息
	 */
	String getMessage();

}
