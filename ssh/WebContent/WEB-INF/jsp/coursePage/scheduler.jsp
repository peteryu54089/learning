<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>高中生歷程系統</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">
</head>
<body>

<script>
function selectOnChange(index) {
    window.location = "performance?type=" + index;
}
</script>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="col-sm-12">
        <div class="col-sm-4">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-circle-arrow-down"
                                                 aria-hidden="true">　</span>選項
                </div>
                <div class="panel-body">
                    <form action="" method="GET">
                        <div class="form-group">
                            <label for="year">選擇學期</label>
                            <select class="form-control" id="year" name="year" onchange="this.form.submit()">
                                <c:forEach var="i" begin="90" end="${max_year}">
                                    <option value="${i}"
                                            <c:if test="${i == year}">selected</c:if> >${i}</option>
                                </c:forEach>
                            </select>
                            <label for="sem">選擇學年度</label>
                            <select class="form-control" id="sem" name="sem" onchange="this.form.submit()">
                                <option value="1" <c:if test="${1 == sem}">selected</c:if>>上學期</option>
                                <option value="2" <c:if test="${2 == sem}">selected</c:if>>下學期</option>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="panel panel-info">
                <div class="panel-heading"><span
                        class="glyphicon glyphicon-exclamation-sign">　</span>${term_year}學年度第${term_sem}學期${cname}課表
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <tr class="text-center">
                            <td>開課學年學期</td>
                            <td>開課班級</td>
                            <td>科目名稱</td>
                            <td>成果狀態</td>
                        </tr>
                        <c:forEach items="${courseList}" var="course">
                            <tr class="text-center">
                                <td>${course.term_year}-${course.term_sem}</td>
                                <td>${course.class_cname}</td>
                                <td>${course.course_cname}</td>
                                <td>
                                    <a href="profit?year=${course.term_year}&sem=${course.term_sem}&code=${course.course_num}">檢視課程成果</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${fn:length(courseList) == 0}">
                            <tr class="text-center">
                                <td colspan="4">查無資料</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div><!-- /container -->
</div>
</body>
</html>
