/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.enums;

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
