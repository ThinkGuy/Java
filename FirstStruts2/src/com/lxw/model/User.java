package com.lxw.model;

import java.util.ArrayList;

public class User {
	
	private String userName;
	private String password;
	private ArrayList<String> bookList;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getBookList() {
		return bookList;
	}

	public void setBookList(ArrayList<String> bookList) {
		this.bookList = bookList;
	}


}
