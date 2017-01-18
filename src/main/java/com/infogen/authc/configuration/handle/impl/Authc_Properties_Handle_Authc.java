package com.infogen.authc.configuration.handle.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.authc.resource.Resource;
import com.infogen.authc.resource.Resource.Operation;
import com.infogen.authc.resource.Resource.Type;
import com.infogen.authc.resource.impl.Resource_End;
import com.infogen.authc.resource.impl.Resource_Equal;
import com.infogen.authc.resource.impl.Resource_Start;
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

	public static final List<Resource> urls_rules = new LinkedList<>();

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
		String resource = Tool_String.trim(split[0]);
		String value = Tool_String.trim(split[1]);

		Resource comparison = null;

		if (resource.endsWith("*")) {
			resource = resource.substring(0, resource.length() - 1);
			comparison = new Resource_Start();
		} else if (resource.startsWith("*")) {
			resource = resource.substring(1, resource.length());
			comparison = new Resource_End();
		} else {
			comparison = new Resource_Equal();
		}
		if (resource.contains("*")) {
			LOGGER.error("[authc] url格式错误 eg:/a/b  或 /a/* 或 *.html:".concat(line));
			return;
		}
		comparison.uri = resource;

		// authc
		String[] value_split = value.split(",");
		if (value_split[0].trim().equals("authc")) {
			if (value_split.length != 3) {
				LOGGER.error("[authc] url格式错误 eg:/* = authc,redirect|api, roles[role1,role2] :".concat(line));
				return;
			}
			comparison.operation = Operation.AUTHC.name();

			if (value_split[1].equals("redirect")) {
				comparison.type = Type.REDIRECT.name();
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
