package com.infogen.authc.subject;

import java.io.Serializable;
import java.time.Clock;
import java.util.UUID;

import com.infogen.authc.InfoGen_Session;
import com.infogen.authc.exception.impl.Roles_Fail_Exception;

/**
 * 安全认证实例
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午3:14:24
 * @since 1.0
 * @version 1.0
 */
public class Subject implements Serializable {
	private static final long serialVersionUID = 162572115555027765L;
	private String x_access_token;
	private String subject;
	private String[] roles;// 用户具有的角色
	private Boolean guest = false;// 是否临时用户
	private Boolean remember_me = true;
	private Long issued_at = Clock.system(InfoGen_Session.zoneid).millis();

	private Object cache;

	public Subject(String subject, String[] roles) {
		this.x_access_token = UUID.randomUUID().toString().replaceAll("-", "");
		this.subject = subject;
		this.roles = roles;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 授权
	public Boolean verifyRole(String[] resource_roles) throws Roles_Fail_Exception {
		if (resource_roles == null || resource_roles.length == 0) {
			return true;
		}
		if (this.roles == null) {
			return false;
		}
		for (String resource_role : resource_roles) {
			for (String role : this.roles) {
				if (resource_role.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean verifyIssued_at() {
		Long now_millis = Clock.system(InfoGen_Session.zoneid).millis();
		return (issued_at + InfoGen_Session.session_expire_second * 1000) > now_millis;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(Long issued_at) {
		this.issued_at = issued_at;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
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

	public Object getCache() {
		return cache;
	}

	public void setCache(Object cache) {
		this.cache = cache;
	}

	public Boolean getRemember_me() {
		return remember_me;
	}

	public void setRemember_me(Boolean remember_me) {
		this.remember_me = remember_me;
	}

}
