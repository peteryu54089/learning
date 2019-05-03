<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <br/>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">高中生歷程系統</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="rmain">首頁</a></li>
                <li><a href="review">學習檔案認證</a></li>
                <li><a href="#">查看個人課表</a></li>
                <li><a href="#">歷史紀錄</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span> 林家豪 老師</a></li>
                <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
            </ul>
        </div>
    </nav>
    <div class="col-sm-12">
        <div class="col-sm-6">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>使用者資訊</div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th colspan="2" class="text-center">基本資料</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="text-center">
                            <td>中文姓名</td>
                            <td>林家豪</td>
                        </tr>
                        <tr class="text-center">
                            <td>職員編號</td>
                            <td>40153</td>
                        </tr>
                        <tr class="text-center">
                            <td>身分證</td>
                            <td>T123456789</td>
                        </tr>
                        <tr class="text-center">
                            <td>電子郵件</td>
                            <td>s111111@ntut.edu.tw</td>
                        </tr>
                        <tr class="text-center">
                            <td>權限</td>
                            <td>教師</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-exclamation-sign">　</span>通知</div>
                <div class="panel-body"><span class="glyphicon glyphicon-pencil"></span><a href="review_detail">學生:王伯駿
                    認證通知</a></div>
                <hr>
                <div class="panel-body"><span class="glyphicon glyphicon-pencil"></span><a href="review_detail">學生:蘇浚賢
                    認證通知</a></div>
                <hr>
                <div class="panel-body"><a href="#">......</a></div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="panel panel-primary">
                <div class="panel-heading"><span class="glyphicon glyphicon-bell">　</span>備註</div>
                <div class="panel-body">有學生資料待認證中</div>
            </div>
        </div>
    </div>
</div><!-- /container -->
</body>
</html>
