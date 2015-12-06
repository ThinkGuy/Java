<%@page import="com.lxw.scoreManager.model.Student"%>
<%@ page language="java"  pageEncoding="UTF-8"%> 

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		学号:${requestScope.student.studentId} <br>
		姓名:${requestScope.student.name } <br>
		语文:${requestScope.student.score.chinese } <br>
		英语:${requestScope.student.score.english } <br>
		数学:${requestScope.student.score.math } <br>
		
		<input type="button" value="重新选择功能" 
			onclick="window.location.href='choose.jsp'">
		<input type="button" value="再查询" 
			onclick="window.location.href='search.jsp'">
	</body>
</html>