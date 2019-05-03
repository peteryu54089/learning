<%--@elvariable id="submitReport" type="dao.SubmitPerformanceDao.Report"--%>
<%--@elvariable id="hideBar" type="java.lang.Boolean"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
	<jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <%--
    <c:if test="${hideBar==null || hideBar == false}">
        <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    </c:if>
    --%>
    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                多元表現紀錄 - 學生上傳但未勾選
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport.uncheckedAndNotSubmit}" scope="request"/>
                <c:import url="./components/performanceReport.jsp">
                    <c:param name="title" value="未勾未送"/>
                    <c:param name="noDetails" value="1"/>
                </c:import>
            </div>
        </div>
        <div class="panel panel-warning">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                多元表現紀錄 - 學生勾選未報送
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport.checkedAndNotSubmit}" scope="request"/>
                <c:import url="./components/performanceReport.jsp">
                    <c:param name="title" value="已勾未報"/>
                    <c:param name="status" value="1"/>
                </c:import>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                多元表現紀錄 - 已報送
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport.checkedAndSubmitted}" scope="request"/>
                <c:import url="./components/performanceReport.jsp">
                    <c:param name="title" value="已報送"/>
                </c:import>
            </div>
        </div>
    </div>
</div>
</body>
</html>
