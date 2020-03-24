package com.infogen.authc.configuration.handle;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.configuration.handle.url_pattern.UrlPattern;
import com.infogen.authc.configuration.handle.url_pattern.UrlPattern_End;
import com.infogen.authc.configuration.handle.url_pattern.UrlPattern_Equal;
import com.infogen.authc.configuration.handle.url_pattern.UrlPattern_Start;
import com.infogen.authc.configuration.handle.url_pattern.UrlPattern.Operation;
import com.infogen.authc.configuration.handle.url_pattern.UrlPattern.Type;

/**
 * 解析安全框架ini配置中[authc]方法权限配置的部分
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年6月19日 下午12:48:48
 * @since 1.0
 * @version 1.0
 */
public class Properties_Handle_Authc extends Properties_Handle {
	private static final Logger LOGGER = LogManager.getLogger(Properties_Handle_Authc.class.getName());

	public static final List<UrlPattern> urls_rules = new LinkedList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.configuration.handle.properties_handle#handle(java.lang.String)
	 */
	@Override
	public void handle(String line) {
		String[] split = line.split("=");
		if (split.length <= 1) {
			LOGGER.error("格式错误 ".concat(line));
			return;
		}

		String uri = split[0].trim();
		String value = split[1].trim();

		UrlPattern comparison = null;

		if (uri.endsWith("*")) {
			uri = uri.substring(0, uri.length() - 1);
			comparison = new UrlPattern_Start();
		} else if (uri.startsWith("*")) {
			uri = uri.substring(1, uri.length());
			comparison = new UrlPattern_End();
		} else {
			comparison = new UrlPattern_Equal();
		}
		if (uri.contains("*")) {
			LOGGER.error("[authc] url格式错误 eg:/a/b  或 /a/* 或 *.html:".concat(line));
			return;
		}
		comparison.uri = uri;

		// authc
		String[] value_array = value.split(",");
		if (value_array[0].trim().equals("authc")) {
			if (value_array.length != 3) {
				LOGGER.error("[authc] url格式错误 eg:/* = authc,redirect|api, roles[role1,role2] :".concat(line));
				return;
			}
			comparison.operation = Operation.AUTHC.name();

			if (value_array[1].equals("redirect")) {
				comparison.type = Type.REDIRECT.name();
			} else {
				comparison.type = Type.API.name();
			}

			String roles = value.substring(value.indexOf("roles["));
			roles = roles.replace("roles[", "");
			roles = roles.replace("]", "");
			if (roles.trim().isEmpty()) {
				comparison.roles = new String[] {};
			} else {
				comparison.roles = roles.split(",");
			}
		} else {
			comparison.operation = Operation.ANON.name();
		}

		urls_rules.add(comparison);
	}
}
