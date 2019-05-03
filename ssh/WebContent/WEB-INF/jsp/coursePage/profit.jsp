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
    <title>高中生歷程系統</title>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">

    <script>
    function uploadFinal() {
        <c:if test="${profit.status != 2}">
        alert('尚未通過老師認證。');
        return false;
        </c:if>
        $.ajax({
            type: "POST",
            url: "uploadFinal",
            data: {
                id: ${profit.id},
                type: 'profit'
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
                id: ${profit.id},
                type: 'profit'
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
                id: ${profit.id},
                type: 'profit'
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
    <div class="col-sm-12">
        <div class="panel panel-default">
            <div class="panel-heading">課程學習成果紀錄</div>
            <div class="panel-body">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th colspan="8" class="active text-center">課程學習成果紀錄表</th>
                    </tr>
                    <tr>
                        <td class="active col-sm-1">姓名</td>
                        <td class="col-sm-2"><c:out value="${student.cname}"></c:out></td>
                        <td class="active col-md-1">身分證</td>
                        <td class="col-sm-2"><c:out value="${student.idno}"></c:out></td>
                        <td class="active col-sm-1">學年度</td>
                        <td class="col-sm-2"><c:out value="${profit.term_year}"></c:out></td>
                        <td class="active col-sm-1">學期</td>
                        <td class="col-sm-2"><c:out value="${profit.term_sem}"></c:out></td>
                    </tr>
                    <tr>
                        <td class="active col-sm-1">課程名稱</td>
                        <td colspan="7" class="col-sm-2"><c:out
                                value="${profit.course_cname}"></c:out></td>
                    </tr>
                    <tr>
                        <td class="active col-sm-1">成果簡述</td>
                        <td colspan="7" class="col-sm-2"><c:out
                                value="${profit.content}"></c:out></td>
                    </tr>
                    <tr>
                        <td class="active col-sm-2">目前狀態</td>
                        <td colspan="7" class="col-sm-2">
                            <c:if test="${profit.status == \"1\"}"> 老師尚未認證 </c:if>
                            <c:if test="${profit.status == \"-1\"}"> 認證未通過 </c:if>
                            <c:if test="${profit.status == \"2\"}"> 認證已通過 </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class=" active col-sm-1">檔案下載</td>
                        <td colspan="7" class="col-sm-11"><a
                                href="download?file=<c:out value="${profit.document}"></c:out> ">點我下載</a>
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
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteData()">
                        刪除此筆紀錄
                    </button>
                </c:if>
            </div>
        </div>

    </div>
</div><!-- /container -->
</body>
</html>
