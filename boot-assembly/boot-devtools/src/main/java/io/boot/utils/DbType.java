/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.utils;

/**
 * 数据库类型
 *
 * @author epiphyllum.zhou@gmail.com
 */
public enum DbType {
	/**
	 * 支持MySQL、Oracle、SQLServer、PostgreSQL
	 */
	MySQL("com.mysql.cj.jdbc.Driver"),
	Oracle("oracle.jdbc.driver.OracleDriver"),
	SQLServer("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	PostgreSQL("org.postgresql.Driver"),
	DM("dm.jdbc.driver.DmDriver");

	private final String driverClass;

	DbType(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getDriverClass() {
		return driverClass;
	}
}