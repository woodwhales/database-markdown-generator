package org.woodwhales.config;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class DataBaseConfig {
	private String dbType = "MySQL5.7";
	private String schema = "test_gc";
	private String ip = "127.0.0.1";
	private int port = 3306;
	private String extraParam = "?useSSL=false";
	private String user = "root";
	private String password = "root";

	public String getUrl() {

		if(dbType.contains("MySQL")) {
			return getUrlPrefix()
					.concat(ip)
					.concat(":")
					.concat(String.valueOf(port))
					.concat("/")
					.concat(schema)
					.concat(this.extraParam);
		}

		if(dbType.contains("ORACLE")) {
			return getUrlPrefix()
					.concat(ip)
					.concat(":")
					.concat(String.valueOf(port));
		}

		return null;

	}

	public String getOriginUrl() {
		return getUrlPrefix()
				.concat(ip)
				.concat(":")
				.concat(String.valueOf(port))
				.concat(extraParam);
	}

	private String getUrlPrefix() {
		String urlPrefix = StringUtils.EMPTY;

		if(dbType.contains("MySQL")) {
			urlPrefix = "jdbc:mysql://";
		}

		if(dbType.contains("ORACLE")) {
			urlPrefix = "jdbc:oracle:thin:@";
		}

		if(StringUtils.isBlank(urlPrefix)) {
			new RuntimeException("this dbType is illegal!");
		}

		return urlPrefix;
	}

	public String getDriver() {
		String driver = StringUtils.EMPTY;

		if(dbType.contains("MySQL5.8")) {
			driver = "com.mysql.cj.jdbc.Driver";
		}

		if(dbType.contains("MySQL")) {
			driver = "com.mysql.jdbc.Driver";
		}

		if(dbType.contains("ORACLE")) {
			driver = "oracle.jdbc.OracleDriver";
		}

		if(StringUtils.isBlank(driver)) {
			new RuntimeException("this dbType is illegal!");
		}

		return driver;
	}


}
