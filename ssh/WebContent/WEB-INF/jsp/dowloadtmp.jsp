<%--
  Created by IntelliJ IDEA.
  User: Victor
  Date: 2017/9/20
  Time: 下午 03:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>

    <link rel="stylesheet" href="resources/css/tableFilter.css">
    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/tableFilter.js"></script>


</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>

    <div class="row">

        <section class="content">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="table-striped">
                            <table class="table table-filter">
                                <tbody>
                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/課程欄位範本.xlsx">課程欄位範本下載</a>
                                        <a href="resources/tempFile/課程欄位範本.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/已選課程範本.xlsx">已選課程範本下載</a>
                                        <a href="resources/tempFile/已選課程範本.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/教師資料上傳範例.xlsx">教師資料範本下載</a>
                                        <a href="resources/tempFile/教師資料上傳範例.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/教師課程範本.xlsx">教師課程範本下載</a>
                                        <a href="resources/tempFile/教師課程範本.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/學生資料上傳範本.xlsx">學生資料上傳範本下載</a>
                                        <a href="resources/tempFile/學生資料上傳範本.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>

                                <tr class="clickable-row">
                                    <td>
                                        <a href="getFinalExcel">
                                            下載暨南大學資料 <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>

                                <tr class="clickable-row">
                                    <td>
                                        <a href="resources/tempFile/校內幹部經歷紀錄.xlsx">校內幹部經歷紀錄下載</a>
                                        <a href="resources/tempFile/校內幹部經歷紀錄.xlsx">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </div>
        </section>

    </div>

</div>

</body>
</html>
