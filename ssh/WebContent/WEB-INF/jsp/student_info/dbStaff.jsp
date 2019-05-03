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
                        <th>職員編號</th>
                        <th>職員中文姓名</th>
                        <th>職員英文姓名</th>
                        <th>所屬系別</th>
                        <th>職稱</th>
                        <th>聯絡地址</th>
                        <th>聯絡電話</th>
                        <th>電子信箱</th>
                        <th>身分證</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <form action="dbStaffDeleteBatch" method="POST">
                        <c:forEach var="staff" items="${staffList}">
                        <tr>
                            <td>
                                <input type="checkbox" name="checkGroup[]"
                                       value="${staff.staffCode}"/>
                            </td>
                            <td>${staff.staffCode}</td>
                            <td>${staff.staff_cname}</td>
                            <td>${staff.staff_ename}</td>
                            <td>${staff.divCname}</td>
                            <td>${staff.job_cname}</td>
                            <td>${staff.address}</td>
                            <td>${staff.phone}</td>
                            <td>${staff.email}</td>
                            <td>${staff.idno}</td>
                            <td>
                                <a href="dbStaffDelete?code=${staff.staffCode}"
                                   class="btn-danger btn-xs" authority="button"
                                   onclick="return confirmCheck()">刪除</a>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                    <button type="submit" class="btn btn-danger btn-xs"
                            onclick="return confirmCheck()">
                        刪除所選資料
                    </button>
                    </form>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
