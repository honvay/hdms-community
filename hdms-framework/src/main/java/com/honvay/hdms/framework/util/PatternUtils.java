/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式工具类，用于检查各种格式
 *
 * @author LIQIU
 * created on 2018/12/25
 **/
public class PatternUtils {

	private static Pattern PHONE_NUMBER_PATTERN;

	static {
		PHONE_NUMBER_PATTERN = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$");
	}


	public static final boolean isPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() != 11) {
			return false;
		} else {
			Matcher m = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
			return m.matches();
		}
	}

}
