<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/9
  Time: 上午 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <script>
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢多元表現
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="SubmitPerformance" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="term_year1" style="font-size: large" class="col-sm-2">學期</label>
                                <div class="col-sm-10">
                                    <select id="term_year1" name="term_year" class="form-control" required>
                                        <option value="">請選取</option>
                                        <option>1071</option>
                                        <option>1062</option>
                                        <option>1061</option>
                                        <option>1052</option>
                                        <option>1051</option>
                                        <option>1042</option>
                                        <option>1041</option>
                                        <option>1032</option>
                                        <option>1031</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <button type="submit" class="btn btn-primary" name="action" value="CADRE">
                                    下載校級幹部提交檔案
                                </button>
                            </div>
                        </div>
                    </form>

                    <form name="myForm" action="SubmitPerformance" enctype='multipart/form-data' method="POST">
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <label for="term_year2" style="font-size: large" class="col-sm-2">學年</label>
                                <div class="col-sm-10">
                                    <select id="term_year2" name="term_year" class="form-control">
                                        <option>請選取</option>
                                        <option>107</option>
                                        <option>106</option>
                                        <option>105</option>
                                        <option>104</option>
                                        <option>103</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <button type="submit" class="btn btn-primary" name="action" value="PERFORMANCE">
                                    下載學生自填表現提交檔案
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="" hidden>
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>提交多元表現
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">班級</th>
                        <th class="text-center">學號</th>
                        <th class="text-center" style="width: 12%">姓名</th>
                        <th class="text-center" style="width: 12%">幹部經歷</th>
                        <th class="text-center" style="width: 12%">競賽</th>
                        <th class="text-center" style="width: 12%">證照</th>
                        <th class="text-center" style="width: 12%">志工</th>
                        <th class="text-center" style="width: 12%">其他活動</th>
                        <th class="text-center" style="width: 12%">課程成果</th>
                    </tr>
                    <c:forEach items="${submitPerformanceList}" var="submitPerformance" varStatus="status">
                        <tr class="text-center">
                            <td>${submitPerformance.term_year}</td>
                            <td>${submitPerformance.term_semester}</td>
                            <td>${submitPerformance.unit}</td>
                            <td>${submitPerformance.register_number}</td>
                            <td>${submitPerformance.name}</td>
                            <td>${submitPerformance.cadre}</td>
                            <td>${submitPerformance.competition}</td>
                            <td>${submitPerformance.license}</td>
                            <td>${submitPerformance.volunteer}</td>
                            <td>${submitPerformance.other}</td>
                            <td>${submitPerformance.course}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fn:length(submitPerformanceList) == 0}">
                        <tr class="text-center">
                            <td colspan="11">查無資料</td>
                        </tr>
                    </c:if>
                </table>
                <form name="yourForm" action="SubmitPerformance" enctype='multipart/form-data' method="POST">
                    <button class="btn btn-primary">提交至全國資料庫</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
