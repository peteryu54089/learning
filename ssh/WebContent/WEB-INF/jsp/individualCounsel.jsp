<%--@elvariable id="individualCounselList" type="java.util.List<model.IndividualCounsel>"--%>
<%--@elvariable id="fileMap" type="java.util.HashMap<java.lang.Integer,java.lang.String>"--%>
<%--@elvariable id="studentMap" type="java.util.HashMap<java.lang.Integer,model.role.Student>"--%>
<%@ page import="util.DateUtils" %>

<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>

    <script>
    function deleteIndividualCounsel(id) {
        let r = confirm('確認刪除?');
        if (r === false)
            return false;
        $.ajax({
            type: "POST",
            url: "DeleteIndividualCounsel",
            data: {
                id: id
            },
            success: function (result) {
                alert(result);
                location.reload();
            }
        })
    }

    function edit(id) {
        window.location.href = "UploadIndividualCounsel?id=" + id;
    }

    function upload() {
        $.ajax({
            type: "POST",
            url: "IndividualCounsel",
            complete: function () {
                window.location.href = "UploadIndividualCounsel";
            }
        });
        return false;
    }
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢個別諮詢管理
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="IndividualCounsel" enctype='multipart/form-data' method="GET">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="startDate" style="font-size: large" class="col-sm-3">日期起</label>
                                <div class="col-sm-6">
                                    <input type="date" id="startDate" name="startDate" class="form-control"
                                           placeholder="請點擊選擇年月日">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="endDate" style="font-size: large" class="col-sm-3">日期訖</label>
                                <div class="col-sm-6">
                                    <input type="date" id="endDate" name="endDate" class="form-control"
                                           placeholder="請點擊選擇年月日">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="title" style="font-size: large" class="col-sm-3">標題</label>
                                <div class="col-sm-6">
                                    <input type="text" id="title" name="title" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
                                    <input type="submit" class="btn btn-warning" value="查詢">
                                    <button class="btn btn-success" type="button" onclick="upload()">上傳</button>
                                    <a class="btn btn-primary" href="./IndividualCounsel?action=export">匯出</a>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
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
            <div class="panel-heading"><span class="glyphicon glyphicon-th-list" aria-hidden="true">　</span>個別諮詢管理</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center" width="7%">班級</th>
                        <th class="text-center" width="7%">座號</th>
                        <th class="text-center" width="7%">姓名</th>
                        <th class="text-center" width="14%">諮詢日期</th>
                        <th class="text-center" width="14%">諮詢標題</th>
                        <th class="text-center" width="14%">諮詢檔案</th>
                        <th class="text-center" width="15%">操作</th>
                    </tr>
                    <c:forEach items="${individualCounselList}" var="individualCounsel" varStatus="status">
                        <tr class="text-center">
                            <td>${studentMap.get(individualCounsel.rgno).className}</td>
                            <td>${studentMap.get(individualCounsel.rgno).class_no}</td>
                            <td>${studentMap.get(individualCounsel.rgno).stu_Cname}</td>
                            <td>${DateUtils.formatDateTime(individualCounsel.startTime)}～${DateUtils.formatDateTime(individualCounsel.endTime)}</td>
                            <td>${individualCounsel.title}：${individualCounsel.description}</td>
                            <td>
                                <a href="${fileMap.get(individualCounsel.fileId)}">${individualCounsel.originalFilename}</a>
                            </td>
                            <td>
                                <button id="edit" value="${individualCounsel.id}" type="button"
                                        class="btn btn-primary btn-sm"
                                        onclick="edit(${individualCounsel.id})">
                                    編輯
                                </button>
                                <button id="delete" value="${individualCounsel.id}" type="button"
                                        class="btn btn-danger btn-sm"
                                        onclick="deleteIndividualCounsel(${individualCounsel.id})">
                                    刪除
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(individualCounselList) == 0}">
                        <tr class="text-center">
                            <td colspan="9">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>

