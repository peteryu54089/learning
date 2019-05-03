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

    $(function () {
        $('#editManagerForm').on('submit', function (e) {
            e.preventDefault();
            $.ajax({
                type: 'post',
                url: 'editManager',
                data: $('#editManagerForm').serialize(),
                success: function (data) {
                    alert(data);
                    $('#myModal').modal('hide');
                }
            });
        });
    });
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
                        <th>帳號</th>
                        <th>密碼</th>
                        <th>職員編號</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <form action="dbManagerDeleteBatch" method="POST">
                        <c:forEach var="manager" items="${managerList}">
                        <tr>
                            <td>
                                <input type="checkbox" name="checkGroup[]"
                                       value="${manager.account}"/>
                            </td>
                            <td>${manager.account}</td>
                            <td>*******</td>
                            <td>${manager.staffCode}</td>
                            <td>
                                <a href="dbStaffDelete?code=${manager.staffCode}"
                                   class="btn-danger btn-xs" authority="button"
                                   onclick="return confirmCheck()">刪除</a>
                                <button type="button" class="btn-info btn-xs" data-toggle="modal" data-target="#myModal"
                                        authority="button" onclick="<c:set var="account" value="${manager.account}" />">
                                    編輯
                                </button>
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
                <!-- Modal -->
                <div class="modal fade" id="myModal" authority="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">編輯帳號</h4>
                            </div>
                            <div class="modal-body">
                                <form id="editManagerForm">
                                    <div class="form-group">
                                        <label for="acc">欲修改帳號:</label>
                                        <input type="text" name="acc" id="acc" value="${account}" class="form-control"
                                               readonly>
                                    </div>
                                    <div class="form-group">
                                        <label for="pass">欲修改密碼:</label>
                                        <input type="password" name="pass" id="pass" value="**********************"
                                               class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <label for="pass2">確認欲修改密碼:</label>
                                        <input type="password" name="pass2" id="pass2" value="**********************"
                                               class="form-control">
                                    </div>
                                    <button type="submit" class="btn btn-default">送出</button>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
