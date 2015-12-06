package com.lxw.socreManager.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lxw.scoreManager.model.DateBase;
	
@WebServlet("/scoreManager")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String studentId = request.getParameter("studentId");
		String password = request.getParameter("password"); 
		DateBase dateBase = new DateBase();
		
		RequestDispatcher view = null;
		
		if (studentId.equals("")) {
			view = request.getRequestDispatcher("login.jsp");
		} else if (!studentId.matches("\\d+") || !dateBase.isExist(studentId)
				.getStudentId().toString().equals(studentId)) {
			request.setAttribute("login", "idFalse");
			view = request.getRequestDispatcher("idFalse.jsp");
		} else if (!dateBase.isExist(studentId).getPassword()
				.equals(password)) {
			request.setAttribute("login", "passwordFalse");
			view = request.getRequestDispatcher("passwordFalse.jsp");
		} else {
			request.setAttribute("login", "ok");
			request.setAttribute("studentId", studentId);
			view = request.getRequestDispatcher("choose.jsp");
		}
		
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
