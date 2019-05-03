<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/8/3
  Time: 下午 09:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>

    <link rel="stylesheet" href="resources/css/tableFilter.css">
    <script src="resources/javascript/jquery-ui.js"></script>
    <script src="resources/javascript/tableFilter.js"></script>

    <script>
    jQuery(document).ready(function ($) {
        $(".clickable-row").click(function () {
            window.location = $(this).data("href");
        });
    });
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>

    <div class="row">
        <section class="content">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <p>注意: 此頁面的資料在每學年末由學校代為上傳。</p>
                    <div class="panel-body">
                        <div class="pull-right">
                            <div class="btn-group">
                                <button type="button" class="btn btn-filter" data-target="profit">課程成果
                                </button>
                                <button type="button" class="btn btn-success btn-filter" data-target="cadre">外校幹部經歷
                                </button>
                                <button type="button" class="btn btn-warning btn-filter" data-target="competition">競賽成果
                                </button>
                                <button type="button" class="btn btn-danger btn-filter" data-target="license">檢定證照
                                </button>
                                <button type="button" class="btn btn-primary btn-filter" data-target="volunteer">志工服務
                                </button>
                                <button type="button" class="btn btn-info btn-filter" data-target="other">其它</button>
                                <button type="button" class="btn btn-default btn-filter" data-target="all">全部</button>
                            </div>
                        </div>
                        <div class="table-striped">
                            <table class="table table-filter">
                                <tbody>
                                <c:forEach items="${profitList}" var="profit">
                                    <tr data-status="profit" class="clickable-row"
                                        data-href="profit?year=${profit.term_year}&sem=${profit.term_sem}&code=${profit.course_num}">
                                        <td>
                                            <div>
                                                <div class="media">
                                                    <div class="media-body">
                                                        <span class="media-meta pull-right">${profit.term_year}${profit.term_sem}</span>
                                                        <h4 class="title">
                                                            <c:out value="${profit.term_year}${profit.term_sem}_${profit.course_cname}"></c:out>
                                                            <span class="pull-right profit">(課程成果)</span>
                                                        </h4>
                                                        <p class="summary"><c:out
                                                                value="${profit.document}"></c:out></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:forEach items="${cadreList}" var="cadre">
                                    <tr data-status="cadre" class="clickable-row" data-href="cadre?id=${cadre.id}">
                                        <td>
                                            <div class="media">
                                                <div class="media-body">
                                                    <span class="media-meta pull-right"><c:out
                                                            value="${cadre.term}(${cadre.startTime})"></c:out></span>
                                                    <h4 class="title">
                                                            ${cadre.unit}
                                                        <span class="pull-right cadre">(幹部經歷)</span>
                                                    </h4>
                                                    <p class="summary"><c:out value="${cadre.document}"></c:out></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:forEach items="${competitionList}" var="competition">
                                    <tr data-status="competition" class="clickable-row"
                                        data-href="competition?id=${competition.id}">
                                        <td>
                                            <div class="media">
                                                <a href="#" class="pull-left">
                                                </a>
                                                <div class="media-body">
                                                    <span class="media-meta pull-right"><c:out
                                                            value="(${competition.time})"></c:out></span>
                                                    <h4 class="title">
                                                        <c:out value="${competition.name}(${competition.item}) - ${competition.award}"></c:out>
                                                        <span class="pull-right competition">(競賽成果)</span>
                                                    </h4>
                                                    <p class="summary"><c:out
                                                            value="${competition.document}"></c:out></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:forEach items="${licenseList}" var="license">
                                    <tr data-status="license" class="clickable-row"
                                        data-href="license?id=${license.id}">
                                        <td>
                                            <div class="media">
                                                <a href="#" class="pull-left">
                                                </a>
                                                <div class="media-body">
                                                    <span class="media-meta pull-right"><c:out
                                                            value="${license.time}"></c:out></span>
                                                    <h4 class="title">
                                                        <c:out value="${license.code}"></c:out>
                                                        <span class="pull-right license">(檢定證照記錄)</span>
                                                    </h4>
                                                    <p class="summary"><c:out value="${license.document}"></c:out></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:forEach items="${volunteerList}" var="volunteer">
                                    <tr data-status="volunteer" class="clickable-row"
                                        data-href="volunteer?id=${volunteer.id}">
                                        <td>
                                            <div class="media">
                                                <a href="#" class="pull-left">
                                                </a>
                                                <div class="media-body">
                                                    <span class="media-meta pull-right"><c:out
                                                            value="${volunteer.startTime}"></c:out></span>
                                                    <h4 class="title">
                                                        <c:out value="${volunteer.name} (${volunteer.unit}) ${volunteer.count} hours"></c:out>
                                                        <span class="pull-right volunteer">(志工服務紀錄)</span>
                                                    </h4>
                                                    <p class="summary"><c:out value="${volunteer.document}"></c:out></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:forEach items="${otherList}" var="other">
                                    <tr data-status="other" class="clickable-row" data-href="other?id=${other.id}">
                                        <td>
                                            <div class="media">
                                                <a href="#" class="pull-left">
                                                </a>
                                                <div class="media-body">
                                                    <span class="media-meta pull-right"><c:out
                                                            value="${other.startTime}"></c:out></span>
                                                    <h4 class="title">
                                                        <c:out value="${other.name} (${other.unit})"></c:out>
                                                        <span class="pull-right other">(其它記錄)</span>
                                                    </h4>
                                                    <p class="summary"><c:out value="${other.document}"></c:out></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </div>
        </section>

    </div>

</div>
</body>
</html>
