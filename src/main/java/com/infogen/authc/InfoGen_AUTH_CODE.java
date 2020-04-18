package com.infogen.authc;

/**
 * infogen框架的返回值错误码
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年4月2日 下午12:34:23
 * @since 1.0
 * @version 1.0
 */
public enum InfoGen_AUTH_CODE {
	fail_auth(1000, "认证失败"), // 没有 Token
	fail_session(1001, "Session 过期"), //
	fail_roles(1002, "授权失败"); // T

	public Integer code;
	public String message;

	private InfoGen_AUTH_CODE(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
