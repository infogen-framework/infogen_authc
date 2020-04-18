package com.infogen.authc.subject;

import java.io.Serializable;
import java.time.Clock;
import java.util.UUID;

import com.infogen.authc.InfoGen_Session;
import com.infogen.authc.exception.impl.Fail_Roles_Exception;

/**
 * 安全认证实例
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午3:14:24
 * @since 1.0
 * @version 1.0
 */
public class Subject implements Serializable {
	private static final long serialVersionUID = 162572115555027765L;
	private String sid;
	private String uid;
	private String[] roles;// 用户具有的角色
	private Object cache;
	private Long ctime = Clock.system(InfoGen_Session.zoneid).millis();

	public Subject(String user_id, String[] roles) {
		this.sid = UUID.randomUUID().toString().replaceAll("-", "");
		this.uid = user_id;
		this.roles = roles;
	}

	public Subject(String session_id, String user_id, String[] roles) {
		this.sid = session_id;
		this.uid = user_id;
		this.roles = roles;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 授权
	public Boolean verifyRole(String[] resource_roles) throws Fail_Roles_Exception {
		if (resource_roles == null || resource_roles.length == 0) {
			return true;
		}
		if (this.roles == null) {
			return false;
		}
		for (String role : this.roles) {
			for (String resource_role : resource_roles) {
				if (resource_role.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean verifyExpire() {
		return (ctime + InfoGen_Session.session_expire_millis) > Clock.system(InfoGen_Session.zoneid).millis();
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public Object getCache() {
		return cache;
	}

	public void setCache(Object cache) {
		this.cache = cache;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

}
