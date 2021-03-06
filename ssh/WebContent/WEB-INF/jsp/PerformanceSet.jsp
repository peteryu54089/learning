<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/25
  Time: 上午 11:53
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
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>多元表現提交設定
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1"></div>
                        <div class="col-sm-11">
                            <label for="text1" style="font-size: large" class="col-sm-2">修改項目</label>
                            <label for="text1" style="font-size: large" class="col-sm-2">修改內容簡述</label>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <form name="myForm" action="CoursePerSet" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-11">
                                <label for="text1" style="font-size: large" class="col-sm-2">text1</label>
                                <div class="col-sm-10">
                                    <input type="text" id="text1" name="text1" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-11">
                                <label for="text2" style="font-size: large" class="col-sm-2">text2</label>
                                <div class="col-sm-10">
                                    <input type="text" id="text2" name="text2" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-11">
                                <label for="text3" style="font-size: large" class="col-sm-2">text3</label>
                                <div class="col-sm-10">
                                    <input type="text" id="text3" name="text3" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-11">
                                <label for="text3" style="font-size: large" class="col-sm-2">....</label>
                                <div class="col-sm-10">
                                    <button class="btn btn-success">button1</button>
                                    <button class="btn btn-primary">button2</button>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-11">
                                <div class="col-sm-2"></div>
                                <div class="col-sm-10"><input type="submit" class="btn btn-warning" value="儲存確認"/></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
