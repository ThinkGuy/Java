package com.lxw.action;

import javax.xml.stream.events.StartDocument;

import com.opensymphony.xwork2.ActionSupport;

public class Timer extends ActionSupport {

	@Override
	public String execute() throws Exception {
		for (int i=0; i<10000; i++) {
			System.out.println("执行时间Action。");	
		}

		return SUCCESS;
	}
	
	
}
