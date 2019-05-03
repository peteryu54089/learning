<%--@elvariable id="title" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <jsp:include page="managerPage/components/studentSearchComponent.jsp">
                <jsp:param name="title" value="${title}"/>
            </jsp:include>

            <div style="background: #FFF; padding: .5rem">
                <jsp:include page="managerPage/components/studentTable.jsp"/>
            </div>
        </div>
    </div>
    <!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <form class="modal-content" id="form">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title"><c:out value="${title}"/></h4>
                </div>
                <div class="modal-body" id="stuModalBody">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
                </div>
            </form>

        </div>
    </div>

    <script>
    let modal = $('#myModal');
    $('.view-btn').click(function () {
        let tr = $(this).parents('.stu-row');
        let year = tr.data('termYear');
        let sem = tr.data('termSem');
        let regno = tr.data('regno');

        $.ajax('tutorStuBasicInfo', {
            method: 'POST',
            dataType: 'text',
            data: {
                year, sem, regno,
            }
        }).done(text => {
            let body = modal.find('#stuModalBody');
            body.empty().append(text);
            modal.modal('show');
        });
    });
    </script>
</div><!-- /container -->
</body>
</html>
