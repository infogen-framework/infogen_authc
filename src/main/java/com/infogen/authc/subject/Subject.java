package com.infogen.authc.subject;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;

import com.infogen.authc.InfoGen_Authc;
import com.infogen.authc.exception.impl.Roles_Fail_Exception;
import com.infogen.authc.exception.impl.Session_Expiration_Exception;

/**
 * 一个安全认证实例
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午3:14:24
 * @since 1.0
 * @version 1.0
 */
public class Subject {
	protected String x_access_token;
	protected String subject;
	/**
	 * 受众ID,客户端类型 eg:android 应用 A/ios 应用 B/web 应用 C
	 */
	protected String audience;
	/**
	 * 是否开启记住我
	 */
	protected Boolean remember;
	/**
	 * 创建时间
	 */
	protected Long issued_at = Clock.system(InfoGen_Authc.zoneid).millis();
	/**
	 * 最后一次通过认证时间
	 */
	protected Long last_access_time = Clock.system(InfoGen_Authc.zoneid).millis();
	/**
	 * 用户具有的角色 使用,分隔 eg:admin,employee
	 */
	protected String roles;
	protected Long session_overtime = 12 * 60 * 60 * 1000l;
	protected Long remember_overtime = 5 * 24 * 60 * 60 * 1000l;

	public Subject(String x_access_token, String subject, String audience, Boolean remember, String roles) {
		this.x_access_token = x_access_token;
		this.subject = subject;
		this.audience = audience;
		this.remember = remember;
		this.roles = roles;
	}

	public Subject(String x_access_token, String subject, String audience, Boolean remember, String[] roles) {
		StringBuilder stringbuilder = new StringBuilder();
		for (int i = 0; i < roles.length; i++) {
			if (i > 0) {
				stringbuilder.append(",");
			}
			stringbuilder.append(roles[i]);
		}
		this.x_access_token = x_access_token;
		this.subject = subject;
		this.audience = audience;
		this.remember = remember;
		this.roles = stringbuilder.toString();
	}
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * 认证
	 */
	public void checkExpiration() throws Session_Expiration_Exception {
		long millis = Clock.system(InfoGen_Authc.zoneid).millis();
		long last_access_time_overtime = millis - last_access_time;
		if (!remember && last_access_time_overtime > session_overtime) {
			throw new Session_Expiration_Exception();
		}
		long issued_at_overtime = millis - issued_at;
		if (remember && issued_at_overtime > remember_overtime && last_access_time_overtime > session_overtime) {
			throw new Session_Expiration_Exception();
		} 
	}

	// 授权
	public void hasRole(String[] roles) throws Roles_Fail_Exception {
		if (roles == null || roles.length == 0) {
			return;
		}
		if (this.roles == null) {
			throw new Roles_Fail_Exception();
		}
		List<String> roles_list = Arrays.asList(this.roles.split(","));
		for (String string : roles) {
			if (roles_list.contains(string)) {
				return;
			}
		}
		throw new Roles_Fail_Exception();
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Boolean getRemember() {
		return remember;
	}

	public void setRemember(Boolean remember) {
		this.remember = remember;
	}

	public Long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(Long issued_at) {
		this.issued_at = issued_at;
	}

	public Long getLast_access_time() {
		return last_access_time;
	}

	public void setLast_access_time(Long last_access_time) {
		this.last_access_time = last_access_time;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getX_access_token() {
		return x_access_token;
	}

	public void setX_access_token(String x_access_token) {
		this.x_access_token = x_access_token;
	}

	/**
	 * @return the session_overtime
	 */
	public Long getSession_overtime() {
		return session_overtime;
	}

	/**
	 * @param session_overtime the session_overtime to set
	 */
	public void setSession_overtime(Long session_overtime) {
		this.session_overtime = session_overtime;
	}

	/**
	 * @return the remember_overtime
	 */
	public Long getRemember_overtime() {
		return remember_overtime;
	}

	/**
	 * @param remember_overtime the remember_overtime to set
	 */
	public void setRemember_overtime(Long remember_overtime) {
		this.remember_overtime = remember_overtime;
	}

}
