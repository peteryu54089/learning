<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>

    <link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css">
    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/datepicker-zh-TW.js"></script>
    <script src="resources/javascript/dateTth.js"></script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-info">
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>課程諮詢報表
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="CourseCounsel" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="startDate" class="col-sm-2" style="font-size: large">日期</label>
                                <div class="col-sm-10">
                                    <input type="text" id="startDate" name="startDate" class="min_go_date form-control"
                                           placeholder="請點擊選擇年月日" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="topic" class="col-sm-2" style="font-size: large">主題</label>
                                <div class="col-sm-10">
                                    <input type="text" id="topic" name="topic" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-2"></div>
                                <div class="col-sm-10">
                                    <button class="btn btn-warning">查詢</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <button class="btn btn-info" onclick="javascript:location.href='ExportCourseCounsel'">匯出Excel</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>個別諮詢管理</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">Label1</th>
                        <th class="text-center">Label2</th>
                        <th class="text-center">Label3</th>
                        <th class="text-center">Label4</th>
                        <th class="text-center">Label5</th>
                    </tr>
                    <c:forEach items="${individualCounselList}" var="individualCounsel">
                        <tr class="text-center">
                            <td>${individualCounsel.label1}</td>
                            <td>${individualCounsel.label2}</td>
                            <td>${individualCounsel.label3}</td>
                            <td>${individualCounsel.label4}</td>
                            <td>${individualCounsel.label5}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(individualCounselList) == 0}">
                        <tr class="text-center">
                            <td colspan="5">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-file" aria-hidden="true">　</span>團體輔導管理</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center" width="20%">日期</th>
                        <th class="text-center" width="20%">輔導對象</th>
                        <th class="text-center" width="10%">人數</th>
                        <th class="text-center" width="25%">輔導主題</th>
                        <th class="text-center" width="25%">檔案</th>
                    </tr>
                    <c:forEach items="${groupCounselList}" var="groupCounsel" varStatus="status">
                    <tr class="text-center">
                        <td>${groupCounsel.startDate} ~ ${groupCounsel.endDate}</td>
                        <td>${groupCounsel.target}</td>
                        <td>${groupCounsel.total}</td>
                        <td>${groupCounsel.topic}: ${groupCounsel.content}</td>
                        <td>${groupCounsel.file}</td>
                        </c:forEach>
                        <c:if test="${fn:length(groupCounselList) == 0}">
                    <tr class="text-center">
                        <td colspan="5">查無資料</td>
                    </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>

