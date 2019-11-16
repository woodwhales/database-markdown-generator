package org.woodwhales.model.md;

import org.apache.commons.lang3.StringUtils;
import org.woodwhales.model.db.SchemaInfo;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class MarkDowndTableInfo extends SimpleMDTableInfoExcutor {
	private String header;

	private String headerLine;

	private SchemaInfo schemaInfo;

	public String genMdTableString() {
		if (StringUtils.isBlank(header) || StringUtils.isBlank(headerLine)) {
			return StringUtils.EMPTY;
		}

		return genMdTableList(schemaInfo);
	}

	public MarkDowndTableInfo(SchemaInfo schemaInfo) {
		this.header = genHeader();
		this.headerLine = genHeaderLine();
		this.schemaInfo = schemaInfo;
	}

	public MarkDowndTableInfo(SchemaInfo schemaInfo, String header, String headerLine) {
		this.header = genHeader(header);
		this.headerLine = genHeaderLine(headerLine);
		this.schemaInfo = schemaInfo;
	}

}
