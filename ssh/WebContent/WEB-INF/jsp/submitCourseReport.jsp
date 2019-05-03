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

    <div class="">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程成果紀錄 - 學生上傳但未驗證
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport._ret1}" scope="request"/>
                <c:import url="./components/courseRecordReport.jsp">
                    <c:param name="title" value="未驗證"/>
                    <c:param name="noDetails" value="0"/>
                    <c:param name="q" value="2"/>
                </c:import>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程成果紀錄 - 驗證成功但未勾選
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport._ret2}" scope="request"/>
                <c:import url="./components/courseRecordReport.jsp">
                    <c:param name="title" value="驗成未勾"/>
                    <c:param name="noDetails" value="0"/>
                    <c:param name="q" value="3"/>
                </c:import>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程成果紀錄 - 驗證成功且已勾選
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport._ret3}" scope="request"/>
                <c:import url="./components/courseRecordReport.jsp">
                    <c:param name="title" value="驗成已勾"/>
                    <c:param name="noDetails" value="0"/>
                    <c:param name="q" value="5"/>

                </c:import>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程成果紀錄 - 已報送
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport._ret4}" scope="request"/>
                <c:import url="./components/courseRecordReport.jsp">
                    <c:param name="title" value="已送"/>
                    <c:param name="noDetails" value="0"/>
                    <c:param name="q" value="6"/>
                </c:import>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>
                課程成果紀錄 - 認證失敗
            </div>
            <div class="panel-body">
                <c:set var="submitRecords" value="${submitReport._ret5}" scope="request"/>
                <c:import url="./components/courseRecordReport.jsp">
                    <c:param name="title" value="驗證失敗"/>
                    <c:param name="noDetails" value="0"/>
                    <c:param name="q" value="4"/>
                </c:import>
            </div>
        </div>

    </div>
</div>
</body>
</html>
