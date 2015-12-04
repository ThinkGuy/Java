<%@ page language="java"  pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %> 

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<form action="search.action" method="post">
			<h2>查询功能。</h2> <br><br>
			请输入学号:<input type="text" name="student.studentId"> <br><br>
			<input type="submit" value="search" name="submit"> <br>
			
			<s:fielderror></s:fielderror>
		</form>
	</body>
</html>