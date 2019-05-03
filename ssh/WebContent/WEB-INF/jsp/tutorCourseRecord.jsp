<%--@elvariable id="title" type="java.lang.String"--%>
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
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <jsp:include page="managerPage/components/studentSearchComponent.jsp">
            <jsp:param name="title" value="${title}"/>
        </jsp:include>

        <div style="background: #FFF; padding: .5rem">
            <jsp:include page="managerPage/components/studentTable.jsp"/>
        </div>

    </div>
</div>

<jsp:include page="managerPage/components/studentLightBox.jsp">
    <jsp:param name="title" value="${title}"/>
</jsp:include>

<script>
$('.view-btn').click(function (e) {
    let tr = $(this).parents('.stu-row');
    let year = tr.data('termYear');
    let sem = tr.data('termSem');
    let rgno = tr.data('rgno');

    let href = './tutorViewCourseRecord?rgno=' + encodeURIComponent(rgno);
    window.open(href);
});
</script>
</body>
