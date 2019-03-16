/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.core;

/**
 * @author LIQIU
 * @date 2018-1-12
 **/
public class ErrorConstants implements ErrorMessage {


	// 1xx Informational

	/**
	 * 系统内部错误
	 */
	public static final ErrorConstants INTERNAL_SERVER_ERROR = new ErrorConstants("10000", "系统错误");
	/**
	 * 参数错误
	 */
	public static final ErrorConstants ILLEGAL_ARGUMENT = new ErrorConstants("10001", "参数错误");
	/**
	 * 业务错误
	 */
	public static final ErrorConstants SERVICE_EXCEPTION = new ErrorConstants("10002", "业务错误");
	/**
	 * 非法的数据格式，参数没有经过校验
	 */
	public static final ErrorConstants ILLEGAL_DATA = new ErrorConstants("10003", "数据错误");
	/**
	 * 非法状态
	 */
	public static final ErrorConstants ILLEGAL_STATE = new ErrorConstants("10005", "非法状态");
	/**
	 * 缺少参数
	 */
	public static final ErrorConstants MISSING_ARGUMENT = new ErrorConstants("10006", "缺少参数");
	/**
	 * 非法访问
	 */
	public static final ErrorConstants ACCESS_DEFINED = new ErrorConstants("10007", "非法访问,没有认证");
	/**
	 * 权限不足
	 */
	public static final ErrorConstants UNAUTHORIZED = new ErrorConstants("10008", "权限不足");

	/**
	 * 错误的请求
	 */
	public static final ErrorConstants METHOD_NOT_ALLOWED = ErrorConstants.of("10009", "不支持的方法");

	/**
	 * 参数错误
	 */
	public static final ErrorConstants ILLEGAL_ARGUMENT_TYPE = ErrorConstants.of("10010", "参数类型错误");


	private final String code;

	private final String message;


	ErrorConstants(String value, String message) {
		this.code = value;
		this.message = message;
	}

	public static ErrorConstants of(String code, String message) {
		return new ErrorConstants(code, message);
	}


	@Override
	public String getCode() {
		return this.code;
	}

	/**
	 * Return the reason phrase of this status credential.
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

}
