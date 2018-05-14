package com.infogen.authc.exception.impl;

import com.infogen.InfoGen_AUTH_CODE;
import com.infogen.authc.exception.InfoGen_Auth_Exception;

/**
 * 会话超时
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月15日 下午12:30:39
 * @since 1.0
 * @version 1.0
 */
public class Session_Expiration_Exception extends InfoGen_Auth_Exception {

	private static final long serialVersionUID = -3944897882402426587L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#code()
	 */
	@Override
	public Integer code() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.session_expiration.code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.session_expiration.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#note()
	 */
	@Override
	public String message() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.session_expiration.message;
	}

}
