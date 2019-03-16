package com.honvay.hdms.dms.document.enums;

import java.util.stream.Stream;

/**
 * @author LIQIU
 * created on 2019/2/28
 **/
public enum MountType {
	/**
	 * 我的文档
	 */
	MY("my", 1, "我的文档"),

	/**
	 * 企业文档
	 */
	ENTERPRISE("enterprise", 2, "企业文档");

	MountType(String code, Integer value, String name) {
		this.code = code;
		this.value = value;
		this.name = name;
	}

	private String code;

	private String name;

	private Integer value;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static MountType of(String code) {
		if (MY.getCode().equals(code)) {
			return MY;
		}
		if (ENTERPRISE.getCode().equals(code)) {
			return ENTERPRISE;
		}
		return null;
	}

	public static MountType of(Integer code) {
		if (MY.getValue().equals(code)) {
			return MY;
		}
		if (ENTERPRISE.getValue().equals(code)) {
			return ENTERPRISE;
		}
		return null;
	}
}
