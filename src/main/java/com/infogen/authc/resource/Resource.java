package com.infogen.authc.resource;

/**
 * @author larry/larrylv@outlook.com/创建时间 2015年11月19日 上午11:48:18
 * @since 1.0
 * @version 1.0
 */
public abstract class Resource {

	public enum Type {
		API, REDIRECT
	}

	public enum Operation {
		ANON, AUTHC
	}

	public String uri = "";
	public String[] roles = new String[] {};
	public String operation = Operation.ANON.name();
	public String type = Type.API.name();

	public abstract Boolean has(String requestURI);

	public Boolean anon() {
		return operation == Operation.ANON.name() ? true : false;
	}

	public Boolean isRedirect() {
		return type == Type.REDIRECT.name();
	}
}
