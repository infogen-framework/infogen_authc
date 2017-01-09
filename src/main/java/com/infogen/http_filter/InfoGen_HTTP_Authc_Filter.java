package com.infogen.http_filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogen.authc.InfoGen_Authc;
import com.infogen.authc.subject.Subject;

/**
 * HTTP方式的安全验证框架的过滤器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年3月27日 下午4:09:09
 * @since 1.0
 * @version 1.0
 */
@WebFilter(filterName = "InfoGen_HTTP_Authc_Filter", urlPatterns = { "/*" }, asyncSupported = true)
public class InfoGen_HTTP_Authc_Filter implements Filter {
	private InfoGen_HTTP_Authc_Handle authc = new InfoGen_HTTP_Authc_Handle();
	private Integer remember_timeout = 60 * 60 * 24 * 7;

	public void doFilter(ServletRequest srequset, ServletResponse sresponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequset;
		HttpServletResponse response = (HttpServletResponse) sresponse;

		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (requestURI.startsWith(contextPath)) {
			requestURI = requestURI.substring(contextPath.length());
		}
		String x_access_token = getCookieByName(request, InfoGen_Authc.X_ACCESS_TOKEN);
		if (!authc.doFilter(requestURI, x_access_token, response)) {
			return;
		}
		filterChain.doFilter(srequset, sresponse);

		Subject subject = InfoGen_Authc.get();
		if (subject != null) {
			Integer time = subject.getRemember() ? remember_timeout : 0;
			setCookie(request, response, InfoGen_Authc.X_ACCESS_TOKEN, subject.getX_access_token(), time);
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

	public HttpServletResponse setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer time) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(request.getContextPath());
		try {
			URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cookie.setMaxAge(time);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return response;
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