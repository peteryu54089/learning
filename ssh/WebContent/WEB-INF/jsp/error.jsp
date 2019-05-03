<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

</head>
<body>
<c:if test="${error == null}"><p style="color:red;">您並無權限使用此檔案或此檔案不存在請重新上傳，若有疑問請洽系統管理員。</p></c:if>
<p style="color:red;"> ${error}</p>

</body>

</html>
