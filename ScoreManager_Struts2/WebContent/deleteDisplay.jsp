<%@ page language="java"  pageEncoding="UTF-8"%> 

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<h2>删除成功</h2> <br>
		
		 学号:${student.studentId} <br>
		姓名:${student.name } <br>
		语文:${student.score.chinese } <br>
		英语:${student.score.english } <br>
		数学:${student.score.math } <br>
		
		<input type="button" value="重新选择功能" 
			onclick="window.location.href='choose.jsp'">
		<input type="button" value="再删除" 
			onclick="window.location.href='delete.jsp'">
	</body>
</html>