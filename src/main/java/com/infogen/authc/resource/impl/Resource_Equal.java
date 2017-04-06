/**
 * 
 */
package com.infogen.authc.resource.impl;

import com.infogen.authc.resource.Resource;
/**
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月4日 下午2:11:06
 * @since 1.0
 * @version 1.0
 */
public class Resource_Equal extends Resource{
	@Override
	public Boolean has(String requestURI) {
		return requestURI.equals(uri);
	}
}
