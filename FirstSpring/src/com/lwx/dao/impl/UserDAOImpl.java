package com.lwx.dao.impl;

import com.lwx.dao.UserDAO;
import com.lwx.model.User;

public class UserDAOImpl implements UserDAO{
	
	public void save(User user) {
		System.out.println(user + "has saved.");
	}
}
