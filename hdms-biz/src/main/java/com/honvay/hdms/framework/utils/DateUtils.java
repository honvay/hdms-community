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
