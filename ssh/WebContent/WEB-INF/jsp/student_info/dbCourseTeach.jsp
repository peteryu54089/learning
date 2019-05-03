<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/10/2
  Time: 下午 04:22
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
    <script>
    function toogle(source) {
        checkboxes = document.getElementsByName('checkGroup[]');
        for (var i = 0, n = checkboxes.length; i < n; i++) {
            checkboxes[i].checked = source.checked;
        }
    }

    function confirmCheck() {
        var r = confirm("確定刪除?");
        if (r == true) {
            return true;
        } else
            return false;
    }

    function selectOnChange(index) {
        window.location = "dbManage?type=" + index;
    }
    </script>
</head>

<body>
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
                        <div class="form-group">
                            <select class="form-control" id="year" name="year">
                                <option value="">不拘學年度</option>
                                <c:forEach var="i" begin="90" end="${max_year}">
                                    <option value="${i}"
                                            <c:if test="${i == year}">selected</c:if> >${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <select class="form-control" id="sem" name="sem">
                                <option value="">不拘學年期</option>
                                <option value="1" <c:if test="${1 == sem}">selected</c:if>>上學期
                                </option>
                                <option value="2" <c:if test="${2 == sem}">selected</c:if>>下學期
                                </option>
                            </select>
                        </div>
                        <div class="input-group input-group-md">
                            <input type="text" class="form-control" name="num" id="num"
                                   placeholder="關鍵字(可留空)"
                                   value="${num}">
                            <div class="input-group-btn">
                                <button class="btn btn-default" type="submit"><i
                                        class="glyphicon glyphicon-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form><!-- /container -->
    <br/>
    <div class="col-sm-12">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>查詢結果</div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th><input type="checkbox" onClick="toogle(this)"/></th>
                        <th>學年度</th>
                        <th>學年期</th>
                        <th>開課班級</th>
                        <th>課程代碼</th>
                        <th>中文課程名稱</th>
                        <th>職員編號</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <form action="dbCourseTeachDeleteBatch" method="POST">
                        <c:forEach var="course" items="${courseList}">
                        <tr>
                            <td>
                                <input type="checkbox" name="checkGroup[]"
                                       value="${course.term_year},${course.term_sem},${course.class_cname},${course.course_num},${course.memberId}"/>
                            </td>
                            <td>${course.term_year}</td>
                            <td>${course.term_sem}</td>
                            <td>${course.class_cname}</td>
                            <td>${course.course_num}</td>
                            <td>${course.course_cname}</td>
                            <td><a href="dbManage?type=staff&num=${course.memberId}">${course.memberId}</a></td>
                            <td>
                                <a href="dbCourseSelectTeach?year=${course.term_year}&sem=${course.term_sem}&num=${course.course_num}&reg=${course.memberId}"
                                   class="btn-danger btn-xs" authority="button"
                                   onclick="return confirmCheck()">刪除</a>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                    <button type="submit" class="btn btn-danger btn-xs"
                            onclick="return confirmCheck()">刪除所選資料
                    </button>
                    </form>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>