package org.woodwhales.model.db;

import static org.woodwhales.constants.DBConstant.TABLE;
import static org.woodwhales.constants.DBConstant.TABLE_NAME;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@ToString
@Slf4j
public class SchemaInfo {
	private String schemaName;
	private List<Tabel> tabelInfoList;

	public static SchemaInfo newInstance(Connection connection) {
		Assert.notNull(connection, "数据库链接不允许为空");
		return genSchemaInfo(connection);
	}

	private static SchemaInfo genSchemaInfo(Connection connection) {
		String schemaName = getSchema(connection);
		List<Tabel> tableInfoList = getTableInfoList(connection);

		return SchemaInfo.builder()
				.schemaName(schemaName)
				.tabelInfoList(tableInfoList)
				.build();
	}

	private static String getSchema(Connection connection) {
		try {
			return connection.getCatalog();
		} catch (SQLException e) {
			return StringUtils.EMPTY;
		}
	}

	private static List<Tabel> getTableInfoList(Connection connection) {
		List<Tabel> tabelInfos = new ArrayList<Tabel>();
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			log.debug("catalog = {}", connection.getCatalog());
			// getTables(); 第一个参数传null，表示查所有数据库表
			ResultSet tableResultSet = metaData.getTables(connection.getCatalog(), null, null, new String[] { TABLE });
			while (tableResultSet.next()) {
				String tableName = tableResultSet.getString(TABLE_NAME);
				log.debug("tableName = {} ", tableName);
				ResultSet columnResultSet = metaData.getColumns(null, "%", tableName, "%");

				while (columnResultSet.next()) {
					Tabel table = Tabel.newInstance(tableName, columnResultSet);

					table.getTableColumns().add(0, getPrimaryKey(metaData, tableName));
					tabelInfos.add(table);
				}
				columnResultSet.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tabelInfos;
	}

	private static TableColumn getPrimaryKey(DatabaseMetaData metaData, String tableName) {

		TableColumn tableColumn = null;
		try {
			ResultSet resultSet = metaData.getPrimaryKeys(null, "hello", tableName);

			while(resultSet.next()) {
				//TODO 目前支持主键只有一个  获取主键的注释和类型
				tableColumn = TableColumn.builder()
									.columnName(resultSet.getString("COLUMN_NAME"))
									.columnType("BIGINT")
									.columnSize(20)
									.nullAble(Boolean.FALSE.booleanValue())
									.deaultValue(StringUtils.EMPTY)
									.remarks("主键")
									.build();
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableColumn;
	}


}
