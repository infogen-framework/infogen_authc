package com.infogen.authc.subject.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.subject.Subject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于redis的session管理器实现
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月13日 下午5:06:34
 * @since 1.0
 * @version 1.0
 */
public class Redis_Subject_DAO extends Subject_DAO {
	private static final Logger LOGGER = LogManager.getLogger(Redis_Subject_DAO.class.getName());
	private JedisPool pool = null;

	public Redis_Subject_DAO(JedisPool pool) {
		super();
		this.pool = pool;
	}

	public Jedis take() {
		if (pool == null) {
			LOGGER.error("Redis 没有初始化");
			return null;
		}
		return pool.getResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.cache.Subject_DAO#save(com.infogen.authc.subject.Subject)
	 */
	private Integer expire = 60 * 60 * 12;

	@Override
	public void save(Subject subject) {
		Jedis take = take();
		Map<String, String> map = new HashMap<>();
		map.put("audience", subject.getAudience());
		map.put("issued_at", subject.getIssued_at().toString());
		map.put("last_access_time", subject.getLast_access_time().toString());
		map.put("remember", subject.getRemember().toString());
		map.put("roles", subject.getRoles());
		try {
			String key = subject.getSubject();
			take.hmset(key, map);
			take.expire(key, expire);
		} finally {
			take.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.cache.Subject_DAO#get(java.lang.String)
	 */
	@Override
	public Subject get(String subject_name) {
		Jedis take = take();
		try {
			Map<String, String> hgetAll = take.hgetAll(subject_name);
			if (hgetAll != null) {
				Subject subject = new Subject(subject_name, hgetAll.get("audience"), Boolean.valueOf(hgetAll.get("remember")), hgetAll.get("roles"));
				subject.setIssued_at(Long.valueOf(hgetAll.get("issued_at")));
				subject.setLast_access_time(Long.valueOf(hgetAll.get("last_access_time")));
				subject.setSubject(subject_name);
				return subject;
			}
		} finally {
			take.close();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infogen.authc.cache.Subject_DAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String subject_name) {
		Jedis take = take();
		try {
			take.hdel(subject_name);
		} finally {
			take.close();
		}
	}

}
