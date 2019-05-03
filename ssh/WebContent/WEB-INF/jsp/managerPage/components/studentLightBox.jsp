<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2018/12/03
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">

        <!-- Modal content-->
        <form class="modal-content" id="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><c:out value="${param[\"title\"]}"/></h4>
            </div>
            <div class="modal-body" id="stuModalBody">
                <div class="row">
                    <iframe src="" frameborder="0" width="100%" style="height: 70vh"></iframe>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
            </div>
        </form>

    </div>
</div>
<script>
let modal = $('#myModal');
let frame = $('#stuModalBody iframe');
frame.bind('load', function () {
    try {
        this.contentDocument.documentElement.style.background =
            this.contentDocument.body.style.background = 'rgba(0, 0, 0, 0)';
        this.contentDocument.body.style.margin = '0';
        let c = $(this.contentDocument.body).find('>.container');
        c.removeClass('container');
        c.find('>nav, >header').hide();
    } catch (e) {
    }
});
</script>
