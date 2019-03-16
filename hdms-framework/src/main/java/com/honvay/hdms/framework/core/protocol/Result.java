/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.core.protocol;

import com.honvay.hdms.framework.core.ErrorMessage;


/**
 * @author LIQIU
 * created on 2018/12/24
 **/
public class Result<T> {

	/**
	 * 结果是否成功
	 */
	private Boolean success;

	/**
	 * 返回信息
	 */
	private String message;

	/**
	 * 返回码
	 */
	private String code;

	/**
	 * 数据
	 */
	private T data;


	private Result(Boolean success, String message, String code, T data) {
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 构建返回结果
	 *
	 * @param success 是否成功
	 * @param msg     错误信息
	 * @param code    错误码
	 * @param data    数据
	 * @param <T>     数据类型
	 * @return 结果
	 */
	public static <T> Result<T> of(Boolean success, String msg, String code, T data) {
		return new Result<>(success, msg, code, data);
	}

	/**
	 * 构建返回结果，code默认值为0
	 *
	 * @param success 是否成功
	 * @param msg     错误信息
	 * @param data    数据
	 * @param <T>     数据类型
	 * @return 结果
	 */
	public static <T> Result<T> of(Boolean success, String msg, T data) {
		return of(success, msg, "0", data);
	}

	/**
	 * 构建成功结果
	 *
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Result<T> success(String msg, T data) {
		return of(Boolean.TRUE, msg, data);
	}

	/**
	 * 构建失败结果
	 *
	 * @param code 错误码
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(String code, String msg, T data) {
		return of(Boolean.FALSE, msg, code, data);
	}

	/**
	 * 构建失败结果
	 *
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(String msg, T data) {
		return of(Boolean.FALSE, msg, data);
	}

	/**
	 * 构建成功结果带信息
	 *
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Result<T> success() {
		return success(null, null);
	}

	/**
	 * 构建成功结果带信息
	 *
	 * @param msg 错误信息
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Result<T> success(String msg) {
		return success(msg, null);
	}

	/**
	 * 构建成功结果待数据
	 *
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Result<T> success(T data) {
		return success(null, data);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param msg 错误信息
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(String msg) {
		return of(Boolean.FALSE, msg, null);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param code 错误码
	 * @param msg  错误信息
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(String code, String msg) {
		return of(Boolean.FALSE, msg, code, null);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param errorMessage 错误数据
	 * @param <T>          数据类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(ErrorMessage errorMessage) {
		return fail(errorMessage.getCode(), errorMessage.getMessage());
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param errorMessage 错误信息
	 * @param data         数据
	 * @param <T>          类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(ErrorMessage errorMessage, T data) {
		return fail(errorMessage.getCode(), errorMessage.getMessage(), data);
	}

	/**
	 * 构建失败结果带数据
	 *
	 * @param data 数据
	 * @param <T>  类型
	 * @return 结果
	 */
	public static <T> Result<T> fail(T data) {
		return fail("", data);
	}

	/**
	 * 构建失败结果带数据
	 *
	 * @param <T> 类型
	 * @return 结果
	 */
	public static <T> Result<T> fail() {
		return fail("", null);
	}
}
