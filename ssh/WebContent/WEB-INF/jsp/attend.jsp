<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/26
  Time: 下午 02:31
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
    url = "attend?";
    if ($("#year").val() != "")
        url += "year=" + $("#year").val() + "&";
    if ($("#semester").val() != "")
        url += "semester=" + $("#semester").val() + "&";
    if ($("#aType").val() != "")
        url += "aType=" + $("#aType").val() + "&";
    window.location = url.substr(0, url.length - 1);
}

$(document).ready(function () {
    $("#year").val(${year});
    $("#semester").val(${semester});
    $("#aType").val("${aType}");
});
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>出缺勤紀錄明細
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <!--
                    <form name="myForm" action="attend" enctype='multipart/form-data' method="POST">
                       -->
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="year" style="font-size: large" class="col-sm-2">年度</label>
                            <div class="col-sm-10">
                                <select id="year" name="year" class="form-control">
                                    <!--TODO 抓年度-->
                                    <option VALUE="">ALL</option>
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
                            <label for="atype" style="font-size: large" class="col-sm-2">假別</label>
                            <div class="col-sm-10">
                                <select id="aType" name="aType" class="form-control">
                                    <!--TODO 抓假別-->
                                    <option VALUE="">ALL</option>
                                    <c:if test="${arCodeList.size() > 0}">
                                        <c:forEach var="i" begin="0" end="${arCodeList.size()-1}">
                                            <option VALUE="${arCodeList.get(i).ar_code}">${arCodeList.get(i).ar_cname}</option>
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
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10"><input type="button" onclick="query()" class="btn btn-warning"
                                                          value="查詢"></div>
                        </div>
                    </div>
                    <!--
                 </form>
                 -->
                </div>
            </div>
        </div>
    </div>
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>出缺勤紀錄</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">年度</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">班級</th>
                        <th class="text-center">座號</th>
                        <th class="text-center" style="width: 20%">日期</th>
                        <th class="text-center" style="width: 20%">假別</th>
                        <th class="text-center" style="width: 15%">節數</th>
                    </tr>
                    <c:if test="${attendList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${attendList.size()-1}">
                            <tr>
                                <td>${attendList.get(i).sbj_year}</td>
                                <td>${attendList.get(i).sbj_sem}</td>
                                <td>${student.className}</td>
                                <td>${student.class_no}</td>
                                <td>${attendList.get(i).getDateString()}</td>
                                <td>${attendList.get(i).ar_Cname}</td>
                                <td>${attendList.get(i).sprd_name}</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <div class="panel-body">總節數:${count} <%--資料更新日期:${lastUpdateDate} 更新頻率:${UpdateInterval} 天--%></div>
                <div id="pagination"></div>

                <button class="btn btn-primary" onclick="location.href='<%=request.getContextPath()%>/calendar'">出缺曠行事曆</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
