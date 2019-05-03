<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../includes/banner.jsp"/>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">高中生歷程系統</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="main">首頁</a></li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">檢視學生
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="teacherCourseRecord">驗證課程成果</a></li>
                </ul>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
        	<c:if test="${isMultiRoles!=null && isMultiRoles=='Y'}">
             	<li><a href="<%=request.getContextPath()%>/LoginSID"> 切換身分 </a></li>
            </c:if>
            
            <li><a href="#"><span
                    class="glyphicon glyphicon-user"></span> ${account.getName()} 授課教師
            </a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
