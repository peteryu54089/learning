<%--
  Created by IntelliJ IDEA.
  User: Victor
  Date: 2017/9/7
  Time: 下午 03:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>高中生歷程系統</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script>
    $(document).ready(function () {
        $("submit").click(function () {
            $.ajax({
                url: "demo_test.txt", success: function (result) {
                    $("#div1").html(result);
                }
            });
        });
    });
    </script>
</head>
<body>

<script>
function selectOnChange(index) {
    window.location = "performance?type=" + index;
}
</script>

<div class="container">
    <br/>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">高中生歷程系統</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="manager">首頁</a></li>
                <li><a href="#">校內幹部紀錄</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">學生資料
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">上傳學生資料</a></li>
                        <li><a href="student_info">檢視學生資料</a></li>
                    </ul>
                </li>
                <li><a href="dowloadtmp">下載各項紀錄</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span> 林家豪 管理員</a></li>
                <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
            </ul>
        </div>
    </nav>
    <div class="col-sm-12">
        <div class="col-sm-4">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-circle-arrow-down"
                                                 aria-hidden="true">　</span>查詢條件
                </div>
                <div class="panel-body">
                    <form action="class_info" method="post">
                        <div class="form-group">

                            <label for="number">學號</label>
                            <input type="text" id="number" class="form-control" name="number" value="810460"/>

                            <label for="year">分類</label>
                            <select class="form-control" id="year" name="year">
                                <option value="0">修課紀錄</option>
                                <option value="1" selected>幹部經歷(非本校幹部)</option>
                                <option value="2">競賽成果</option>
                                <option value="3">檢定證照</option>
                                <option value="4">志工服務</option>
                                <option value="5">其它</option>
                            </select>

                            <label for="sem">學期</label>
                            <select class="form-control" id="sem" name="sem">
                                <option value="100">100</option>
                                <option value="101">101</option>
                                <option value="102">102
                                <option>
                                <option value="103">103</option>
                                <option value="104">104</option>
                                <option value="105" selected>105</option>
                                <option value="106">106</option>
                            </select>

                            <label for="sem">學年</label>
                            <select class="form-control" id="sem" name="sem">
                                <option value="1">上</option>
                                <option value="2">下</option>
                            </select>
                            <br/>
                            <div class="col-md-12 text-center"><input type="submit" class="btn btn-info" value="查詢">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>查詢結果</div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="panel panel-info">
                            <table class="table table-bordered">
                                <tr class="text-center">
                                    <td>社團</td>
                                    <td>起始時間</td>
                                    <td>結束時間</td>
                                    <td>職位</td>
                                    <td>成果狀態</td>
                                </tr>
                                <tr class="text-center">
                                    <td>JAVA社</td>
                                    <td>106-03-01</td>
                                    <td>106-08-01</td>
                                    <td>社長</td>
                                    <td><a href="#">檢視課程成果</a></td>
                                </tr>
                                <tr class="text-center">
                                    <td>臺大醫院</td>
                                    <td>106-09-06</td>
                                    <td>106-09-12</td>
                                    <td>社員</td>
                                    <td><a href="#">檢視課程成果</a></td>
                                </tr>
                                <tr class="text-center">
                                    <td>ABC社</td>
                                    <td>106-08-10</td>
                                    <td>106-08-31</td>
                                    <td>社員</td>
                                    <td><a href="#">檢視課程成果</a></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /container -->
</div>
</body>
</html>
