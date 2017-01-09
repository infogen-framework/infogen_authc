/**
 * 
 */
package com.infogen.authc.configuration.comparison.impl;

import com.infogen.authc.configuration.comparison.Comparison;

/**
 * @author larry
 * @email   larrylv@outlook.com
 * @version 创建时间 2017年1月9日 下午6:16:24
 */
public class Comparison_Equal extends Comparison{
	@Override
	public Boolean has(String requestURI) {
		return requestURI.equals(key);
	}
}
