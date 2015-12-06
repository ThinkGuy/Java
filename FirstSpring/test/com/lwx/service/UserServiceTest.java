package com.lwx.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lwx.model.User;

public class UserServiceTest {

	@Test
	public void testAdd() {
//		fail("Not yet implemented");
		UserService service = new UserService();
		User user = new User();
		service.add(user);
	}

}
