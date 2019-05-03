<%@ page import="util.ObjectUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>

</head>
<body>
<p>&nbsp;</p>
<div class="container">
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程學習成果提交紀錄 (詳情)
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">提交時間</th>
                        <th class="text-center">身份證字號</th>
                        <th class="text-center">課程名稱</th>
                        <th class="text-center">成果檔案名稱</th>
                    </tr>
                    <c:forEach items="${list}" var="record">
                        <tr class="text-center">
                            <td>${ObjectUtils.getField(record, "year")}</td>
                            <td>${ObjectUtils.getField(record, "sem")}</td>
                            <td>${ObjectUtils.getField(record, "date")}</td>
                            <td>${ObjectUtils.getField(record, "idno")}</td>
                            <td>${ObjectUtils.getField(record, "cn")}</td>
                            <td>${ObjectUtils.getField(record, "files")}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(list) == 0}">
                        <tr class="text-center">
                            <td colspan="6">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
