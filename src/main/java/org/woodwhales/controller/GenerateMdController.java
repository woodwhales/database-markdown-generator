package org.woodwhales.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.woodwhales.controller.model.DataBaseConfigRequestBody;
import org.woodwhales.controller.model.MarkDowndTableInfoVO;
import org.woodwhales.model.db.SchemaInfo;
import org.woodwhales.model.md.MarkDowndTableInfo;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class GenerateMdController {

	@GetMapping
	public String index() {
		return "index";
	}
	
	@ResponseBody
	@PostMapping("/generate")
	public MarkDowndTableInfoVO generate(@RequestBody @Validated DataBaseConfigRequestBody dataBaseConfigRequestBody, BindingResult bindingResult) {
		log.debug("dataBaseConfigRequestBody = {}", JSON.toJSONString(dataBaseConfigRequestBody));
		if(bindingResult.hasErrors()){
			String errorMsg = bindingResult.getFieldError().getDefaultMessage();
			return MarkDowndTableInfoVO.error(StringUtils.EMPTY, errorMsg);
		}
		
		Connection conn = null;
		String databaseProductVersion = StringUtils.EMPTY;
		try {
			conn = getConnection(dataBaseConfigRequestBody);
			databaseProductVersion = conn.getMetaData().getDatabaseProductVersion();
			
			SchemaInfo schemaInfo = SchemaInfo.newInstance(conn);
			MarkDowndTableInfo markDowndTableInfo = new MarkDowndTableInfo(schemaInfo);
			String mdTableString = markDowndTableInfo.genMdTableString();
			return MarkDowndTableInfoVO.success(databaseProductVersion, mdTableString);
		} catch (Exception e) {
			e.printStackTrace();
			return MarkDowndTableInfoVO.error(databaseProductVersion, e.getMessage());
		} finally {
			if(Objects.nonNull(conn)) {
				try {
					conn.close();
				} catch (SQLException e) {
					return MarkDowndTableInfoVO.error(databaseProductVersion, e.getMessage().toString());
				}
			}
		}
	}

	private Connection getConnection(DataBaseConfigRequestBody dataBaseConfigRequestBody) throws ClassNotFoundException, SQLException {
		return getConnection(dataBaseConfigRequestBody.getDriver(),
				"jdbc:mysql://" + dataBaseConfigRequestBody.getIp() + ":" + dataBaseConfigRequestBody.getPort() + "/"
						+ dataBaseConfigRequestBody.getSchema() + "?useUnicode=yes&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC",
				dataBaseConfigRequestBody.getUserName(),
				dataBaseConfigRequestBody.getPassWord());
	}
	
	private Connection getConnection(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
}
