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

    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/datepicker-zh-TW.js"></script>
    <script src="resources/javascript/dateTth.js"></script>
    <script>
    function toogle3(source) {
        checkboxes3 = document.getElementsByName('checkGroup3[]');
        for (var i = 0, n = checkboxes3.length; i < n; i++) {
            checkboxes3[i].checked = source.checked;
        }
    }

    function toogle(source) {
        checkboxes = document.getElementsByName('checkGroup[]');
        for (var i = 0, n = checkboxes.length; i < n; i++) {
            checkboxes[i].checked = source.checked;
        }
    }

    function confirmCheck() {
        var r = confirm("確定刪除?");
        if (r == true) {
            return true;
        } else
            return false;
    }
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢團體輔導管理3
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="GroupCounsel3" enctype='multipart/form-data' method="POST">
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
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>團體輔導管理3
            </div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th><input type="checkbox" onClick="toogle3(this)"/></th>
                        <th class="text-center" width="12%">Label1</th>
                        <th class="text-center" width="12%">Label2</th>
                        <th class="text-center" width="12%">Label3</th>
                        <th class="text-center" width="22%">Label4</th>
                        <th class="text-center" width="22%">Label5</th>
                        <th class="text-center" width="20%">Label6</th>
                    </tr>
                    </thead>
                    <tbody>
                    <form action="BatchDeleteDbGroupCounsel3" method="POST">
                        <c:forEach items="${groupCounsel3List}" var="groupCounsel3" varStatus="status">
                            <tr class="text-center">
                                <td>
                                    <input type="checkbox" name="checkGroup3[]"
                                           value="${groupCounsel3.label1},${groupCounsel3.label2},${groupCounsel3.label3},${groupCounsel3.label4},${groupCounsel3.label5},${groupCounsel3.label6}"/>
                                </td>
                                <td>${groupCounsel3.label1}</td>
                                <td>${groupCounsel3.label2}</td>
                                <td>${groupCounsel3.label3}</td>
                                <td>${groupCounsel3.label4}</td>
                                <td>${groupCounsel3.label5}</td>
                                <td>${groupCounsel3.label6}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${fn:length(groupCounsel3List) == 0}">
                            <tr class="text-center">
                                <td colspan="7">查無資料</td>
                            </tr>
                        </c:if>
                        <button type="submit" class="btn btn-danger btn-xs" onclick="return confirmCheck()">刪除所選資料
                        </button>
                    </form>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-file" aria-hidden="true">　</span>團體輔導管理</div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th><input type="checkbox" onClick="toogle(this)"/></th>
                        <th class="text-center" width="10%">日期</th>
                        <th class="text-center" width="10%">輔導對象</th>
                        <th class="text-center" width="17%">人數</th>
                        <th class="text-center" width="21%">輔導主題</th>
                        <th class="text-center" width="22%">檔案</th>
                        <th class="text-center" width="20%">下載</th>
                    </tr>
                    </thead>
                    <tbody>
                    <form action="BatchDeleteDbGroupCounsel" method="POST">
                        <c:forEach items="${groupCounselList}" var="groupCounsel" varStatus="status">
                            <tr class="text-center">
                                <td>
                                    <input type="checkbox" name="checkGroup[]"
                                           value="${groupCounsel.startDate},${groupCounsel.endDate},${groupCounsel.target},${groupCounsel.total},${groupCounsel.topic},${groupCounsel.content},${groupCounsel.file}"/>
                                </td>
                                <td>${groupCounsel.startDate} ~ ${groupCounsel.endDate}</td>
                                <td>${groupCounsel.target}</td>
                                <td>${groupCounsel.total}</td>
                                <td>${groupCounsel.topic}: ${groupCounsel.content}</td>
                                <td>${groupCounsel.file}</td>
                                <td>
                                    <button type="button" class="btn btn-success"
                                            onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                    value="${groupCounsel.file}"></c:out>'">下載檔案
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${fn:length(groupCounselList) == 0}">
                            <tr class="text-center">
                                <td colspan="7">查無資料</td>
                            </tr>
                        </c:if>
                        <button type="submit" class="btn btn-danger btn-xs" onclick="return confirmCheck()">刪除所選資料
                        </button>
                    </form>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>

