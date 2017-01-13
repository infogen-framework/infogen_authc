/**
 * 
 */
package com.infogen.authc.configuration.resource.impl;

import com.infogen.authc.configuration.resource.Resource;

/**
 * @author larry
 * @email larrylv@outlook.com
 * @version 创建时间 2017年1月9日 下午6:19:15
 */
public class Resource_End extends Resource {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.configuration.comparison.Comparison#has(java.lang.String)
	 */
	@Override
	public Boolean has(String requestURI) {
		return requestURI.endsWith(uri);
	}
}
