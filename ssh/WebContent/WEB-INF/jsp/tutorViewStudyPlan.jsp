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
    function download(a, b) {
        window.location.href = './downloadStudyPlan?no=' + a + '&id=' + b;
    }

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
    </div>
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>自傳</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年度</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">標題</th>
                        <th class="text-center" width="30%">內容簡述</th>
                        <th class="text-center">上傳時間</th>
                        <th class="text-center">學習計畫檔案</th>
                        <th class="text-center">副檔案</th>
                    </tr>
                    <c:if test="${studyPlanList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${studyPlanList.size()-1}">
                            <tr>
                                <td>${studyPlanList.get(i).term_year}</td>
                                <td>${studyPlanList.get(i).term_semester}</td>
                                <td>${studyPlanList.get(i).topic}</td>
                                <td>${studyPlanList.get(i).description}</td>
                                <td>${studyPlanList.get(i).getCreatedDateString()}</td>
                                <td>
                                    <a href="${studyPlanList.get(i).main_link}">
                                            ${studyPlanList.get(i).main_uploadFile.fileName}

                                </td>
                                <td>
                                    <a href="${studyPlanList.get(i).sub_link}">
                                            ${studyPlanList.get(i).sub_uploadFile.fileName}
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
</html>
