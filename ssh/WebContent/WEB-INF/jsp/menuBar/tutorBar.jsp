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
                    <li><a href="tutorStuBasicInfo">學生基本資料</a></li>
                    <li><a href="tutorAutobiography">自傳</a></li>
                    <li><a href="tutorStudyPlan">學習計畫</a></li>
                    <li><a href="tutorPerformance">多元表現</a></li>
                    <%--<li><a href="tselectcourse">(X)預選課程紀錄</a></li>--%>
                    <li><a href="tutorCourseResult">修課成績</a></li>
                    <li><a href="tutorCourseRecord">課程成果</a></li>
                    <li><a href="tutorAttend">出缺勤紀錄</a></li>
                </ul>
            </li>
            <%--<li class="dropdown">--%>
                <%--<a class="dropdown-toggle" data-toggle="dropdown" href="#">未知--%>
                    <%--<span class="caret"></span></a>--%>
                <%--<ul class="dropdown-menu">--%>
                    <%--<li><a href="history">查看歷程記錄</a></li>--%>
                    <%--<li><a href="uploadFinal">查看選定資料</a></li>--%>
                    <%--<li><a href="review">學習檔案認證</a></li>--%>
                <%--</ul>--%>
            <%--</li>--%>

        </ul>
        <ul class="nav navbar-nav navbar-right">
        	<c:if test="${isMultiRoles!=null && isMultiRoles=='Y'}">
             	<li><a href="<%=request.getContextPath()%>/LoginSID"> 切換身分 </a></li>
            </c:if>
            <li><a href="#"><span
                    class="glyphicon glyphicon-user"></span> ${account.getName()} 導師
            </a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
