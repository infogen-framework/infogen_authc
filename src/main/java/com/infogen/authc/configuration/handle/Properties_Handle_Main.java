package com.infogen.authc.configuration.handle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 解析安全框架ini配置中[main] 基本配置的部分
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年6月19日 下午12:47:45
 * @since 1.0
 * @version 1.0
 */
public class Properties_Handle_Main extends Properties_Handle {
	private static final Logger LOGGER = LogManager.getLogger(Properties_Handle_Main.class.getName());
	public static String login = "/";

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
		String key = split[0].trim();
		String value = split[1].trim();

		if (key.equals("login")) {
			Properties_Handle_Main.login = value;
		}
	}
}
