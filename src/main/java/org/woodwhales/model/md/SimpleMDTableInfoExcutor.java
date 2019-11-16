package org.woodwhales.model.md;

import static org.woodwhales.constants.MDConstant.br;
import static org.woodwhales.constants.MDConstant.separator;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.woodwhales.constants.MDConstant;
import org.woodwhales.model.db.SchemaInfo;
import org.woodwhales.model.db.Tabel;
import org.woodwhales.model.db.TableColumn;

public class SimpleMDTableInfoExcutor implements MDTableInfoExcutor {

	@Override
	public String genHeader() {
		return "|名称  | 数据类型 | 长度 | 允许NULL | 默认值  | 注释    | 备注说明  |";
	}

	public String genHeader(String header) {
		return header;
	}

	@Override
	public String genHeaderLine() {
		return "| ---- | ---- | ---- | ---- | ---- | ---------- | ---- |";
	}

	public String genHeaderLine(String headerLine) {
		return headerLine;
	}

	public String genMdTableList(SchemaInfo schemaInfo) {
		if(Objects.isNull(schemaInfo)) {
			return StringUtils.EMPTY;
		}
		
		StringBuffer bodyStringBuffer = new StringBuffer();
		bodyStringBuffer.append("# " + schemaInfo.getSchemaName() + "数据库表结构设计").append(br);
		bodyStringBuffer.append(br);
		for (Tabel tabelInfo : schemaInfo.getTabelInfoList()) {
			bodyStringBuffer.append("## " + tabelInfo.getName()).append(br)
							.append(br)
							.append("> 表名：" + tabelInfo.getName()).append(br)
							.append(br)
							.append(genMdTable(tabelInfo));
		}

		return bodyStringBuffer.toString();
	}

	public String genMdTable(Tabel tabelInfo) {
		return new StringBuffer().append(genHeader())
									.append(br)
									.append(genHeaderLine())
									.append(br)
									.append(genBody(tabelInfo))
									.append(br).toString();
	}

	@Override
	public String genBody(Tabel table) {
		StringBuffer bodyStringBuffer = new StringBuffer();
		for (TableColumn tableColumn : table.getTableColumns()) {
			bodyStringBuffer.append(genTableBodyLine(tableColumn));
		}

		return bodyStringBuffer.toString();
	}

	private String genTableBodyLine(TableColumn tableColumn) {
		if(Objects.isNull(tableColumn)) {
			return StringUtils.EMPTY;
		}
		StringBuffer bodyStringBuffer = new StringBuffer();
		String centerStr = StringUtils.join(
				new Object[] {
						tableColumn.getColumnName(),
						tableColumn.getColumnType().toLowerCase(),
						tableColumn.getColumnSize(),
						tableColumn.isNullAble() ? "是" : "否",
						tableColumn.getDeaultValue(),
						tableColumn.getRemarks(),
						StringUtils.EMPTY
				},
				MDConstant.separator);
		bodyStringBuffer.append(separator).append(centerStr).append(separator).append(br);
		return bodyStringBuffer.toString();
	}

}
