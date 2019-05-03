<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新北市政府 教育局 入口網站 Portal</title>
</head>
<body>
<%
String succsMsg = (String)request.getAttribute("succsMsg");
%>
<script>
location.replace("<%=succsMsg%>");
</script>
</body>
</html>