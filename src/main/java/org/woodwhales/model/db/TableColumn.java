package org.woodwhales.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableColumn {
	private String columnName;
	private String columnType;
	private String remarks;
	private int columnSize;
	private String deaultValue;
	private boolean nullAble;
}
