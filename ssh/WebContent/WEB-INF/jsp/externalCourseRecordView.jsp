<%@ page import="util.ObjectUtils" %><%--@elvariable id="list" type="java.util.List<model.List<CourseRecordDocument>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <c:forEach items="${list}" var="o">
        <p>
            <a class="btn btn-default" href="${ObjectUtils.getField(o, "link")}"><c:out value="${ObjectUtils.getField(o, \"doc\").original_filename}"/></a>
        </p>
    </c:forEach>
</div>
</body>
</html>

