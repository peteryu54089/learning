<%--@elvariable id="_cadreImportResult" type="dao.CadreDao.ImportResult"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>
            匯入學生幹部經歷
        </div>
        <form class="panel-body" enctype="multipart/form-data" method="POST">
            <div class="form-group">
                <div class="row">
                    <div class="messages col-xs-12">
                        <c:forEach items="${_cadreImportResult.errors}" var="msg">
                            <div class="alert alert-danger">
                                <span><c:out value="${msg}"/></span>
                            </div>
                        </c:forEach>
                        <c:if test="${_cadreImportResult.errors.size() == 0 && _cadreImportResult.importRecordCnt > 0}">
                            <div class="alert alert-success">
                                <span>成功匯入<c:out value="${_cadreImportResult.importRecordCnt}"/>筆資料</span>
                            </div>
                        </c:if>
                    </div>
                    <div class="clearfix"></div>

                    <div class="col-sm-4 text-right">
                        <label for="import" style="font-size: large" class=" required">上傳檔案</label>
                    </div>
                    <div class="col-sm-8">
                        <input class="form-control" type="file" accept="*.xlsx" name="import" id="import"/>

                        <p class="text-muted">
                            <a href="./resources/template/%E6%A0%A1%E5%85%A7%E5%B9%B9%E9%83%A8%E5%8C%AF%E5%85%A5%E7%AF%84%E6%9C%AC.xlsx">
                                下載範本
                            </a>
                        </p>
                        <p>
                            <button type="submit" class="btn btn-primary">匯入</button>
                        </p>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>