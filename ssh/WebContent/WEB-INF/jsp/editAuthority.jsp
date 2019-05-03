<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/25
  Time: 上午 09:15
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
        <div class="panel panel-info">
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>帳號權限管理(校管理者)
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="editAuthority" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="id" style="font-size: large">請輸入人員資訊</label>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="id" class="col-sm-2" style="font-size: large">代號</label>
                                <div class="col-sm-10">
                                    <input type="text" id="id" name="id" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="name" class="col-sm-2" style="font-size: large">姓名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-2"></div>
                                <div class="col-sm-10">
                                    <button class="btn btn-warning">確認</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>......</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">勾選</th>
                        <th class="text-center">代號</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">身分</th>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="authority" name="authority"></td>
                        <td>101010</td>
                        <td>Shawn</td>
                        <td>
                            <button class="btn btn-primary">帳號管理者</button>
                        </td>
                    </tr>
                </table>
                <button class="btn btn-warning">選擇</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-file" aria-hidden="true">　</span>......</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">序次</th>
                        <th class="text-center">代號</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>101010</td>
                    </tr>
                </table>
                <button class="btn btn-success" onclick="location.href='/accountManage'">儲存返回</button>
                <button class="btn btn-info">確認</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
