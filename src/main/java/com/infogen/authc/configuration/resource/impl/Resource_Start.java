/**
 * 
 */
package com.infogen.authc.configuration.resource.impl;

import com.infogen.authc.configuration.resource.Resource;

/**
 * @author larry
 * @email   larrylv@outlook.com
 * @version 创建时间 2017年1月9日 下午6:18:17
 */
public class Resource_Start extends Resource {

	/* (non-Javadoc)
	 * @see com.infogen.authc.configuration.comparison.Comparison#has(java.lang.String)
	 */
	@Override
	public Boolean has(String requestURI) {
		return requestURI.startsWith(uri);
	}

}
