/*
 *  Copyright 2013-2014 the original author or authors.
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;

/**
 * Jdbc的工具类
 *
 * @author yuqs
 * @since 1.0
 */
public class JdbcUtils {
	private static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();

	private static Properties getDefaultDatabaseTypeMappings() {
		Properties databaseTypeMappings = new Properties();
		databaseTypeMappings.setProperty("H2", "h2");
		databaseTypeMappings.setProperty("MySQL", "mysql");
		databaseTypeMappings.setProperty("Oracle", "oracle");
		databaseTypeMappings.setProperty("PostgreSQL", "postgres");
		databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
		return databaseTypeMappings;
	}

	/**
	 * 根据连接对象获取数据库类型
	 *
	 * @param conn 数据库连接
	 * @return 类型
	 * @throws Exception
	 */
	public static String getDatabaseType(Connection conn) throws Exception {
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		String databaseProductName = databaseMetaData.getDatabaseProductName();
		return databaseTypeMappings.getProperty(databaseProductName);
	}
}
