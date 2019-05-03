<%--@elvariable id="account" type="model.Account"--%>
<%--@elvariable id="workTeam" type="model.role.WorkTeam"--%>
<%@ page import="model.Authority.RoleIndex" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="row">
                <div class="col-sm-6">
                    <div class="panel panel-danger">
                        <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>使用者資訊
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th colspan="2" class="text-center">基本資料</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr class="text-center">
                                    <td>中文姓名</td>
                                    <td>${account.name}</td>
                                </tr>
                                <tr class="text-center">
                                    <td>職員編號</td>
                                    <td>${workTeam.staffCode}</td>
                                </tr>
                                <tr class="text-center">
                                    <td>職稱</td>
                                    <td>工作小組</td>
                                </tr>
                                <tr class="text-center">
                                    <td>電子信箱</td>
                                    <td>${account.email}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6">
                    <jsp:include page="../components/announcement.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div><!-- /container -->
</body>
</html>
