package com.infogen.authc.exception.impl;

import com.infogen.authc.InfoGen_AUTH_CODE;
import com.infogen.authc.exception.InfoGen_Auth_Exception;

/**
 * 角色授权失败的错误
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月11日 上午11:44:55
 * @since 1.0
 * @version 1.0
 */
public class Roles_Fail_Exception extends InfoGen_Auth_Exception {
	private static final long serialVersionUID = 153970941852883330L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#code()
	 */
	@Override
	public Integer code() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.fail_roles.code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.fail_roles.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.exception.InfoGen_Auth_Exception#note()
	 */
	@Override
	public String message() {
		// TODO Auto-generated method stub
		return InfoGen_AUTH_CODE.fail_roles.message;
	}

}
