<%--@elvariable id="groupCounselList" type="java.util.List<model.GroupCounsel>"--%>

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
    <script>
    function download(id) {
        window.location.href = './downloadGroupCounsel?id=' + id;
    }
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢團體輔導管理
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="GroupCounsel" enctype='multipart/form-data' method="GET">
                        <input name="action" style="display:none" value="search">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="startDate" style="font-size: large" class="col-sm-3">日期起</label>
                                <div class="col-sm-6">
                                    <input type="date" id="startDate" name="startDate" class="form-control"
                                           placeholder="請點擊選擇年月日">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="endDate" style="font-size: large" class="col-sm-3">日期訖</label>
                                <div class="col-sm-6">
                                    <input type="date" id="endDate" name="endDate" class="form-control"
                                           placeholder="請點擊選擇年月日">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="title" style="font-size: large" class="col-sm-3">標題</label>
                                <div class="col-sm-6">
                                    <input type="text" id="title" name="title" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6"><input type="submit" class="btn btn-warning" value="查詢"></div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>團體輔導紀錄
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center" width="20%">日期</th>
                        <th class="text-center" width="10%">人數</th>
                        <th class="text-center" width="25%">輔導主題</th>
                        <th class="text-center" width="20%">PDF</th>
                    </tr>
                    <c:forEach items="${groupCounselList}" var="groupCounsel" varStatus="status">
                        <tr class="text-center">
                            <td>${groupCounsel.startTime}<br>
                                ~ ${groupCounsel.endTime}</td>
                            <td>${groupCounsel.total}</td>
                            <td>${groupCounsel.title}: ${groupCounsel.description}</td>
                            <td>
                                <a href="javascript:download(${groupCounsel.id})">${groupCounsel.originalFilename}</a>
                            </td>
                        </tr>
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

