<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<script>

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

function query(n) {
    url = "teacherCourseRecord?";
    if ($("#year").val() != "")
        url += "year=" + $("#year").val() + "&";
    if ($("#semester").val() != "")
        url += "semester=" + $("#semester").val() + "&";
    if ($("#classseName").val() != "")
        url += "className=" + $("#className").val() + "&";
    if ($("#courseName").val() != "")
        url += "courseName=" + $("#courseName").val() + "&";
    window.location = url.substr(0, url.length - 1);
}

$(document).ready(function () {
    $("#year").val(${year});
    $("#semester").val(${semester});
    $("#className").val("${className}");
    $("#courseName").val("${courseName}");
});

function view(n) {
    window.open('./teacherViewCourseRecord?courseno=' + n);
}


</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢課程學習成果
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="year" style="font-size: large" class="col-sm-2">學年</label>
                            <div class="col-sm-10">
                                <select id="year" name="year" class="form-control">
                                    <!--TODO 抓年度-->
                                    <option VALUE="">ALL</option>
                                    <option VALUE="103">103</option>
                                    <option VALUE="104">104</option>
                                    <option VALUE="105">105</option>
                                    <option VALUE="106">106</option>
                                    <option VALUE="107">107</option>
                                    <option VALUE="108">108</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="semester" style="font-size: large" class="col-sm-2">學期</label>
                            <div class="col-sm-10">
                                <select id="semester" name="semester" class="form-control">
                                    <!--TODO 抓當前學期-->
                                    <option VALUE="">ALL</option>
                                    <option VALUE="1">1</option>
                                    <option VALUE="2">2</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="className" style="font-size: large" class="col-sm-2">班別</label>
                            <div class="col-sm-10">
                                <input type="text" id="className" name="className" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="courseName" style="font-size: large" class="col-sm-2">科目</label>
                            <div class="col-sm-10">
                                <input type="text" id="courseName" name="courseName" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10"><input type="button" onclick="query()" value="查詢"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>課程學習成果
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">班別</th>
                        <th class="text-center">課名</th>
                        <th class="text-center">授課教師</th>
                        <th class="text-center">已送未驗</th>
                        <th class="text-center">檢視</th>
                    </tr>
                    <c:if test="${courseList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${courseList.size()-1}">
                            <tr>
                                <td>${courseList.get(i).term_year}</td>
                                <td>${courseList.get(i).term_sem}</td>
                                <td>${courseList.get(i).course_no}</td>
                                <td>${courseList.get(i).class_Cname}</td>
                                <td>${courseList.get(i).course_Cname}</td>
                                <td>${courseList.get(i).names}</td>
                                <td>${courseList.get(i).courseRecordList.size()}</td>
                                <td>
                                    <button onclick="view('${courseList.get(i).course_no}')" class="btn btn-warning">
                                        檢視
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <div id="pagination"></div>
            </div>

        </div>
    </div>
</div>

</body>
</html>
