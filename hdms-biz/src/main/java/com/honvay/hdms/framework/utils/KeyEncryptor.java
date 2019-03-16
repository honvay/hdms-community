package com.honvay.hdms.framework.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class KeyEncryptor {

	public static String generate(String password,String text){
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
