<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css">
    <script src="resources/javascript/jquery-1.11.2.min.js"></script>
    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/datepicker-zh-TW.js"></script>
    <script src="resources/javascript/dateTth.js"></script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>新增課程諮詢紀錄
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="selectcourse" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="adviseType" style="font-size: large" class="col-sm-2">類型</label>
                                <div class="col-sm-10">
                                    <select id="adviseType" name="adviseType" class="form-control">
                                        <option>個別</option>
                                        <option>團體</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="startTime" style="font-size: large" class="col-sm-2">日期</label>
                                <div class="col-sm-4"><input type="text" id="startTime" name="startTime"
                                                             class="min_go_date form-control"></div>
                                <div class="col-sm-2" align="center">~<span style="text-align: center"></span></div>
                                <div class="col-sm-4"><input type="text" id="endTime" name="endTime"
                                                             class="min_go_date form-control"></div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="topic" style="font-size: large" class="col-sm-2">主題</label>
                                <div class="col-sm-10">
                                    <input type="topic" id="topic" name="topic" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-2"></div>
                                <div class="col-sm-10"><input type="submit" class="btn btn-warning" value="新增"></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>課程諮詢紀錄
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">Label1</th>
                        <th class="text-center">Label2</th>
                        <th class="text-center">Label3</th>
                        <th class="text-center">Label4</th>
                        <th class="text-center">Label5</th>
                        <th class="text-center">Label6</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>2</td>
                        <td>3</td>
                        <td>4</td>
                        <td>5</td>
                        <td>6</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
