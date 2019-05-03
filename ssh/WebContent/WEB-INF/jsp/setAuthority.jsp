<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/25
  Time: 上午 09:46
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
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>......</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">序次</th>
                        <th class="text-center">代號</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">附屬學生</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>101010</td>
                        <td>Shawn</td>
                        <td>
                            <button class="btn btn-primary">預覽名單</button>
                            <button class="btn btn-danger">設定人員xx</button>
                        </td>
                    </tr>
                </table>
                <button class="btn btn-warning" onclick="location.href='/accountManage'">返回</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
