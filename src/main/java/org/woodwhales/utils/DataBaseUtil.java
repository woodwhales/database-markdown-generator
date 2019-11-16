package org.woodwhales.utils;

import static org.woodwhales.constants.DBConstant.TABLE_CAT;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.woodwhales.config.DataBaseConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DataBaseUtil {

	private DataBaseConfig dataBaseConfig;

	public static DataBaseUtil newInstance(DataBaseConfig dataBaseConfig) {
		return new DataBaseUtil(dataBaseConfig);
	}

	private DataBaseUtil(DataBaseConfig dataBaseConfig) {
		this.dataBaseConfig = dataBaseConfig;
	}

	/**
	 * 获取数据库链接
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	public Connection getConnection(String url) {
		Connection connection = null;
		try {
			Class.forName(dataBaseConfig.getDriver());
			connection = DriverManager.getConnection(url, dataBaseConfig.getUser(), dataBaseConfig.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 获取所有数据库名称
	 * @return
	 */
	public List<String> getSchemas() {
		List<String> list = new ArrayList<String>();
		DatabaseMetaData metaData;
		try {
			Connection connection = getConnection(dataBaseConfig.getOriginUrl());
			metaData = connection.getMetaData();
			ResultSet resultSet = metaData.getCatalogs();
			while(resultSet.next()) {
				list.add(resultSet.getString(TABLE_CAT));
			}
			close(resultSet);
			close(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void getTables(String schema) {
		Connection connection = getConnection(dataBaseConfig.getUrl());
		try {
			String catalog = connection.getCatalog();

			System.out.println("catalog = "+catalog);
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet tablerResultSet = metaData.getTables(catalog, schema, null, new String[]{"TABLE"});

			while(tablerResultSet.next()) {

				String tableName = tablerResultSet.getString("TABLE_NAME");

				ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
				while (primaryKeys.next()) {
					String columnName = primaryKeys.getString("COLUMN_NAME");
	                short keySeq = primaryKeys.getShort("KEY_SEQ"); //$NON-NLS-1$
	                log.debug("columnName -> {}, primaryKeys -> {}", columnName, keySeq);
				}
			}

			close(tablerResultSet);
			close(connection);
		} catch (SQLException e) {
			log.error("getTables process is fail");
			e.printStackTrace();
		}

	}

	private void close(Connection connection) {
		if(Objects.isNull(connection)) {
			return;
		}
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("connection close process is fail");
			e.printStackTrace();
		}
	}

	private void close(ResultSet resultSet) {
		if(Objects.isNull(resultSet)) {
			return;
		}
		
		try {
			resultSet.close();
		} catch (SQLException e) {
			log.error("resultSet close process is fail");
			e.printStackTrace();
		}
	}
}

