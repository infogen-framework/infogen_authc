package com.infogen.authc.configuration.handle.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.configuration.Comparison;
import com.infogen.authc.configuration.Comparison.Matching;
import com.infogen.authc.configuration.Comparison.Operation;
import com.infogen.authc.configuration.Comparison.Type;
import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.core.tools.Tool_String;

/**
 * 解析安全框架ini配置中[authc]方法权限配置的部分
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年6月19日 下午12:48:48
 * @since 1.0
 * @version 1.0
 */
public class Authc_Properties_Handle_Authc extends Authc_Properties_Handle {
	private static final Logger LOGGER = LogManager.getLogger(Authc_Properties_Handle_Authc.class.getName());

	public static final List<Comparison> urls_rules = new LinkedList<>();

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
		String key = Tool_String.trim(split[0]);
		String value = Tool_String.trim(split[1]);

		Comparison comparison = new Comparison();

		if (key.endsWith("*")) {
			key = key.substring(0, key.length() - 1);
			comparison.match = Matching.STARTSWITH;
		} else if (key.startsWith("*")) {
			key = key.substring(1, key.length());
			comparison.match = Matching.ENDSWITH;
		} else {
			comparison.match = Matching.EQUAL;
		}
		if (key.contains("*")) {
			LOGGER.error("[authc] url格式错误 eg:/a/b  或 /a/* 或 *.html:".concat(line));
			return;
		}
		comparison.key = key;

		// authc
		String[] value_split = value.split(",");
		if (value_split[0].trim().equals("authc")) {
			if (value_split.length != 3) {
				LOGGER.error("[authc] url格式错误 eg:/* = authc,redirect|api, roles[role1,role2] :".concat(line));
				return;
			}
			comparison.operation = Operation.AUTHC;

			if (value_split[1].equals("redirect")) {
				comparison.type = Type.REDIRECT;
			}

			String roles = value.substring(value.indexOf("roles["));
			roles = roles.replace("roles[", "");
			roles = roles.replace("]", "");
			roles = Tool_String.trim(roles);
			comparison.roles = roles.split(",");
		}

		urls_rules.add(comparison);
	}
}
