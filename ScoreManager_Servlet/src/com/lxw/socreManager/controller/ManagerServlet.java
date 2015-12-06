package com.lxw.socreManager.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lxw.scoreManager.model.DateBase;
import com.lxw.scoreManager.model.Student;

@WebServlet("/managerServlet")
public class ManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		DateBase dateBase = new DateBase();
		Student student = new Student();
		String studentId = request.getParameter("studentId");
		String way = request.getParameter("submit");
		
		RequestDispatcher view = null;
		
		if (studentId.equals("")) {
			response.sendRedirect(way + ".jsp");
		} else if (!studentId.matches("\\d+") || !dateBase.isExist(studentId)
				.getStudentId().toString().equals(studentId)) {
			response.sendRedirect(way + "Wrong.jsp");		
		} else if ("search".equals(way)) {
			student = dateBase.searchInfo(studentId);
			
			request.setAttribute("student", student);

			view =  request.getRequestDispatcher("searchDisplay.jsp");
			view.forward(request, response);
		} else if("delete".equals(way)) {
			student = dateBase.searchInfo(studentId);
			request.setAttribute("student", student);
			request.setAttribute("success", dateBase.deleteInfo(studentId));
			
			view = request.getRequestDispatcher("deleteDisplay.jsp");
			view.forward(request, response);
		} else if("update".equals(way)) {
			int chinese = Integer.parseInt(request.getParameter("chinese"));
			int english = Integer.parseInt(request.getParameter("english"));
			int math = Integer.parseInt(request.getParameter("math"));
			request.setAttribute("success", dateBase.updateInfo(studentId, 
					chinese, english, math));	
			
			student = dateBase.searchInfo(studentId);
			request.setAttribute("student", student);
			
			view = request.getRequestDispatcher("updateDisplay.jsp");
			view.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
