package com.infogen;

/**
 * infogen框架的返回值错误码
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年4月2日 下午12:34:23
 * @since 1.0
 * @version 1.0
 */
public enum AUTH_CODE {
	authentication_fail(1000, "认证失败"), // 没有 Token
	session_lose(1001, "Session 丢失"), // Token 不存在
	session_expiration(1002, "Session 过期"), //
	roles_fail(1003, "授权失败"); // T
	public String note;
	public Integer code;

	private AUTH_CODE(Integer code, String note) {
		this.note = note;
		this.code = code;
	}
}
