<%@ page language="java"  pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<form action="delete.action">
			<h2>删除功能。</h2> <br><br>
			请输入学号:<input type="text" name="student.studentId"> <br><br>
			<input type="submit" value="delete" name="submit"> <br>
			<s:fielderror></s:fielderror>
		</form>
	</body>
</html>