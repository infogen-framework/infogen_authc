package com.infogen.authc.configuration;

/**
 * @author larry/larrylv@outlook.com/创建时间 2015年11月19日 上午11:48:18
 * @since 1.0
 * @version 1.0
 */
public class Comparison {
	public enum Matching {
		EQUAL, STARTSWITH, ENDSWITH
	}

	public enum Type {
		API, REDIRECT
	}

	public enum Operation {
		ANON, AUTHC
	}

	public String key = "";
	public Matching match = Matching.EQUAL;
	public String[] roles = new String[] {};
	public Operation operation = Operation.ANON;
	public Type type = Type.API;

	public Boolean authc() {
		return operation == Operation.ANON ? true : false;
	}

	public Boolean isRedirect() {
		return type == Type.REDIRECT;
	}

	public Boolean isEqual() {
		return match == Matching.EQUAL;
	}

	public Boolean isStartswith() {
		return match == Matching.STARTSWITH;
	}

	public Boolean isEndsWith() {
		return match == Matching.ENDSWITH;
	}
}
