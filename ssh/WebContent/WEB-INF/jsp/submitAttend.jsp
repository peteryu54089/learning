<%--@elvariable id="isNight" type="java.lang.Boolean"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading">提交出缺勤紀錄</div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-12">

                            <form action="SubmitAttend" method="POST" class="form form-inline">
                                <div class="form-group">
                                    <label>出缺勤紀錄上傳資料下載</label>
                                    <label for="course_year_sem">學期</label>&nbsp;&nbsp;
                                    <input name="course_year_sem"
                                           id="course_year_sem" type="text" required class="form-control"
                                           style="display: inline-block; width: auto;"
                                           pattern="^\d{2,3}[12]$"
                                           value="1031"/>
                                </div>

                                <div class="form-group">
                                    <button class="btn btn-primary" type="submit">下載</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /container -->
</div>
</body>
</html>
