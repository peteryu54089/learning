<%--@elvariable id="submitRecords" type="java.util.List<model.PerformanceRecordSubmitRecord>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<table class="table table-bordered" style="text-align: center;table-layout: fixed">
    <tr>
        <th class="text-center" width="12%">學年</th>
        <th class="text-center">幹部(校內)<br/>${param.title}數量</th>
        <th class="text-center">幹部(學生自建)<br/>${param.title}數量</th>
        <th class="text-center">競賽<br/>${param.title}數量</th>
        <th class="text-center">證照<br/>${param.title}數量</th>
        <th class="text-center">志工<br/>${param.title}數量</th>
        <th class="text-center">其他活動<br/>${param.title}數量</th>
        <c:if test="${!param.noDetails.equals(\"1\")}">
            <th class="text-center">詳情</th>
        </c:if>
    </tr>
    <c:forEach items="${submitRecords}" var="record" varStatus="status">
        <tr class="text-center">
            <td>${record.year}</td>
            <td>${record.cadSchool}</td>
            <td>${record.cadSelf}</td>
            <td>${record.com}</td>
            <td>${record.lic}</td>
            <td>${record.vol}</td>
            <td>${record.oth}</td>

            <c:if test="${!param.noDetails.equals(\"1\")}">
                <td>
                    <form method="POST" target="_blank">
                        <input type="hidden" name="year" value="${record.year}"/>
                        <input type="hidden" name="date" value="${record.submittedAt}"/>
                        <input type="hidden" name="status" value="${param.status==null?2:param.status}"/>
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
                        colspan="8"
                    </c:if>
                    <c:if test="${param.noDetails.equals(\"1\")}">
                        colspan="7"
                    </c:if>
            >查無資料
            </td>
        </tr>
    </c:if>
</table>

<p class="text-muted">學年為0表示尚未確認學年</p>
