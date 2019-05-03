<%@ page import="util.ObjectUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                多元表現提交紀錄 (詳情)
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center;table-layout: fixed">
                    <tr>
                        <th class="text-center" width="12%">學年</th>
                        <th class="text-center">類型</th>
                        <th class="text-center">身份證字號</th>
                        <th class="text-center">文件名稱</th>
                        <th class="text-center">影片名稱</th>
                        <th class="text-center">外部連結</th>
                    </tr>
                    <c:forEach items="${list}" var="record">
                        <tr class="text-center">
                            <td>${ObjectUtils.getField(record, "year")}</td>
                            <td>${ObjectUtils.getField(record, "type")}</td>
                            <td>${ObjectUtils.getField(record, "idno")}</td>
                            <td>${ObjectUtils.getField(record, "doc_org_fn")}</td>
                            <td>${ObjectUtils.getField(record, "video_org_fn")}</td>
                            <td>${ObjectUtils.getField(record, "ext_link")}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(list) == 0}">
                        <tr class="text-center">
                            <td colspan="6">查無資料</td>
                        </tr>
                    </c:if>
                </table>

                <p class="text-muted">學年為0表示尚未確認學年</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
