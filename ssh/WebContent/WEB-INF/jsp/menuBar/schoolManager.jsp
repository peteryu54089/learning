<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/23
  Time: 下午 08:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../includes/banner.jsp"/>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">高中生歷程系統</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="main">首頁</a></li>
            <li><a href="accountManage">帳號權限管理</a></li>
            <li><a href="SchoolSystemSet">系統設定</a></li>
            <li><a href="CoursePerSet">課程成果提交設定</a></li>
            <li><a href="PerformanceSet">多元表現提交設定</a></li>
            <li><a href="schoolRecord">各項紀錄下載</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#"><span
                    class="glyphicon glyphicon-user"></span> ${teacher.staff_cname} ${teacher.job_cname}
            </a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>