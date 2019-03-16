/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
public class FileEncryptor {


	static {
		//Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 初始化 AES Cipher
	 *
	 * @param password
	 * @param cipherMode
	 * @return
	 */
	public static Cipher initAESCipher(String password, int cipherMode) {
		// 创建Key gen
		KeyGenerator keyGenerator = null;
		Cipher cipher = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(256, new SecureRandom(password.getBytes()));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] codeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
			cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			//cipher = Cipher.getInstance("AES");
			// 初始化
			cipher.init(cipherMode, key);
			return cipher;
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
		}
		return null;
	}

	/**
	 * 对文件进行AES加密
	 *
	 * @param file
	 * @param password
	 * @return
	 */
	public static InputStream encrypt(File file, String password) {
		try {
			return encrypt(new FileInputStream(file), password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对文件进行AES加密
	 *
	 * @param source
	 * @param target
	 * @param password
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void encrypt(File source, File target, String password) throws Exception {
		InputStream input = new FileInputStream(source);
		OutputStream ouput = new FileOutputStream(target);
		log.debug("开始写入加密文件");
		long start = System.currentTimeMillis();
		CipherInputStream cipherInputStream = (CipherInputStream) encrypt(input, password);
		IOUtils.copy(cipherInputStream, ouput);
		cipherInputStream.close();
		input.close();
		ouput.close();
		log.debug("完成文件加密，耗时：" + (System.currentTimeMillis() - start));
	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:/洪都条码管理系统规划方案v15.0923 - Liuy.docx");
		File encrypted = new File(file.getParent(), file.getName() + "_encrypted");
		System.out.println("加密");
		FileEncryptor.encrypt(file, encrypted, "123");
		file.delete();
		System.out.println("解密");
		FileEncryptor.decrypt(encrypted, file, "123");
		//encrypted.renameTo(file);
	}

	/**
	 * 对文件进行AES加密
	 *
	 * @param input
	 * @param target
	 * @param password
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void encrypt(InputStream input, File target, String password) throws Exception {
		OutputStream ouput = new FileOutputStream(target);
		input = encrypt(input, password);
		log.debug("开始写入加密文件");
		long start = System.currentTimeMillis();
		IOUtils.copy(input, ouput);
		ouput.close();
		input.close();
		log.debug("完成文件加密，耗时：" + (System.currentTimeMillis() - start));
	}

	/**
	 * 对文件进行AES加密
	 *
	 * @param input
	 * @param password
	 * @return
	 */
	public static InputStream encrypt(InputStream input, String password) {
		//log.debug("开始加密文件流");
		//long start = System.currentTimeMillis();
		Cipher cipher = initAESCipher(password, Cipher.ENCRYPT_MODE);
		//log.debug("生成密钥耗时：" + (System.currentTimeMillis() - start));
		Assert.notNull(cipher);
		// 以加密流写入文件
		CipherInputStream cipherInputStream = new CipherInputStream(input, cipher);
		//log.debug("完成加密文件流，耗时：" + (System.currentTimeMillis() - start));
		return cipherInputStream;
	}
	

	/*public static void main(String[] args) {
		File owner = new File("D:/hdms/temp/402880ea5c4981de015c49854a570001_e");
		try {
			owner.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File("D:/hdms/temp/402880ea5c4981de015c49854a570001");
		FileEncryptor.decrypt(file, owner, "132221f6fa2d0da4");
	}*/

	public static void decrypt(File source, File target, String password) {
		try {
			decrypt(new FileInputStream(source), new FileOutputStream(target), password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void decrypt(File file, OutputStream output, String password) {
		try {
			decrypt(new FileInputStream(file), output, password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static InputStream decrypt(InputStream input, String password) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		decrypt(input, output, password);
		return new ByteArrayInputStream(output.toByteArray());
	}

	public static void decrypt(InputStream input, OutputStream output, String password) {
		try {
			Cipher cipher = initAESCipher(password, Cipher.DECRYPT_MODE);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(output, cipher);
			IOUtils.copy(input, cipherOutputStream);
			cipherOutputStream.close();
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String uuid() {
		return UUID.randomUUID().toString();
	}
}
