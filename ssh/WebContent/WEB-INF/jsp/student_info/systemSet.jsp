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
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">
    <link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css">
    <script src="resources/javascript/jquery-1.11.2.min.js"></script>
    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<script>
function checkRegex() {
    var patt = new RegExp("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    var num = new RegExp("^\\d+$");
    var sStartTime = document.getElementById("sStartTime");
    var sEndTime = document.getElementById("sEndTime");
    var tStartTime = document.getElementById("tStartTime");
    var tEndTime = document.getElementById("tEndTime");
    var tInterval = document.getElementById("tInterval");
    if (!patt.test(sStartTime.value)) {
        alert("開放學生上傳時間格式錯誤，應為yyyy-MM-dd HH:mm:ss");
        sStartTime.focus();
        return false;
    }
    if (!patt.test(sEndTime.value)) {
        alert("截止學生上傳時間格式錯誤，應為yyyy-MM-dd HH:mm:ss");
        sStartTime.focus();
        return false;
    }
    if (!patt.test(tStartTime.value)) {
        alert("截止老師驗證時間格式錯誤，應為yyyy-MM-dd HH:mm:ss");
        sStartTime.focus();
        return false;
    }
    if (!patt.test(tEndTime.value)) {
        alert("開放學生上傳時間格式錯誤，應為yyyy-MM-dd HH:mm:ss");
        sStartTime.focus();
        return false;
    }
    if (!num.test(tInterval.value)) {
        alert("開放老師修改時間錯誤，應為一個數字");
        return false;
    }
    return true;
}
</script>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="col-sm-12">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-cog" aria-hidden="true"> </span>系統設置
                </div>
                <div class="panel-body">
                    ${result}
                    <form id="myForm" action="" method="POST" onsubmit="return checkRegex()">
                        <label for="sStartTime">開放學生上傳時間</label>
                        <input type="text" class=" form-control" id="sStartTime" name="sStartTime"
                               placeholder="請點擊選擇年月日" value="${systemSet.sStartTime}" required>
                        <label for="sEndTime">截止學生上傳時間</label>
                        <input type="text" class="form-control" id="sEndTime" name="sEndTime"
                               placeholder="請點擊選擇年月日" value="${systemSet.sEndTime}" required>
                        <label for="tStartTime">開放老師驗證時間</label>
                        <input type="text" class="form-control" id="tStartTime" name="tStartTime"
                               placeholder="請點擊選擇年月日" value="${systemSet.tStartTime}" required>
                        <label for="tEndTime">截止老師驗證時間</label>
                        <input type="text" class="form-control" id="tEndTime" name="tEndTime"
                               placeholder="請點擊選擇年月日" value="${systemSet.tEndTime}" required>
                        <label for="tInterval">老師修改權限(至驗證起(天)內可修改)</label>
                        <input type="number" class="form-control" id="tInterval" name="tInterval"
                               placeholder="天" value="${systemSet.tInterval}" required>
                        <label for="size">文件上傳大小設定</label>
                        <input type="number" class="form-control" id="size" name="size"
                               placeholder="MB" value="${systemSet.size}" required>
                        <br/>
                        <input type="submit" class="btn btn-info" value="送出">
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
