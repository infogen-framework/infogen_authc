package com.infogen.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.InfoGen_Session;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Authc;
import com.infogen.authc.configuration.handle.impl.Authc_Properties_Handle_Main;
import com.infogen.authc.exception.InfoGen_Auth_Exception;
import com.infogen.authc.exception.impl.Authentication_Fail_Exception;
import com.infogen.authc.exception.impl.Roles_Fail_Exception;
import com.infogen.authc.exception.impl.Session_Expiration_Exception;
import com.infogen.authc.resource.Resource;
import com.infogen.authc.subject.Subject;
import com.infogen.core.json.JSONObject;

/**
 * HTTP接口的API认证的处理器,可以通过ini配置注入使用的session管理器
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年11月20日 下午6:59:38
 * @since 1.0
 * @version 1.0
 */
public class InfoGen_HTTP_Authc_Handle {
	private static final Logger LOGGER = LogManager.getLogger(InfoGen_HTTP_Authc_Handle.class.getName());

	// 初始化配置时赋值
	public static final List<Resource> urls_rules = Authc_Properties_Handle_Authc.urls_rules;
	public static String signin = Authc_Properties_Handle_Main.signin;

	public Resource has(String requestURI) {
		for (Resource operator : urls_rules) {
			if (operator.has(requestURI)) {
				return operator;
			}
		}
		return null;
	}

	public String get_ip(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// js 前端页面加载时判断是否有 x-access-token 没有跳转到登录页面
	// ajax 调用后判断如果为没有权限执行登录操作
	// 只有存在 x-access-token 并通过有效期验证的才生成用于验证权限的subject
	public Boolean doFilter(Subject subject, String requestURI, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// request.getRealPath("/")); F:\Tomcat 6.0\webapps\news\test
		// System.out.println(request.getRequestURL()); //
		// http://localhost:8080/news/main/list.jsp
		// System.out.println(request.getContextPath()); // /news
		// System.out.println(request.getServletPath()); // /main/list.jsp
		// 配置spring mvc 的<mvc:default-servlet-handler />后始终为空
		// System.out.println(request.getRequestURI()); // /news/main/list.jsp

		Resource operator = has(requestURI);
		try {
			// 该方法不需要任何角色验证直接返回认证成功
			if (operator == null || operator.anon()) {
				return true;
			}
			// 需要验证的角色
			String[] roles = operator.roles;
			// 认证
			if (subject == null) {
				throw new Authentication_Fail_Exception();
			} else if (subject.verifyIssued_at()) {
				InfoGen_Session.delete(subject.getX_access_token());
				throw new Session_Expiration_Exception();
			} else if (subject.verifyRole(roles)) {
				throw new Roles_Fail_Exception();
			} else {
				// Authentication Success
			}
		} catch (InfoGen_Auth_Exception e) {
			LOGGER.info("认证失败:".concat(requestURI) + " from " + get_ip(request));
			if (operator.isRedirect()) {
				response.sendRedirect(signin.concat("?code=" + e.code()));
			} else {
				response.getWriter().write(JSONObject.create("code", e.code().toString()).put("message", e.message()).toJson("{}"));
			}
			return false;
		}
		return true;
	}

}