package com.andre.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

public class DBScriptRunner {

	private static final Logger log = Logger.getLogger(DBScriptRunner.class);

	private DBScriptRunner() {
		super();
	}

	public static void executeMultipleScript(List<String> dataCreationScript) throws IOException, SQLException {
		for (String path : dataCreationScript) {
			executeScript(path);
		}
	}

	public static void executeScript(String scriptFilePath) throws IOException, SQLException {
		try (Statement statement = ConnectionUtil.getConnection().createStatement();
				BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath));) {
			String line = null;
			String script = "";
			while ((line = reader.readLine()) != null) {
				script +=line+" ";
			}
			statement.execute(script);
		} catch (Exception e) {
			log.error(Constants.EXCEPTION, e);
		}
	}
}