package org.woodwhales.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarkDowndTableInfoVO {

	private boolean success;
	private String mdString;
	private String databaseProductVersion;
	private String errorMsg;
	
	public static MarkDowndTableInfoVO success(String databaseProductVersion, String mdString) {
		return MarkDowndTableInfoVO.builder().success(true).databaseProductVersion(databaseProductVersion).mdString(mdString).build();
	}
	
	public static MarkDowndTableInfoVO error(String databaseProductVersion, String errorMsg) {
		return MarkDowndTableInfoVO.builder().success(false).databaseProductVersion(databaseProductVersion).errorMsg(errorMsg).build();
	}
	
}
