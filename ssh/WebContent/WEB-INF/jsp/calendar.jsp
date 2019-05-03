<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/26
  Time: 下午 04:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <jsp:include page="includes/header.jsp"></jsp:include>

    <link href='resources/css/fullcalendar.min.css' rel='stylesheet'/>
    <link href='resources/css/fullcalendar.print.min.css' rel='stylesheet' media='print'/>
    <script src='resources/javascript/moment.min.js'></script>
    <script src='resources/javascript/fullcalendar.min.js'></script>
    <script>

    $(document).ready(function () {

        $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,basicWeek,basicDay'
            },
            <c:if test = "${attend.size() > 0}">
            defaultDate: '${attend.get(0).date}',
            </c:if>
            <c:if test = "${attend.size() == 0}">
            defaultDate: '2018-05-14',
            </c:if>
            navLinks: true, // can click day/week names to navigate views
            editable: false,
            eventLimit: true, // allow "more" link when too many events
            eventOrder: 'order',
            events: {
                url: './calendar',
                type: 'POST',
                data: {
                    a: "1"
                },
            },

            error: function () {
                alert('無法取得資料!');
            }
        });

    });

    </script>
    <style>

    #calendar {
        max-width: 900px;
        margin: 0 auto;
        background-color: #FFFFFF;
        padding: 20px;
    }

    </style>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="body"></div>
    <div id='calendar'></div>
</div>
</body>
</html>
