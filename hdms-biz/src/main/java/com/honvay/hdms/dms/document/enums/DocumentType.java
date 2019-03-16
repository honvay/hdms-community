package com.honvay.hdms.dms.document.enums;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
public enum DocumentType {
	/**
	 * 文件
	 */
	FILE("file","文件"),
	/**
	 * 目录
	 */
	DIRECTORY("directory","目录");

	DocumentType(String code, String name) {
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

	public static boolean isDirectory(String type) {
		return DIRECTORY.getCode().equals(type);
	}

	public static boolean isFile(String type) {
		return FILE.getCode().equals(type);
	}

}
