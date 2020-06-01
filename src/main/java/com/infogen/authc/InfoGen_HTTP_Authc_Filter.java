package com.infogen.authc;

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

import com.infogen.authc.subject.Subject;

/**
 * HTTP方式的安全验证框架的过滤器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年3月27日 下午4:09:09
 * @since 1.0
 * @version 1.0
 */
// 注解不支持排序 所以在 WebApplicationInitializer 中加载
public class InfoGen_HTTP_Authc_Filter implements Filter {
	private InfoGen_HTTP_Authc_Handle authc = new InfoGen_HTTP_Authc_Handle();

	public void doFilter(ServletRequest srequset, ServletResponse sresponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequset;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		// 是否是 Servlet 的跨域保护 preflighted request。
		// preflighted request在发送真正的请求前, 会先发送一个方法为OPTIONS的预请求(preflight request), 用于试探服务端是否能接受真正的请求，如果options获得的回应是拒绝性质的，比如404\403\500等http状态，就会停止post、put等请求的发出
		if (request.getMethod().equals("OPTIONS")) {
			filterChain.doFilter(request, response);
			return;
		}

		InfoGen_Session.set_request(request);
		InfoGen_Session.set_response(response);

		// requestURI
		String request_uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (request_uri.startsWith(contextPath)) {
			request_uri = request_uri.substring(contextPath.length());
		}

		// subject
		String x_access_token = request.getHeader(InfoGen_Session.X_ACCESS_TOKEN);
		if (x_access_token == null || x_access_token.trim().isEmpty()) {
			x_access_token = getCookieByName(request, InfoGen_Session.X_ACCESS_TOKEN);
		}
		Subject subject = null;
		if (x_access_token == null || x_access_token.trim().isEmpty()) {
		} else {
			subject = InfoGen_Session.load(x_access_token);// load 后缓存在本地一份
		}

		//
		if (authc.doFilter(subject, request_uri, request, response)) {
			try {
				filterChain.doFilter(request, response);
			} finally {
				InfoGen_Session.clean_thread_local();// 执行完后清除本地缓存的 subject
			}
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