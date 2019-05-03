<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="profitList" type="java.util.List<model.Profit>"--%>
<html>
<head>
    <jsp:include page="../includes/header.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="row">
                <div class="col-sm-6">
                    <div class="panel panel-danger">
                        <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>使用者資訊
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th colspan="2" class="text-center">基本資料</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr class="text-center">
                                    <td>中文姓名</td>
                                    <td>${account.getName()}</td>
                                </tr>
                                <tr class="text-center">
                                    <td>職員編號</td>
                                    <td>${account.regNumber}</td>
                                </tr>
                                <tr class="text-center">
                                    <td>職稱</td>
                                    <td>授課教師</td>
                                </tr>
                                <tr class="text-center">
                                    <td>電子信箱</td>
                                    <td>${account.getEmail()}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="panel panel-info">
                        <div class="panel-heading"><span
                                class="glyphicon glyphicon-exclamation-sign">　</span>
                                	<b>${config.courseStudyRecord.activeYear} - ${config.courseStudyRecord.activeSem}學年期</b>　課程學習成果待認證通知 <br>
                                	(可驗證期間 ${allowVerifyTimeRange})
                        </div>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>學年</th>
                                <th>學期</th>
                                <th>科目</th>
                                <th>學生姓名</th>
                            </tr>
                            <thead>
                            <tbody>
                            <c:if test="${profitList!=null}">
                                <c:forEach var="profit" items="${profitList}">
                                    <tr>
                                        <td>${profit.term_year}</td>
                                        <td>${profit.term_sem}</td>
                                        <td>${profit.course_cname}</td>
                                        <td><c:out value="${profit.stuName}"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${fn:length(profitList) == 0}">
                                    <tr class="text-center">
                                        <td colspan="4">尚無待認證資料</td>
                                    </tr>
                                </c:if>
                            </c:if>
                            <c:if test="${profitList==null}">
                                <tr class="text-center">
                                    <td colspan="4">非驗證期間</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>

                    <jsp:include page="../components/announcement.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div><!-- /container -->
</body>
</html>
