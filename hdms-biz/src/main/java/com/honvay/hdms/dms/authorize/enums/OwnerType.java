package com.honvay.hdms.dms.authorize.enums;

import java.util.stream.Stream;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
public enum OwnerType {
	/**
	 * 用户
	 */
	USER(1, "用户"),
	/**
	 * 部门
	 */
	DEPARTMENT(2, "部门");

	OwnerType(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Integer code;

	private String name;

	public static OwnerType of(Integer code) {
		return Stream.of(values())
				.filter(ownerType -> ownerType.getCode().equals(code))
				.findAny().orElse(null);
	}
}
