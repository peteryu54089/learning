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
                id: ${volunteer.id},
                type: 'volunteer'
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function deleteData() {
        $.ajax({
            type: "POST",
            url: "deleteData",
            data: {
                id: ${volunteer.id},
                type: 'volunteer'
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function deleteFinal() {
        $.ajax({
            type: "POST",
            url: "deleteFinal",
            data: {
                id: ${volunteer.id},
                type: 'volunteer'
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
    <div class="col-sm-12">
        <div class="panel panel-primary">
            <div class="panel-heading">志工服務紀錄</div>
            <div class="panel-body">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th colspan="8" class="info text-center">志工服務記錄表</th>
                    </tr>
                    <tr>
                        <td class="info col-sm-1">姓名</td>
                        <td class="col-sm-2"><c:out value="${student.cname}"></c:out></td>
                        <td class="info col-md-1">身分證</td>
                        <td class="col-sm-2"><c:out value="${student.idno}"></c:out></td>
                        <td class="info  col-sm-1">出生日期</td>
                        <td class="col-sm-2"><c:out value="${volunteer.birth}"></c:out></td>
                        <td class="info  col-sm-1">學期</td>
                        <td class="col-sm-2"><c:out value="-"></c:out></td>
                    </tr>
                    <tr>
                        <td class="info col-sm-1">單位名稱</td>
                        <td class="col-sm-2"><c:out value="${volunteer.name}"></c:out></td>
                        <td class="info col-sm-1">單位職務</td>
                        <td class="col-sm-2"><c:out value="${volunteer.unit}"></c:out></td>
                        <td class="info col-md-1">開始日期</td>
                        <td class="col-sm-2"><c:out value="${volunteer.startTime}"></c:out></td>
                        <td class="info  col-sm-1">結束日期</td>
                        <td class="col-sm-2"><c:out value="${volunteer.endTime}"></c:out></td>
                    </tr>
                    <tr>
                        <td class="info col-sm-1">執行時間</td>
                        <td colspan="7" class="col-sm-11"><c:out value="${volunteer.count} hr(s)"></c:out></td>
                    </tr>
                    <tr>
                        <td class="info col-sm-1">檔案下載</td>
                        <td colspan="7" class="col-sm-11"><a
                                href="download?file=<c:out value="${volunteer.document}"></c:out><c:if test="${not empty manager}">&i=${volunteer.idno}</c:if> ">點我下載</a>
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
</div><!-- /container -->
</body>
</html>
