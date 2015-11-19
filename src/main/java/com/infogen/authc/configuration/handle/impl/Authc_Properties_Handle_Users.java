package com.infogen.authc.configuration.handle.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.configuration.Configuration_User;
import com.infogen.authc.configuration.handle.Authc_Properties_Handle;
import com.infogen.core.tools.Tool_Core;

/**
 * 解析安全框架ini配置中[main] 基本配置的部分
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年6月19日 下午12:47:45
 * @since 1.0
 * @version 1.0
 */
public class Authc_Properties_Handle_Users extends Authc_Properties_Handle {
	private static final Logger LOGGER = LogManager.getLogger(Authc_Properties_Handle_Users.class.getName());
	public static final List<Configuration_User> users = new LinkedList<>();

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
		String key = Tool_Core.trim(split[0]);
		String value = Tool_Core.trim(split[1]);

		String[] value_split = value.split(",");
		if (value_split.length < 1) {
			LOGGER.error("[authc] user格式错误 eg:account=password,role1,role2:".concat(line));
			return;
		}

		Configuration_User user = new Configuration_User();
		user.account = key;
		user.password = value_split[0];
		String[] roles = new String[value_split.length - 1];
		for (int i = 1; i < value_split.length; i++) {
			roles[i - 1] = value_split[i];
		}
		user.roles = roles;

		users.add(user);
	}
}
