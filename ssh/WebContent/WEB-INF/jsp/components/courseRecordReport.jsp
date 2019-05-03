<%--@elvariable id="submitRecords" type="java.util.List<model.PerformanceRecordSubmitRecord>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<table class="table table-bordered" style="text-align: center;table-layout: fixed">
    <tr>
        <th class="text-center" width="12%">學年</th>
        <th class="text-center" width="12%">學期</th>
        <th class="text-center"><br/>${param.title}數量</th>
        <c:if test="${!param.noDetails.equals(\"1\")}">
            <th class="text-center">詳情</th>
        </c:if>
    </tr>
    <c:forEach items="${submitRecords}" var="record" varStatus="status">
        <tr class="text-center">
            <td>${record.year}</td>
            <td>${record.sem}</td>
            <td>${record.cnt}</td>

            <c:if test="${!param.noDetails.equals(\"1\")}">
                <td>
                    <form method="POST" target="_blank">
                        <input type="hidden" name="year" value="${record.year}"/>
                        <input type="hidden" name="sem" value="${record.sem}"/>
                        <input type="hidden" name="q" value="${param.q}"/>
                        <button type="submit" class="btn btn-success">查看詳情</button>
                    </form>
                </td>
            </c:if>
        </tr>
    </c:forEach>
    <c:if test="${fn:length(submitRecords) == 0}">
        <tr class="text-center">
            <td
                    <c:if test="${!param.noDetails.equals(\"1\")}">
                        colspan="4"
                    </c:if>
                    <c:if test="${param.noDetails.equals(\"1\")}">
                        colspan="3"
                    </c:if>
            >查無資料
            </td>
        </tr>
    </c:if>
</table>

<p class="text-muted">學年為0表示尚未確認學年</p>
