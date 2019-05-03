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
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">前台功能
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <li><a href="announcementManage">校內公告管理</a></li>
                    <%-- <li><a href="bannerManage">Banner管理</a></li>  不開放學校使用 --%>
                </ul>
            </li>
            

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">基本資料
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <li><a href="showStudentBasicInfo">基本資料</a></li>
                    <li><a href="showStudentAutoBio">自傳</a></li>
                    <li><a href="showStudentStudyPlan">學習計畫檔案</a></li>
                    <li><a href="showStudentAttend">出缺勤紀錄</a></li>
                    <li><a href="showStudentOtherDocument">文件管理</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">課程紀錄
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <%--<li><a href="#">預選課程紀錄</a></li>--%> <%-- 功能未完成--%>
                    <%--<li><a href="#">課程諮詢紀錄</a></li>--%> <%-- 功能未完成--%>
                    <li><a href="showStudentCourseScore">修課成績</a></li>
                    <li><a href="showStudentCourseRecord">課程學習成果</a></li>
                    <li><a href="UnlockCourseRecord">解鎖課程學習成果</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">多元表現
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <li><a href="showStudentPerformance?p=cadre">幹部經歷紀錄</a></li>
                    <li><a href="showStudentPerformance?p=competition">競賽參與紀錄</a></li>
                    <li><a href="showStudentPerformance?p=license">檢定證照紀錄</a></li>
                    <li><a href="showStudentPerformance?p=volunteer">志工服務紀錄</a></li>
                    <li><a href="showStudentPerformance?p=other">其他活動紀錄</a></li>
                    <li><a href="UnlockPerformance">解鎖多元表現</a></li>
                </ul>
            </li>


            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">提交紀錄
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <li><a target="_blank" href="SubmitCourseReport">課程學習成果提交紀錄</a></li>	 <%-- 功能未完全完成--%>
                    <li><a target="_blank" href="SubmitPerformanceReport">多元表現提交紀錄</a></li>	 <%-- 功能未完全完成--%>
                </ul>
            </li>

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">校內幹部經歷管理
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="showStudentPerformance?p=cadreRecordViewCadreRecord">校內幹部經歷紀錄</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">系統管理功能
                    <span class="caret"></span></a>

                <ul class="dropdown-menu">
                    <li><a href="roleConfiguration">帳號權限管理</a></li>
                    <%-- <li><a href="schoolBasicInfoMange">設定學校資訊</a></li> --%>	<%-- 不開放使用 --%>
                    <li><a href="courseRecordConfiguration">課程成果提交設定</a></li>
                    <li><a href="performanceConfiguration">多元表現提交設定</a></li>
                    <li><a href="recordExporter">各項記錄下載</a></li>	<%-- 功能未完全完成--%>
                    <%--<li><a href="#">學習經歷檔案下載</a></li>--%>	<%-- 暨大系統已無此功能--%>
                    <li><a href="otherDocumentConfiguration">文件上傳大小設定</a></li>
                </ul>
            </li>

        </ul>
        <ul class="nav navbar-nav navbar-right">
        	<c:if test="${isMultiRoles!=null && isMultiRoles=='Y'}">
             	<li><a href="<%=request.getContextPath()%>/LoginSID"> 切換身分 </a></li>
            </c:if>
            <li><a href="#"><span class="glyphicon glyphicon-user"></span> ${account.getName()} 校管理員</a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
