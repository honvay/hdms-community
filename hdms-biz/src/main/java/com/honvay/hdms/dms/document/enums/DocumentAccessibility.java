package com.honvay.hdms.dms.document.enums;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
public enum DocumentAccessibility {

	/**
	 * 文件
	 */
	PUBLIC("public","公开的"),
	/**
	 * 目录
	 */
	PRIVATE("private","私有的");

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
