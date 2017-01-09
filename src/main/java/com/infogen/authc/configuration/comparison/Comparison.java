package com.infogen.authc.configuration.comparison;

/**
 * @author larry/larrylv@outlook.com/创建时间 2015年11月19日 上午11:48:18
 * @since 1.0
 * @version 1.0
 */
public abstract class Comparison {

	public enum Type {
		API, REDIRECT
	}

	public enum Operation {
		ANON, AUTHC
	}

	public String key = "";
	public String[] roles = new String[] {};
	public Operation operation = Operation.ANON;
	public Type type = Type.API;

	public abstract Boolean has(String requestURI);
	public Boolean anon() {
		return operation == Operation.ANON ? true : false;
	}

	public Boolean isRedirect() {
		return type == Type.REDIRECT;
	}
}
