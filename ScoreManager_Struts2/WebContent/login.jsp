<%@ page language="java"  pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>   

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<h2>欢迎进入学生成绩管理系统</h2> <br><br>
		<form action="login.action" method="post">
			学号：<input name="studentId" type="text"> <br><br>
			密码：<input name="password" type="password">	<br><br>
			<input type="submit" value="提交"> <br>
			
			<s:fielderror></s:fielderror>
		</form>
		
	</body>
</html>