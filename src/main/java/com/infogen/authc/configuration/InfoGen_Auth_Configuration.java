package com.infogen.authc.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.InfoGen_Authc;
import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Authc;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Main;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Users;
import com.infogen.authc.subject.dao.Subject_DAO;
import com.infogen.core.tools.Tool_Core;

/**
 * 读取安全框架ini配置中的各个部分并分给对应的解析器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年6月19日 上午11:49:48
 * @since 1.0
 * @version 1.0
 */
public class InfoGen_Auth_Configuration {
	private static final Logger LOGGER = LogManager.getLogger(InfoGen_Auth_Configuration.class.getName());
	public final static ZoneId zoneid = ZoneId.of("GMT+08:00");
	public final static Charset charset = StandardCharsets.UTF_8;

	private static class InnerInstance {
		public static final InfoGen_Auth_Configuration instance = new InfoGen_Auth_Configuration();
	}

	public static InfoGen_Auth_Configuration getInstance() {
		return InnerInstance.instance;
	}

	private InfoGen_Auth_Configuration() {
	}

	private final Authc_Properties_Handle properties_main = new Authc_Properties_Handle_Main();
	private final Authc_Properties_Handle properties_authc = new Authc_Properties_Handle_Authc();
	private final Authc_Properties_Handle properties_users = new Authc_Properties_Handle_Users();

	public void authc(Path authc_path) throws IOException {
		load_configuration(authc_path);
		LOGGER.info("初始化权限配置");
	}

	public void authc(Path authc_path, Subject_DAO subject_dao) throws IOException {
		load_configuration(authc_path);
		InfoGen_Authc.subject_dao = subject_dao;
		LOGGER.info("初始化权限配置");
	}

	private void load_configuration(Path authc_path) throws IOException {
		try (InputStream resourceAsStream = Files.newInputStream(authc_path, StandardOpenOption.READ); //
				InputStreamReader inputstreamreader = new InputStreamReader(resourceAsStream, charset); //
				BufferedReader reader = new BufferedReader(inputstreamreader)) {
			Authc_Properties_Handle properties_current = null;
			String line;
			while ((line = reader.readLine()) != null) {
				line = Tool_Core.trim(line);
				if (line.startsWith("#")) {
					continue;
				} else if (line.equals("[main]")) {
					properties_current = properties_main;
				} else if (line.equals("[users]")) {
					properties_current = properties_users;
				} else if (line.equals("[authc]")) {
					properties_current = properties_authc;
				} else if (line != null && !line.isEmpty()) {
					properties_current.handle(line);
				} else {

				}
			}
			Collections.reverse(Authc_Properties_Handle_Authc.urls_rules);
		}
	}

}
