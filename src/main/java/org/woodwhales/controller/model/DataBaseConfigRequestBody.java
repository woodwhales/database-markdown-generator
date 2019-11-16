package org.woodwhales.controller.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DataBaseConfigRequestBody {
	
	@NotBlank(message = "数据库驱动名不允许为空")
	private String driver;
	
	@NotBlank(message = "数据库ip不允许为空")
	private String ip;
	
	@NotNull(message = "数据库端口不允许为空")
	private Integer port;
	
	@NotBlank(message = "数据库名不允许为空")
	private String schema;
	
	@NotBlank(message = "用户名不允许为空")
	private String userName;
	
	@NotBlank(message = "密码不允许为空")
	private String passWord;
	
}
