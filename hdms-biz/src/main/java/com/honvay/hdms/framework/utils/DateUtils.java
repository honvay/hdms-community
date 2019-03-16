/*
 *  Copyright 2014-2015 snakerflow.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing authorizes and
 *  * limitations under the License.
 *
 */
/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author yuqs
 * @since 0.1
 */
public class DateUtils {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

	public static String formatDateTime(Date date) {
		return new DateTime(date).toString(DATE_FORMAT_DEFAULT);
	}

	public static String formatDate(Date date) {
		return new DateTime(date).toString(DATE_FORMAT_YMD);
	}

	public static String getCurrentTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}

	public static String getCurrentDay() {
		return new DateTime().toString(DATE_FORMAT_YMD);
	}
}
