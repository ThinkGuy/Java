<%@ page language="java"  pageEncoding="UTF-8"%> 

<html>
	<head>
		<title>学生成绩管理系统</title>
	</head>
	
	<body>
		<form action="scoreManager.do">
			<h2>更新功能。</h2> <br><br>
			请输入学号:<input type="text" name="studentId"> <br><br>
			请输入语文成绩:<input type="text" name="chinese"> <br><br>
			请输入英语成绩:<input type="text" name="english"> <br><br>
			请输入数学成绩:<input type="text" name="math"> <br><br>
			<input type="submit" value="update" name="submit">
		</form>
	</body>
</html>