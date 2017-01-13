/**
 * 
 */
package com.infogen.authc.configuration.resource.impl;

import com.infogen.authc.configuration.resource.Resource;

/**
 * @author larry
 * @email   larrylv@outlook.com
 * @version 创建时间 2017年1月9日 下午6:16:24
 */
public class Resource_Equal extends Resource{
	@Override
	public Boolean has(String requestURI) {
		return requestURI.equals(uri);
	}
}
