package com.infogen.http;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.http.ServletContainerInitializer.WebApplicationInitializer;

/**
 * 用于启动mvc框架的监听器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年11月20日 下午6:51:20
 * @since 1.0
 * @version 1.0
 */
public class InfoGenAuthcApplicationInitializer implements WebApplicationInitializer {
	private static final Logger LOGGER = LogManager.getLogger(InfoGenAuthcApplicationInitializer.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet .ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		LOGGER.info("initialized for Application - JAR AUTHC");
		FilterRegistration.Dynamic filter = servletContext.addFilter("InfoGen_HTTP_Authc_Filter", InfoGen_HTTP_Authc_Filter.class);
		filter.addMappingForUrlPatterns(null, true, "/*");
		filter.setAsyncSupported(true);
	}
}
