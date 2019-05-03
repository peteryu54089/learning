<%--
  Created by IntelliJ IDEA.
  User: Victor
  Date: 2017/9/5
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
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
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
                                <option value="0" selected>修課紀錄</option>
                                <option value="1">幹部經歷(非本校幹部)</option>
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
                                <option value="104">106</option>
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
                            <div class="panel-heading"><span class="glyphicon glyphicon-exclamation-sign">　</span>學年度第學期$課表
                            </div>
                            <div class="panel-body">
                                <table class="table table-bordered">
                                    <tr class="text-center">
                                        <td>開課學年學期</td>
                                        <td>開課班級</td>
                                        <td>科目名稱</td>
                                        <td>科目代碼</td>
                                        <td>成果狀態</td>
                                    </tr>
                                    <tr class="text-center">
                                        <td>105-1</td>
                                        <td>312</td>
                                        <td>1382</td>
                                        <td>選修物理Ⅲ</td>
                                        <td><a href="#">檢視課程成果</a></td>
                                    </tr>
                                    <tr class="text-center">
                                        <td>105-1</td>
                                        <td>312</td>
                                        <td>1383</td>
                                        <td>英文(五)</td>
                                        <td><a href="#">檢視課程成果</a></td>
                                    </tr>
                                    <tr class="text-center">
                                        <td>105-1</td>
                                        <td>312</td>
                                        <td>1384</td>
                                        <td>國文(五)</td>
                                        <td><a href="#">檢視課程成果</a></td>
                                    </tr>
                                    <tr class="text-center">
                                        <td>105-1</td>
                                        <td>312</td>
                                        <td>1385</td>
                                        <td>日文(一)</td>
                                        <td><a href="#">檢視課程成果</a></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /container -->
</div>
</body>
</html>
