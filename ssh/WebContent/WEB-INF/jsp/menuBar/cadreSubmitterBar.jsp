<%--
  Created by IntelliJ IDEA.
  User: Ching Yun Yu
  Date: 2018/4/27
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
            <li><a href="cadreRecordImport">匯入幹部經歷</a></li>
            <li><a href="cadreRecordBuildCadreRecord">建立幹部經歷</a></li>
            <li><a href="CadreRecordSync">同步社團幹部資料</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
        	<c:if test="${isMultiRoles!=null && isMultiRoles=='Y'}">
             	<li><a href="<%=request.getContextPath()%>/LoginSID"> 切換身分 </a></li>
            </c:if>
            <li><a href="#"><span
                    class="glyphicon glyphicon-user"></span> ${account.getName()} 幹部經歷紀錄建立人
            </a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
