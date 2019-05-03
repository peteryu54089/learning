<%--
  Created by IntelliJ IDEA.
  User: Victor
  Date: 2017/9/5
  Time: 下午 03:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>高中生歷程系統</title>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">
</head>
<body>

<script>
function selectOnChange(index) {
    window.location = "dbManage?type=" + index;
}
</script>

<div class="container">
    <form action="" method="GET">
        <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
        <div class="col-sm-12">
            <div class="col-sm-12">
                <div class="panel panel-danger">
                    <div class="panel-heading"><span class="glyphicon glyphicon-circle-arrow-down"
                                                     aria-hidden="true">　</span>查詢條件
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="type">分類</label>
                            <select class="form-control" id="type" name="type"
                                    onchange="selectOnChange(this.options[this.selectedIndex].value)">
                                <option value="course" <c:if
                                        test="${type == 'course'}"> selected</c:if>>課程資料管理
                                </option>
                                <option value="select" <c:if
                                        test="${type == 'select'}"> selected</c:if>>學生選課資料管理
                                </option>
                                <option value="teach" <c:if
                                        test="${type == 'teach'}"> selected</c:if>>教師授課資料管理
                                </option>
                                <option value="staff" <c:if
                                        test="${type == 'staff'}"> selected</c:if>>教職員資料管理
                                </option>
                                <option value="reg" <c:if test="${type == 'reg'}"> selected</c:if>>
                                    學生註冊資料管理
                                </option>
                                <option value="manage" <c:if
                                        test="${type == 'manage'}"> selected</c:if>>管理員資料管理
                                </option>
                            </select>
                        </div>
                        <c:if test="${type == 'course'}">
                            <jsp:include page="dbCourse.jsp"></jsp:include>
                        </c:if>
                        <c:if test="${type == 'select'}">
                            <jsp:include page="dbCourseSelect.jsp"></jsp:include>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </form><!-- /container -->
</div>
</body>
</html>
