package com.lwx.service;

import com.lwx.dao.UserDAO;
import com.lwx.dao.impl.UserDAOImpl;
import com.lwx.model.User;

/**
 * 业务逻辑。
 * @author liuxinwei
 *
 */
public class UserService {

	//需要用哪个实现就new哪个， 面向抽象编程。
	private UserDAO userDAO = new UserDAOImpl();

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void add(User user) {
		userDAO.save(user);
	}
	
	
}
