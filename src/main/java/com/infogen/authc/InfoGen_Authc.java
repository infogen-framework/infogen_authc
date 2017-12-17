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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Authc;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Main;
import com.infogen.authc.subject.Subject;
import com.infogen.authc.subject.dao.Local_Subject_DAO;
import com.infogen.authc.subject.dao.Subject_DAO;
import com.infogen.core.tools.Tool_String;
import com.infogen.core.util.NativePath;

/**
 * API认证框架的session本地缓存工具类,可以保存和获取subject
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月4日 下午2:11:06
 * @since 1.0
 * @version 1.0
 */
public class InfoGen_Authc {
	private static final Logger LOGGER = LogManager.getLogger(InfoGen_Authc.class.getName());
	public static final String X_ACCESS_TOKEN = "X-Access-Token";
	public final static ZoneId zoneid = ZoneId.of("GMT+08:00");
	public final static Charset charset = StandardCharsets.UTF_8;

	private static final Authc_Properties_Handle properties_main = new Authc_Properties_Handle_Main();
	private static final Authc_Properties_Handle properties_authc = new Authc_Properties_Handle_Authc();

	public static void init(String authc_path, Subject_DAO subject_dao) throws IOException {
		init(NativePath.get(authc_path));
		InfoGen_Authc.subject_dao = subject_dao;
	}

	public static void init(Path authc_path, Subject_DAO subject_dao) throws IOException {
		init(authc_path);
		InfoGen_Authc.subject_dao = subject_dao;
	}

	public static void init(String authc_path) throws IOException {
		init(NativePath.get(authc_path));
	}

	public static void init(Path authc_path) throws IOException {
		try (InputStream resourceAsStream = Files.newInputStream(authc_path, StandardOpenOption.READ); //
				InputStreamReader inputstreamreader = new InputStreamReader(resourceAsStream, charset); //
				BufferedReader reader = new BufferedReader(inputstreamreader)) {
			Authc_Properties_Handle properties_current = null;
			String line;
			while ((line = reader.readLine()) != null) {
				line = Tool_String.trim(line);
				if (line.startsWith("#")) {
					continue;
				} else if (line.equals("[main]")) {
					properties_current = properties_main;
				} else if (line.equals("[authc]")) {
					properties_current = properties_authc;
				} else if (line != null && !line.isEmpty()) {
					properties_current.handle(line);
				} else {

				}
			}
			Collections.reverse(Authc_Properties_Handle_Authc.urls_rules);
			LOGGER.info("初始化 InfoGen_Authc 成功");
		}
	}

	private static Integer remember_timeout = 60 * 60 * 24 * 7;
	private static final ThreadLocal<Subject> thread_local_subject = new ThreadLocal<>();
	public static final ThreadLocal<HttpServletRequest> thread_local_request = new ThreadLocal<>();
	public static final ThreadLocal<HttpServletResponse> thread_local_response = new ThreadLocal<>();
	private static Subject_DAO subject_dao = new Local_Subject_DAO();

	public static void create(Subject subject) {
		thread_local_subject.set(subject);
		if (subject.getRemember()) {
			set_cookie(X_ACCESS_TOKEN, subject.getX_access_token(), remember_timeout);
		} else {
			set_cookie(X_ACCESS_TOKEN, subject.getX_access_token(), -1);
		}
		subject_dao.create(subject);
	}

	public static Subject read(String subject_name) {
		Subject subject = subject_dao.read(subject_name);
		thread_local_subject.set(subject);
		return subject;
	}

	public static Subject read() {
		Subject subject = thread_local_subject.get();
		return subject;
	}

	public static void delete(String subject_name) {
		thread_local_subject.remove();
		subject_dao.delete(subject_name);
	}

	public static void update(Subject subject) {
		thread_local_subject.set(subject);
		subject_dao.update(subject);
	}

	private static void set_cookie(String x_access_token, String value, Integer max_age) {
		HttpServletResponse response = thread_local_response.get();
		if (response != null) {
			Cookie cookie = new Cookie(x_access_token, value);
			cookie.setPath("/");
			cookie.setMaxAge(max_age);
			// cookie.setSecure(true);
			// cookie.setHttpOnly(true);
			response.addCookie(cookie);
		}
	}

}
