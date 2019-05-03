<%--@elvariable id="title" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <jsp:include page="../includes/header.jsp"/>
</head>
<body>
<div class="container">
	<jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <jsp:include page="components/studentSearchComponent.jsp">
                <jsp:param name="title" value="${title}"/>
            </jsp:include>

            <div style="background: #FFF; padding: .5rem">
                <jsp:include page="components/studentTable.jsp"/>
            </div>
        </div>
    </div>

    <script>
    $('.view-btn').click(function (e) {
        let tr = $(this).parents('.stu-row');
        let year = tr.data('termYear');
        let sem = tr.data('termSem');
        let regno = tr.data('regno');
        let rgno = tr.data('rgno');
        let search = new URLSearchParams();
        search.append('year', year);
        search.append('sem', sem);
        search.append('rgno', rgno);

        let href = './tutorViewCourseResult?' + search.toString();
        window.open(href);
    });
    </script>
</div><!-- /container -->
</body>
</html>
