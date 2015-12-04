package com.lxw.action;

import org.apache.struts2.interceptor.SessionAware;

import com.lxw.model.User;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import java.util.Map;

public class Login extends ActionSupport implements ModelDriven<User>,
		SessionAware {

	private User user = new User();
	private Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String execute() {
		System.out.println(user.getUserName());
		System.out.println(user.getBookList().get(0));

		if ("admin".equals(user.getUserName())
				&& "20134019".equals(user.getPassword())) {
			session.put("loginInfo", user.getUserName());
			return SUCCESS;
		} else {
			session.put("loginErro", "用户名或密码不正确");
			return ERROR;
		}

	}

	@Override
	public User getModel() {
		return user;
	}

	/**
	 * 重写的方法，自动返回input
	 */
	@Override
	public void validate() {
		if (user.getUserName() == null || "".equals(user.getUserName())) {
			this.addFieldError("userName", "用户名不能为空");
		}
	}

}
