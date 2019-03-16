/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.enums;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
public enum DocumentAccessibility {

	/**
	 * 文件
	 */
	PUBLIC("public", "公开的"),
	/**
	 * 目录
	 */
	PRIVATE("private", "私有的");

	DocumentAccessibility(String code, String name) {
		this.code = code;
		this.name = name;
	}

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

	private String code;

	private String name;

	public static boolean isPrivate(String type) {
		return PRIVATE.getCode().equals(type);
	}

	public static boolean isPublic(String type) {
		return PUBLIC.getCode().equals(type);
	}
}
