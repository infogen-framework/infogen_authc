/**
 * 
 */
package com.infogen.authc.configuration.comparison.impl;

import com.infogen.authc.configuration.comparison.Comparison;

/**
 * @author larry
 * @email larrylv@outlook.com
 * @version 创建时间 2017年1月9日 下午6:19:15
 */
public class Comparison_End extends Comparison {

	/* (non-Javadoc)
	 * @see com.infogen.authc.configuration.comparison.Comparison#has(java.lang.String)
	 */
	@Override
	public Boolean has(String requestURI) {
		return requestURI.endsWith(key);
	}

}
