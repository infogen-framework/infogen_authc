package com.infogen.authc.subject.dao;

import com.infogen.authc.subject.Subject;
import com.infogen.structure.map.LRULinkedHashMap;

/**
 * 默认的基于本地内存的session管理器实现
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午5:06:34
 * @since 1.0
 * @version 1.0
 */
public class Local_Subject_DAO extends Subject_DAO {
	private LRULinkedHashMap<String, Subject> map = new LRULinkedHashMap<>(5000000);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.subject.dao.Subject_DAO#create(com.infogen.authc.subject.Subject)
	 */
	@Override
	public void save(Subject subject) {
		map.put(subject.getSid(), subject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.cache.Subject_DAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String sid) {
		map.remove(sid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.subject.dao.Subject_DAO#read(java.lang.String)
	 */
	@Override
	public Subject get(String sid) {
		return map.get(sid);
	}

}
