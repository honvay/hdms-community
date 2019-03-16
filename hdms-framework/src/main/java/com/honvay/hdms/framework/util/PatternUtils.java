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
