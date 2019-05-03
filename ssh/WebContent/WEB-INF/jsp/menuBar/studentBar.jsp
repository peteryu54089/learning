<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
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
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">基本資料
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <%-- <li><a href="studentBasicInfoModifyServlet">基本資料維護</a></li> --%>	<%-- 不開放使用 --%>
                    <li><a href="autobiography">自傳</a></li>
                    <li><a href="studyplan">學習計畫</a></li>
                    <li><a href="attend">出缺勤紀錄</a></li>
                    <li><a href="document">文件管理</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">課程紀錄
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="selectcourse">預選課程紀錄</a></li>
                    <li><a href="IndividualCounsel">個人課程諮詢紀錄</a></li>
                    <li><a href="GroupCounsel">團體課程諮詢紀錄</a></li>
                    <li><a href="courseresult">修課成績</a></li>
                    <li><a href="courseRecord">課程學習成果</a></li>
                    <li><a href="courseRecordCheck">勾選課程學習成果</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">多元表現
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="cadre">多元表現</a></li>
                    <li><a href="performanceCheck">勾選多元表現</a></li>
                </ul>
            </li>
            
            <%-- 暨大系統已無此功能
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">資料匯出
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="resume">履歷表管理</a></li>
                    <li><a href="lifeplan">生涯規劃書管理</a></li>
                </ul>
            </li>
            --%>
            
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">提交紀錄
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="SubmitCourseReport">課程學習成果提交紀錄</a></li>
                    <li><a href="SubmitPerformanceReport">多元表現提交紀錄</a></li>
                </ul>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#"><span class="glyphicon glyphicon-user"></span> ${account.getName()} 學生</a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
