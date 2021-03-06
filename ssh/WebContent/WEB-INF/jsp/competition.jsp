<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>

    <script>
    function uploadFinal() {
        $.ajax({
            type: "POST",
            url: "uploadFinal",
            data: {
                id: ${competition.id},
                type: 'competition'
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function deleteData() {
        var r = confirm('確認刪除?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "deleteData",
            data: {
                id: ${competition.id},
                type: 'competition'
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function deleteFinal() {
        var r = confirm('確認刪除?');
        if (r == false)
            return false;
        $.ajax({
            type: "POST",
            url: "deleteFinal",
            data: {
                id: ${competition.id},
                type: 'competition'
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
    <c:if test="${not empty manager}">
        <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    </c:if>
    <c:if test="${empty manager}">
        <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    </c:if>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-warning">
                <div class="panel-heading">競賽參與紀錄</div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th colspan="8" class="warning text-center">競賽參與紀錄</th>
                        </tr>
                        <tr>
                            <td class="warning col-sm-1">姓名</td>
                            <td class="col-sm-2"><c:out value="${student.cname}"></c:out></td>
                            <td class="warning col-md-1">身分證</td>
                            <td class="col-sm-2"><c:out value="${student.idno}"></c:out></td>
                            <td class="warning  col-sm-1">出生日期</td>
                            <td class="col-sm-2"><c:out value="${competition.birth}"></c:out></td>
                            <td class="warning  col-sm-1">學期</td>
                            <td class="col-sm-2"><c:out value="-"></c:out></td>
                        </tr>
                        <tr>
                            <td class="warning col-sm-1">競賽名稱</td>
                            <td class="col-sm-2"><c:out value="${competition.name}"></c:out></td>
                            <td class="warning col-sm-1">項目</td>
                            <td class="col-sm-2"><c:out value="${competition.item}"></c:out></td>
                            <td class="warning  col-sm-1">競賽成果</td>
                            <td class="col-sm-2"><c:out value="${competition.level} - ${competition.award}"></c:out></td>
                            <td class="warning col-md-1">競賽日期</td>
                            <td class="col-sm-2"><c:out value="${competition.time}"></c:out></td>
                        </tr>
                        <tr>
                            <td class="warning col-sm-1">檔案下載</td>
                            <td colspan="7" class="col-sm-11"><a
                                    href="download?file=<c:out value="${competition.document}"></c:out><c:if test="${not empty manager}">&i=${competition.idno}</c:if> ">點我下載</a>
                            </td>
                        </tr>
                        </thead>
                    </table>
                    <c:if test="${empty manager}">
                        <c:if test="${empty uploaded}">
                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteFinal()">
                                從選定清單刪除
                            </button>
                        </c:if>
                        <c:if test="${uploaded == 1}">
                            <button type="button" class="btn btn-primary btn-sm"
                                    onclick="uploadFinal()">
                                選定上傳至全國資料庫
                            </button>
                        </c:if>
                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteData()">刪除此筆紀錄</button>
                    </c:if>
                </div>
            </div>

        </div>
    </div>
</div><!-- /container -->
</body>
</html>
