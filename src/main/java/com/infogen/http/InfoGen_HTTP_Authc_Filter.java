package com.infogen.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogen.authc.InfoGen_Session;
import com.infogen.authc.subject.Subject;

/**
 * HTTP方式的安全验证框架的过滤器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年3月27日 下午4:09:09
 * @since 1.0
 * @version 1.0
 */
//@WebFilter(filterName = "InfoGen_HTTP_Authc_Filter", urlPatterns = { "/*" }, asyncSupported = true)
public class InfoGen_HTTP_Authc_Filter implements Filter {
	private InfoGen_HTTP_Authc_Handle authc = new InfoGen_HTTP_Authc_Handle();

	public void doFilter(ServletRequest srequset, ServletResponse sresponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequset;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		InfoGen_Session.thread_local_request.set(request);
		InfoGen_Session.thread_local_response.set(response);
		// 验证权限
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (requestURI.startsWith(contextPath)) {
			requestURI = requestURI.substring(contextPath.length());
		}
		//
		String x_access_token = getCookieByName(request, InfoGen_Session.X_ACCESS_TOKEN);
		if (x_access_token == null || x_access_token.trim().isEmpty()) {
			x_access_token = request.getHeader(InfoGen_Session.X_ACCESS_TOKEN);
		}
		Subject subject = null;
		if (x_access_token == null || x_access_token.trim().isEmpty()) {
		} else {
			// load 后缓存在本地一份
			subject = InfoGen_Session.load(x_access_token);
		}
		if (!authc.doFilter(subject, requestURI, request, response)) {
//			return;
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			// 执行完后清除本地缓存的 subject
			InfoGen_Session.delete_local();
		}
	}

	public String getCookieByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}