package com.infogen.authc.subject;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.infogen.authc.InfoGen_Session;
import com.infogen.authc.exception.impl.Roles_Fail_Exception;
import com.infogen.core.json.JSONObject;

/**
 * 一个安全认证实例
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午3:14:24
 * @since 1.0
 * @version 1.0
 */
public class Subject extends JSONObject {
	private static final long serialVersionUID = 162572115555027765L;
	private String x_access_token;
	private String subject;
	private Object cache;

	/**
	 * 是否临时用户
	 */
	private Boolean guest;
	/**
	 * 
	 * 是否开启记住我
	 */
	private Boolean remember = true;
	/**
	 * 创建时间
	 */
	private Long issued_at = Clock.system(InfoGen_Session.zoneid).millis();
	/**
	 * 用户具有的角色 使用,分隔 eg:admin,employee
	 */
	private String roles;

	public Subject(String subject, Boolean guest, String roles) {
		this(subject, guest, roles == null ? new String[] {} : roles.split(","));
	}

	public Subject(String subject, Boolean guest, String[] roles) {
		StringBuilder stringbuilder = new StringBuilder();
		for (int i = 0; i < roles.length; i++) {
			if (i > 0) {
				stringbuilder.append(",");
			}
			stringbuilder.append(roles[i]);
		}
		this.x_access_token = UUID.randomUUID().toString().replaceAll("-", "");
		this.subject = subject;
		this.guest = guest;
		this.roles = stringbuilder.toString();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 授权
	public Boolean verifyRole(String[] roles) throws Roles_Fail_Exception {
		if (roles == null || roles.length == 0) {
			return true;
		}
		if (this.roles == null) {
			return false;
		}
		List<String> roles_list = Arrays.asList(this.roles.split(","));
		for (String string : roles) {
			if (roles_list.contains(string)) {
				return true;
			}
		}
		return false;
	}

	public Boolean verifyIssued_at() {
		Long now_millis = Clock.system(InfoGen_Session.zoneid).millis();
		return now_millis - InfoGen_Session.session_expire_millis > issued_at;
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

	public Boolean getGuest() {
		return guest;
	}

	public void setGuest(Boolean guest) {
		this.guest = guest;
	}

	/**
	 * @return the cache
	 */
	public Object getCache() {
		return cache;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	public void setCache(Object cache) {
		this.cache = cache;
	}

}
