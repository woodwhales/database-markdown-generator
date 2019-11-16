package org.woodwhales.model.db;

import static org.woodwhales.constants.DBConstant.COLUMN_DEF;
import static org.woodwhales.constants.DBConstant.COLUMN_NAME;
import static org.woodwhales.constants.DBConstant.COLUMN_SIZE;
import static org.woodwhales.constants.DBConstant.NULLABLE;
import static org.woodwhales.constants.DBConstant.REMARKS;
import static org.woodwhales.constants.DBConstant.TYPE_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Tabel {
	private String name;

	private List<TableColumn> tableColumns;

	public static Tabel newInstance(String tableName, ResultSet columnResultSet) {
		if(Objects.isNull(columnResultSet)) {
			return null;
		}

		return Tabel.builder()
					.name(tableName)
					.tableColumns(genTableColumList(columnResultSet))
					.build();
	}

	private static List<TableColumn> genTableColumList(ResultSet columnResultSet) {
		List<TableColumn> tableColumns = new ArrayList<>();
		try {
			while (columnResultSet.next()) {
				TableColumn tableColumn = TableColumn.builder()
													.columnName(columnResultSet.getString(COLUMN_NAME))
													.columnType(columnResultSet.getString(TYPE_NAME))
													.columnSize(columnResultSet.getInt(COLUMN_SIZE))
													.nullAble(getBooleanValue(columnResultSet.getInt(NULLABLE)))
													.deaultValue(getDeaultValue(columnResultSet.getString(COLUMN_DEF)))
													.remarks(columnResultSet.getString(REMARKS))
													.build();

				tableColumns.add(tableColumn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableColumns;
	}

	private static String getDeaultValue(String deaultValueStr) {
		if(StringUtils.isBlank(deaultValueStr) || "0000-00-00 00:00:00".equals(deaultValueStr)) {
			return StringUtils.EMPTY;
		}

		return deaultValueStr;
	}

	private static boolean getBooleanValue(int nullAbleValue) {
		return ((nullAbleValue == 0) ? Boolean.FALSE :Boolean.TRUE).booleanValue();
	}

}
