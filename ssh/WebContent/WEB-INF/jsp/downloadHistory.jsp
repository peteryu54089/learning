<%--
  Created by IntelliJ IDEA.
  User: Ching-Yun Yu
  Date: 2018/6/6
  Time: 上午 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>學習歷程檔案下載
            </div>
            <div class="panel-body">學生課程學習成果紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">term_year</th>
                        <th class="text-center">term_sem</th>
                        <th class="text-center">course_num</th>
                        <th class="text-center">course_cname</th>
                        <th class="text-center">content</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${profitList}" var="profit" varStatus="status">
                        <tr class="text-center">
                            <td>${profit.id}</td>
                            <td>${profit.idno}</td>
                            <td>${profit.term_year}</td>
                            <td>${profit.term_sem}</td>
                            <td>${profit.course_num}</td>
                            <td>${profit.course_cname}</td>
                            <td>${profit.content}</td>
                            <td>${profit.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${profit.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(profitList) == 0}">
                        <tr class="text-center">
                            <td colspan="9">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <div class="panel-body">幹部經歷紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">birth</th>
                        <th class="text-center">unit</th>
                        <th class="text-center">startTime</th>
                        <th class="text-center">endTime</th>
                        <th class="text-center">term</th>
                        <th class="text-center">job</th>
                        <th class="text-center">level</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${cadreList}" var="cadre" varStatus="status">
                        <tr class="text-center">
                            <td>${cadre.id}</td>
                            <td>${cadre.idno}</td>
                            <td>${cadre.birth}</td>
                            <td>${cadre.unit}</td>
                            <td>${cadre.startTime}</td>
                            <td>${cadre.endTime}</td>
                            <td>${cadre.term}</td>
                            <td>${cadre.job}</td>
                            <td>${cadre.level}</td>
                            <td>${cadre.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${cadre.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(cadreList) == 0}">
                        <tr class="text-center">
                            <td colspan="11">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <div class="panel-body">競賽參與紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">birth</th>
                        <th class="text-center">name</th>
                        <th class="text-center">item</th>
                        <th class="text-center">level</th>
                        <th class="text-center">award</th>
                        <th class="text-center">time</th>
                        <th class="text-center">type</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${competitionList}" var="competition" varStatus="status">
                        <tr class="text-center">
                            <td>${competition.id}</td>
                            <td>${competition.idno}</td>
                            <td>${competition.birth}</td>
                            <td>${competition.name}</td>
                            <td>${competition.item}</td>
                            <td>${competition.level}</td>
                            <td>${competition.award}</td>
                            <td>${competition.time}</td>
                            <td>${competition.type}</td>
                            <td>${competition.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${competition.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(competitionList) == 0}">
                        <tr class="text-center">
                            <td colspan="11">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <div class="panel-body">檢定證照紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">birth</th>
                        <th class="text-center">code</th>
                        <th class="text-center">note</th>
                        <th class="text-center">point</th>
                        <th class="text-center">result</th>
                        <th class="text-center">time</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${licenseList}" var="license" varStatus="status">
                        <tr class="text-center">
                            <td>${license.id}</td>
                            <td>${license.idno}</td>
                            <td>${license.birth}</td>
                            <td>${license.code}</td>
                            <td>${license.note}</td>
                            <td>${license.point}</td>
                            <td>${license.result}</td>
                            <td>${license.time}</td>
                            <td>${license.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${license.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(licenseList) == 0}">
                        <tr class="text-center">
                            <td colspan="10">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <div class="panel-body">志工服務紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">birth</th>
                        <th class="text-center">name</th>
                        <th class="text-center">place</th>
                        <th class="text-center">startTime</th>
                        <th class="text-center">endTime</th>
                        <th class="text-center">count</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${volunteerList}" var="volunteer" varStatus="status">
                        <tr class="text-center">
                            <td>${volunteer.id}</td>
                            <td>${volunteer.idno}</td>
                            <td>${volunteer.birth}</td>
                            <td>${volunteer.name}</td>
                            <td>${volunteer.unit}</td>
                            <td>${volunteer.startTime}</td>
                            <td>${volunteer.endTime}</td>
                            <td>${volunteer.count}</td>
                            <td>${volunteer.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${volunteer.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(volunteerList) == 0}">
                        <tr class="text-center">
                            <td colspan="10">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <div class="panel-body">其他紀錄
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">id</th>
                        <th class="text-center">idno</th>
                        <th class="text-center">birth</th>
                        <th class="text-center">name</th>
                        <th class="text-center">unit</th>
                        <th class="text-center">startTime</th>
                        <th class="text-center">endTime</th>
                        <th class="text-center">count</th>
                        <th class="text-center">document</th>
                        <th class="text-center">download</th>
                    </tr>
                    <c:forEach items="${otherList}" var="other" varStatus="status">
                        <tr class="text-center">
                            <td>${other.id}</td>
                            <td>${other.idno}</td>
                            <td>${other.birth}</td>
                            <td>${other.name}</td>
                            <td>${other.unit}</td>
                            <td>${other.startTime}</td>
                            <td>${other.endTime}</td>
                            <td>${other.count}</td>
                            <td>${other.document}</td>
                            <td>
                                <button type="button" class="btn btn-success"
                                        onclick="javascript:location.href='sendDownloadFile?file=<c:out
                                                value="${other.document}"></c:out>'">下載檔案
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(otherList) == 0}">
                        <tr class="text-center">
                            <td colspan="10">查無資料</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
