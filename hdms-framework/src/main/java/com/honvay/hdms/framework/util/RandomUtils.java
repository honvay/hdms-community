/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.util;

import java.util.Random;

/**
 * 验证码生成器
 *
 * @author LIQIU
 */
public class RandomUtils {

	public static final String VERIFY_CODES = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final String NUMBER_VERIFY_CODES = "012345679";
	private static Random random = new Random();


	/**
	 * 使用系统默认字符源生成验证码
	 *
	 * @param verifySize 验证码长度
	 * @return
	 */
	public static String generateString(int verifySize) {
		return generateString(verifySize, VERIFY_CODES);
	}

	/**
	 * 使用系统默认字符源生成数字验证码
	 *
	 * @param verifySize 验证码长度
	 * @return
	 */
	public static String generateNumber(int verifySize) {
		return generateString(verifySize, NUMBER_VERIFY_CODES);
	}

	/**
	 * 使用指定源生成验证码
	 *
	 * @param verifySize 验证码长度
	 * @param sources    验证码字符源
	 * @return
	 */
	public static String generateString(int verifySize, String sources) {
		if (sources == null || sources.length() == 0) {
			sources = VERIFY_CODES;
		}
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for (int i = 0; i < verifySize; i++) {
			verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}
}