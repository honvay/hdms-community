/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class KeyEncryptor {

	public static String generate(String password, String text) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(password);
		return encryptor.encrypt(text);
	}

	public static void main(String[] args) {
		//System.out.println(KeyEncryptor.generate("root", "111111"));
		Integer quato = 20;
		System.out.println((1024 * 1024 * 1024) * Long.valueOf(quato));
	}
}
