package com.infogen.authc;


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

import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Authc;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Main;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Users;
import com.infogen.authc.exception.impl.Session_Lose_Exception;
import com.infogen.authc.subject.Subject;
import com.infogen.authc.subject.dao.Local_Subject_DAO;
import com.infogen.authc.subject.dao.Subject_DAO;
import com.infogen.core.tools.Tool_Core;

/**
 * API认证框架的session本地缓存工具类,可以保存和获取subject
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月4日 下午2:11:06
 * @since 1.0
 * @version 1.0
 */
public class InfoGen_Authc {
	private static final Logger LOGGER = LogManager.getLogger(InfoGen_Authc.class.getName());
	private static class InnerInstance {
		public static final InfoGen_Authc instance = new InfoGen_Authc();
	}

	public static InfoGen_Authc getInstance() {
		return InnerInstance.instance;
	}

	private InfoGen_Authc() {
	}
	
	private static final ThreadLocal<Subject> thread_local_subject = new ThreadLocal<>();
	private static Subject_DAO subject_dao = new Local_Subject_DAO();
	
	public static Subject read(String x_access_token) throws Session_Lose_Exception {
		return subject_dao.read(x_access_token);
	}

	public static Subject get() {
		Subject subject = thread_local_subject.get();
		if (subject == null) {
			LOGGER.warn("没有找到当前线程存储的subject,检查是否有存入,或当前代码是否是在新创建的线程里执行的");
		}
		return subject;
	}

	public static void create(Subject subject) {
		subject_dao.create(subject);
		thread_local_subject.set(subject);
	}
	
	public static void update(Subject subject) throws Session_Lose_Exception {
		subject_dao.update(subject);
		thread_local_subject.set(subject);
	}
	
	public final static ZoneId zoneid = ZoneId.of("GMT+08:00");
	public final static Charset charset = StandardCharsets.UTF_8;
	
	private final Authc_Properties_Handle properties_main = new Authc_Properties_Handle_Main();
	private final Authc_Properties_Handle properties_authc = new Authc_Properties_Handle_Authc();
	private final Authc_Properties_Handle properties_users = new Authc_Properties_Handle_Users();
	public void authc(Path authc_path, Subject_DAO subject_dao) throws IOException {
		authc(authc_path);
		InfoGen_Authc.subject_dao = subject_dao;
		LOGGER.info("初始化权限配置");
	}
	public void authc(Path authc_path) throws IOException {
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
