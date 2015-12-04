package com.lxw.action;

import com.opensymphony.xwork2.ActionSupport;

public class Hello extends ActionSupport {
	
	public String execute() {
		System.out.println("执行Action");
		
		return SUCCESS;
	}
	
	public String add() {
		return "add";
	}
}
