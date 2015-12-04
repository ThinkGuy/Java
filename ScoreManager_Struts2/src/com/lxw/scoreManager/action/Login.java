package com.lxw.scoreManager.action;


import java.sql.DatabaseMetaData;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.lxw.scoreManager.model.*;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class Login extends ActionSupport 
					implements ModelDriven<Student> {
	
	private Student student = new Student();
//	private Map<String, Object> session;
	
//	@Override
//	public void setSession(Map<String, Object> session) {
//		this.session = session;
//	}

	DataBase dataBase = new DataBase();
	
	public String login() {
//		session.put("loginInfo", student.getName());
		
		return SUCCESS;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Override
	public Student getModel() {
		return getStudent();
	}

	@Override
	public void validate() {
		if (student.getStudentId() == null ||
				"".equals(student.getStudentId())) {
			this.addFieldError("studentId", "学号为空");
		} else if (student.getPassword() == null ||
				"".equals(student.getPassword())) {
			this.addFieldError("password", "密码为空");
		} else if (!dataBase.isExist(student.getStudentId().toString()).
				getStudentId().equals(student.getStudentId())) {
			this.addFieldError("studentId", "数据库里无此人");
		} else if (!dataBase.isExist(student.getStudentId().toString()).
				getPassword().equals(student.getPassword())) {
			this.addFieldError("password", "密码错误");
		}
	}
}
