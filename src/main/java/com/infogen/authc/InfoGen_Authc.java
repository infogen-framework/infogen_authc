package com.infogen.authc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infogen.authc.subject.Subject;
import com.infogen.authc.subject.dao.Local_Subject_DAO;
import com.infogen.authc.subject.dao.Subject_DAO;

/**
 * API认证框架的session本地缓存工具类,可以保存和获取subject
 * 
 * @author larry/larrylv@outlook.com/创建时间 2015年5月4日 下午2:11:06
 * @since 1.0
 * @version 1.0
 */
public class InfoGen_Authc {
	private static final Logger LOGGER = LogManager.getLogger(InfoGen_Authc.class.getName());
	private static final ThreadLocal<Subject> thread_local_subject = new ThreadLocal<>();
	public static Subject_DAO subject_dao = new Local_Subject_DAO();

	public static Subject load(String x_access_token) {
		return subject_dao.get(x_access_token);
	}

	public static Subject get() {
		Subject subject = thread_local_subject.get();
		if (subject == null) {
			LOGGER.warn("没有找到当前线程存储的subject,检查是否有存入,或当前代码是否是在新创建的线程里执行的");
		}
		return subject;
	}

	public static void set(Subject subject) {
		subject_dao.save(subject);
		thread_local_subject.set(subject);
	}
}
