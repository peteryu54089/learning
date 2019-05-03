<%--@elvariable id="student" type="model.role.Student"--%>
<%--@elvariable id="account" type="model.Account"--%>
<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ page import="util.DateUtils" --%> 
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
                    <jsp:include page="../studentBasicInfo.jsp">
                        <jsp:param name="account" value="${account}"/>
                        <jsp:param name="student" value="${student}"/>
                    </jsp:include>
                </div>
                <div class="col-sm-6">
                    <div class="panel panel-info">
                        <div class="panel-heading"><span
                                class="glyphicon glyphicon-exclamation-sign">　</span>
                                成果上傳時間
                        </div>
                        <div class="panel-body">
                            <h3>${config.courseStudyRecord.activeYear} - ${config.courseStudyRecord.activeSem}課程學習成果</h3>
                            <p>
                                <b>上傳時間：</b> 
								${DateUtils.formatDateTime(config.courseStudyRecord.studentStartDateTime)}  
                                ~
                                ${DateUtils.formatDateTime(config.courseStudyRecord.studentEndDateTime)}
                            </p>
                            
                            <hr/>
                            
                            <h3>${config.performance.submitYear}多元學習成果</h3>
                            <p>
                                <b>上傳時間：</b>
                                ${DateUtils.formatDateTime(config.performance.studentStartDateTime)}
                                ~
                                ${DateUtils.formatDateTime(config.performance.studentEndDateTime)}
                                
                            </p>

                        </div>
                    </div>
                    <jsp:include page="../components/announcement.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div><!-- /container -->
</body>
</html>
