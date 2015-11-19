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

	public String key = "";
	public Matching match = Matching.EQUAL;
	public String[] roles = new String[] {};

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
