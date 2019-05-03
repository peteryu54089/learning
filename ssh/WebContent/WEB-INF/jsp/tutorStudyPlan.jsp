<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/25
  Time: 下午 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<script>

function query() {
    url = "tutorStudyPlan?";
    if ($("#class").val() != "")
        url += "class=" + $("#class").val() + "&";
    if ($("#regno").val() != "")
        url += "regno=" + $("#regno").val() + "&";
    if ($("#name").val() != "")
        url += "name=" + $("#name").val() + "&";
    window.location = url.substr(0, url.length - 1);
}

function view(n) {
    window.open('./tutorViewStudyPlan?rgno=' + n);
}

$(document).ready(function () {
    $("#class").val(${clscode});
    $("#regno").val("${regno}");
    $("#name").val("${name}");
});

url = "?page=";
if (location.href.split("?page")[0].split("?").length >= 2) {
    url = "?" + location.href.split('?')[1].split('&page')[0] + "&page=";
}
$(function () {
    $('#pagination').pagination({
        pages:${pageAmount},
        hrefTextPrefix: url,
        currentPage:${page},
        edges: 2,
        cssStyle: 'light-theme',
        ellipsePageSet: false
    });
});
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>檢閱學生學習計畫
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="class" style="font-size: large" class="col-sm-2">班級</label>
                            <div class="col-sm-10">
                                <select id="class" name="class" class="form-control">
                                    <option VALUE="">ALL</option>
                                    <c:if test="${CUnitList.size() > 0}">
                                        <c:forEach var="i" begin="0" end="${CUnitList.size()-1}">
                                            <option VALUE=${CUnitList.get(i).cls_code}>${CUnitList.get(i).cunit_name}</option>
                                        </c:forEach>
                                    </c:if>

                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="regno" style="font-size: large" class="col-sm-2">學號</label>
                            <div class="col-sm-10">
                                <input type="text" id="regno" name="regno" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="name" style="font-size: large" class="col-sm-2">姓名</label>
                            <div class="col-sm-10">
                                <input type="text" id="name" name="name" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10"><input type="button" onclick="query()" class="btn btn-warning"
                                                          value="查詢"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>學習計畫</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">班級</th>
                        <th class="text-center">座號</th>
                        <th class="text-center">學號</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">操作</th>
                    </tr>
                    <c:if test="${StudentList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${StudentList.size()-1}">
                            <tr>
                                <td>${StudentList.get(i).className}</td>
                                <td>${StudentList.get(i).class_no}</td>
                                <td>${StudentList.get(i).regNumber}</td>
                                <td>${StudentList.get(i).stu_Cname}</td>
                                <td>
                                    <button onclick="view('${StudentList.get(i).rgno}')" class="btn btn-warning">
                                        檢視
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </div>
            <div id="pagination"></div>
        </div>
    </div>
</div>

</body>
