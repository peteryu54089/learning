<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/25
  Time: 上午 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <script>
    function exportAttend() {
        var r = confirm('確認匯出?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "DownloadRecord",
            data: {
                label: "出缺勤紀錄",
                select: document.getElementById("attendRecord").value
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function exportCadre() {
        var r = confirm('確認匯出?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "DownloadRecord",
            data: {
                label: "校內幹部經歷名冊",
                select: document.getElementById("cadreRecord").value
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function exportPerformance() {
        var r = confirm('確認匯出?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "DownloadRecord",
            data: {
                label: "學生自填多元表現名冊",
                select: document.getElementById("performanceRecord").value
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function exportAutoOrStudy() {
        var r = confirm('確認匯出?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "DownloadRecord",
            data: {
                label: "自傳與學習計畫",
                select: document.getElementById("autoOrStudyRecord").value
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>各項紀錄下載</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-sm-1"></div>
                    <div class="col-sm-11">
                        <label for="attendRecord" style="font-size: large" class="col-sm-2">出缺勤紀錄</label>
                        <div class="col-sm-8">
                            <select id="attendRecord" name="attendRecord" class="form-control">
                                <option value="" selected disabled>-選擇學年期-</option>
                                <option>107-1</option>
                                <option>107-2</option>
                                <option>106-1</option>
                                <option>106-2</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-info" onclick="exportAttend()">匯出檔案Excel</button>
                        </div>
                    </div>
                </div>
                <div class="row"><br/></div>
                <div class="row">
                    <div class="col-sm-1"></div>
                    <div class="col-sm-11">
                        <label for="cadreRecord" style="font-size: large" class="col-sm-2">校內幹部經歷名冊</label>
                        <div class="col-sm-8">
                            <select id="cadreRecord" name="cadreRecord" class="form-control">
                                <option value="" selected disabled>-選擇學年期-</option>
                                <option>107-1</option>
                                <option>107-2</option>
                                <option>106-1</option>
                                <option>106-2</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-info" onclick="exportCadre()">匯出名冊Excel</button>
                        </div>
                    </div>
                </div>
                <div class="row"><br/></div>
                <div class="row">
                    <div class="col-sm-1"></div>
                    <div class="col-sm-11">
                        <label for="performanceRecord" style="font-size: large" class="col-sm-2">學生自填多元表現名冊</label>
                        <div class="col-sm-8">
                            <select id="performanceRecord" name="performanceRecord" class="form-control">
                                <option value="" selected disabled>-選擇學年期-</option>
                                <option>107-1</option>
                                <option>107-2</option>
                                <option>106-1</option>
                                <option>106-2</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-info" onclick="exportPerformance()">匯出名冊Excel</button>
                        </div>
                    </div>
                </div>
                <div class="row"><br/></div>
                <div class="row">
                    <div class="col-sm-1"></div>
                    <div class="col-sm-11">
                        <label for="autoOrStudyRecord" style="font-size: large" class="col-sm-2">自傳與學習計畫</label>
                        <div class="col-sm-8">
                            <select id="autoOrStudyRecord" name="autoOrStudyRecord" class="form-control">
                                <option value="" selected disabled>-選擇學年期-</option>
                                <option>107-1</option>
                                <option>107-2</option>
                                <option>106-1</option>
                                <option>106-2</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-info" onclick="exportAutoOrStudy()">匯出Word</button>
                        </div>
                    </div>
                </div>
                <div class="row"><br/></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
