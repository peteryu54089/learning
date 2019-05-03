<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/23
  Time: 下午 08:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-wrench" aria-hidden="true">　</span>帳號權限管理</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">編號</th>
                        <th class="text-center">管理者</th>
                        <th class="text-center">編輯權限</th>
                        <th class="text-center">設定人員權限</th>
                        <th class="text-center">.....</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>俞黃淞</td>
                        <td>
                            <button class="btn btn-warning" onclick="location.href='/editAuthority'">編輯權限</button>
                        </td>
                        <td>
                            <button class="btn btn-info" onclick="location.href='/setAuthority'">設定人員權限</button>
                        </td>
                        <td>
                            <button class="btn btn-success">...</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
