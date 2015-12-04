<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 标签 自动提示错误 -->
<%@ taglib prefix="s" uri="/struts-tags" %>   

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	${loginError}
	<form action="login.action" method="post"> 
	<!-- 显示userName的错误， 与addFiledError(,)对应 -->
	用户名:  <input type="text" name="userName"> <s:fielderror name="userName"> </s:fielderror><br><br>
	密码:   <input type="password" name="password"> <br><br>
	书籍1:  <input type="text" name="bookList[0]"> <br><br>
	书籍2:  <input type="text" name="bookList[1]"> <br><br>
	<input type="submit" value="提交">
	</form>
</body>
</html>