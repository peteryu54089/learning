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

    <jsp:include page="components/studentLightBox.jsp">
        <jsp:param name="title" value="${title}"/>
    </jsp:include>

    <script>
    frame.unbind('load');
    frame.bind('load', function (e) {
        let doc = this.contentDocument;
        doc.documentElement.style.background =
            doc.body.style.background = 'rgba(0, 0, 0, 0)';
        doc.body.style.margin = '0';

        let c = doc.body.querySelector('.container');
        if(!c) return;
        c.classList.remove('container');
        c.classList.add('col-xs-12');

        let cjQuery = $(c);
        cjQuery.find('>nav, >header').remove();
        let table = cjQuery.find('#main-table');
        table.find('.btn:not(.view-btn, .close-btn)').remove();
        table.find('.cell-op').css('width', '100px');

        $(this).show();
    })
    </script>
    <script>
    $('.view-btn').click(function (e) {
        let tr = $(this).parents('.stu-row');
        let year = tr.data('termYear');
        let sem = tr.data('termSem');
        let regno = tr.data('regno');
        let rgno = tr.data('rgno');
        let search = new URLSearchParams();
        // search.append('year', year);
        // search.append('sem', sem);
        search.append('rgno', rgno);

        let href = './courseRecord?' + search.toString();
        frame.prop('src', href);
        frame.hide();
        modal.modal('show');
    });
    </script>
</div><!-- /container -->
</body>
</html>
