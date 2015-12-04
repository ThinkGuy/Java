<%@ page language="java"  pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<form action="insert.action">
			<h2>插入功能。</h2> <br><br>
			请输入学号:<input type="text" name="student.studentId"> <br><br>
			请输入姓名:<input type="text" name="student.name"> <br><br>
			请输入语文成绩:<input type="text" name="student.score.chinese"> <br><br>
			请输入英语成绩:<input type="text" name="student.score.english"> <br><br>
			请输入数学成绩:<input type="text" name="student.score.math"> <br><br>
			<input type="submit" name="submit"> <br><br>
			<s:fielderror></s:fielderror>
		</form>
	</body>
</html>