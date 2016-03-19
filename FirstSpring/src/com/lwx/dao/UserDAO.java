package com.lwx.dao;

import com.lwx.model.User;

/**
 * 抽象类，负责数据库的访问。
 * @author liuxinwei
 *
 */
public interface UserDAO {

	public void save(User user);
}
