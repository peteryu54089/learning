<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    url = "tutorViewCourseResult?rgno=${rgno}&";
    if ($("#year").val() != "")
        url += "year=" + $("#year").val() + "&";
    if ($("#semester").val() != "")
        url += "semester=" + $("#semester").val() + "&";
    if ($("#courseName").val() != "")
        url += "courseName=" + $("#courseName").val() + "&";
    window.location = url.substr(0, url.length - 1);
}

$(document).ready(function () {
    $("#year").val(${year});
    $("#semester").val(${semester});
    $("#courseName").val("${courseName}");
});
</script>
<body>
<div class="container">
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>學生資料</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">班級</th>
                        <th class="text-center">座號</th>
                        <th class="text-center">學號</th>
                        <th class="text-center">姓名</th>
                    </tr>
                    <c:if test="${student!=null}">
                        <tr>
                            <td>${student.className}</td>
                            <td>${student.class_no}</td>
                            <td>${student.regNumber}</td>
                            <td>${student.stu_Cname}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢修課成績
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
                            <div class="col-sm-10"><input type="button" onclick="query()" class="btn btn-warning"
                                                          value="查詢"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>修課成績</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">科目名稱</th>
                        <th class="text-center">授課教師</th>
                        <th class="text-center">一段</th>
                        <th class="text-center">二段</th>
                        <th class="text-center">期末</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">補修</th>
                    </tr>
                    <c:if test="${courseResultList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${courseResultList.size()-1}">
                            <tr>
                                <td>${courseResultList.get(i).year}</td>
                                <td>${courseResultList.get(i).sem}</td>
                                <td>${courseResultList.get(i).course_no}</td>
                                <td>${courseResultList.get(i).course_Cname}</td>
                                <td>${courseResultList.get(i).staff_Cnames}</td>
                                <td>${courseResultList.get(i).p1_score}</td>
                                <td>${courseResultList.get(i).p2_score}</td>
                                <td>${courseResultList.get(i).final_score}</td>
                                <td>
                                    <c:if test="${courseResultList.get(i).sem_star_sign != null}">
                                    <font color="#FF0000">
                                        </c:if>
                                            ${courseResultList.get(i).sem_score}
                                        <c:if test="${courseResultList.get(i).sem_star_sign != null}">
                                    </font>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${courseResultList.get(i).makeup_star_sign != null}">
                                    <font color="#FF0000">
                                        </c:if>
                                            ${courseResultList.get(i).makeup_score}
                                        <c:if test="${courseResultList.get(i).makeup_star_sign != null}">
                                    </font>
                                    </c:if>
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
