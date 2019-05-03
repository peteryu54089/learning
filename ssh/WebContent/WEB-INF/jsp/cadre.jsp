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
                id: ${cadre.id},
                type: 'cadre'
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
                id: ${cadre.id},
                type: 'cadre'
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
                id: ${cadre.id},
                type: 'cadre'
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
            <div class="panel panel-success">
                <div class="panel-heading">校外幹部記錄</div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th colspan="8" class="success text-center">校外幹部記錄表</th>
                        </tr>
                        <tr>
                            <td class="success col-sm-1">姓名</td>
                            <td class="col-sm-2"><c:out value="${student.cname}"></c:out></td>
                            <td class="success col-md-1">身分證</td>
                            <td class="col-sm-2"><c:out value="${student.idno}"></c:out></td>
                            <td class="success  col-sm-1">出生日期</td>
                            <td class="col-sm-2"><c:out value="${cadre.birth}"></c:out></td>
                            <td class="success  col-sm-1">學期</td>
                            <td class="col-sm-2"><c:out value="${cadre.term}"></c:out></td>
                        </tr>
                        <tr>
                            <td class="success col-sm-1">單位名稱</td>
                            <td class="col-sm-2"><c:out value="${cadre.unit}"></c:out></td>
                            <td class="success col-sm-1">擔任職務</td>
                            <td class="col-sm-2"><c:out value="${cadre.job}"></c:out></td>
                            <td class="success col-md-1">開始日期</td>
                            <td class="col-sm-2"><c:out value="${cadre.startTime}"></c:out></td>
                            <td class="success  col-sm-1">結束日期</td>
                            <td class="col-sm-2"><c:out value="${cadre.endTime}"></c:out></td>
                        </tr>
                        <tr>
                            <td class="success col-sm-1">檔案下載</td>
                            <td colspan="7" class="col-sm-11"><a
                                    href="download?file=<c:out value="${cadre.document}"></c:out><c:if test="${not empty manager}">&i=${cadre.idno}</c:if> ">點我下載</a>
                            </td>
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
                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteData()">
                            刪除此筆紀錄
                        </button>
                    </c:if>
                </div>
            </div>

        </div>
    </div>
</div><!-- /container -->
</body>
</html>
