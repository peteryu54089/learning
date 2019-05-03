<%--
  Created by IntelliJ IDEA.
  User: Victor
  Date: 2017/9/19
  Time: 上午 09:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>高中生歷程系統</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
          id="bootstrap-css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="resources/css/tableFilter.css">
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">

    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/tableFilter.js"></script>

    <script>
    function validate() {
        var parts = x.split('.');
        var ext = parts[parts.length - 1];
        if (ext.toLowerCase() != "xlsx" && ext.toLowerCase() != "xls") {
            alert("請選擇正確的檔案格式");
            return false;
        }
    }
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>

    <div class="row">

        <section class="content">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="myForm" action="stuDataUpload" enctype='multipart/form-data'
                              onsubmit="return validate()" method="POST">
                            <h3>選擇學生歷程檔案</h3>

                            <div class="alert alert-info" authority="alert">
                                <strong>上傳格式為excel檔</strong>
                                <select name="type" class=".btn-success">
                                    <option>---請選擇上傳檔案類型---</option>
                                    <option value="vcourse">課程欄位範本</option>
                                    <option value="vcourse_select">已選課程範本</option>
                                    <option value="vcourse_teaching">教師課程範本</option>
                                    <option value="vstaff">教師資料上傳範本</option>
                                    <option value="vstu">學生資料上傳範本</option>
                                    <option value="president">校內幹部經歷紀錄</option>
                                </select>
                            </div>
                            <input type="file" class="form-control" id="document" name="document" required>
                            <br/>
                            <c:if test="${result=='success'}">
                                <div class="alert alert-success">
                                    <strong>資料上傳成功</strong>
                                </div>
                            </c:if>
                            <c:if test="${result=='repeat'}">
                                <div class="alert alert-danger">
                                    <strong>資料重複上傳</strong>
                                </div>
                            </c:if>
                            <c:if test="${result=='format'}">
                                <div class="alert alert-danger">
                                    <strong>格式錯誤</strong>
                                </div>
                            </c:if>
                            <c:if test="${result=='fail'}">
                                <div class="alert alert-danger">
                                    <strong>系統發生錯誤，若有疑問請洽系統管理</strong>
                                </div>
                            </c:if>
                            <input type="submit" class="btn btn-info" value="送出">
                        </form>
                    </div>
                </div>
            </div>
        </section>

    </div>

</div>

</body>
</html>
