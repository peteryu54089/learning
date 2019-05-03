<%--@elvariable id="list" type="model.ModelList<model.role.Student>"--%>
<%--@elvariable id="itemsPerPage" type="java.lang.Integer"--%>
<%--@elvariable id="page" type="java.lang.Integer"--%>
<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2018/12/03
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table class="table table-striped" style="table-layout: fixed">
    <thead>
    <tr>
        <th width="120">學號</th>
        <th width="120">班級</th>
        <th width="120">座號</th>
        <th width="150">姓名</th>
        <th style="width: 120px;">
        </th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list.list}" var="stu">
        <tr data-regno="${stu.regNumber}" data-rgno="${stu.rgno}" data-term-year="${stu.term_year}"
            data-term-sem="${stu.term_semester}"
            class="stu-row">
            <td>${fn:escapeXml(stu.regNumber)}</td>
            <td>${fn:escapeXml(stu.className)}</td>
            <td>${fn:escapeXml(stu.class_no)}</td>
            <td>${fn:escapeXml(stu.stu_Cname)}</td>
            <c:if test="${stu.performanceUnlock}">
                <td>
                    <button class="btn btn-warning view-btn">取消解鎖</button>
                </td>
            </c:if>
            <c:if test="${!stu.performanceUnlock}">
                <td>
                    <button class="btn btn-warning view-btn">解鎖</button>
                </td>
            </c:if>


        </tr>
    </c:forEach>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="5">
            <div id="pagination"></div>
        </td>
    </tr>
    </tfoot>
</table>

<script>
(function ($) {
    let search = new URLSearchParams(location.search);
    $('#pagination').pagination({
        items: ${list.total},
        itemsOnPage: ${itemsPerPage},
        currentPage: ${page},
        cssStyle: 'light-theme',
        ellipsePageSet: false,
        selectOnClick: false,
        useAnchors: false,
        onPageClick: function (pageNumber, e) {
            search.set('page', pageNumber);
            location.search = search.toString();
        },
    });
})(jQuery);
</script>