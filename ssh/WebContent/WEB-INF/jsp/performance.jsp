<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>

<script>
function selectOnChange(index) {
    window.location = "performance?type=" + index;
}
</script>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="row">
                <div class="col-sm-4">
                    <div class="panel panel-danger">
                        <div class="panel-heading"><span class="glyphicon glyphicon-circle-arrow-down"
                                                         aria-hidden="true">　</span>選項
                        </div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label for="type">選擇多元表現分類</label>
                                <select class="form-control" id="type" name="type"
                                        onchange="selectOnChange(this.options[this.selectedIndex].value)">
                                    <option value="1"
                                            <c:if test="${type == 1}">selected</c:if> >幹部經歷(非本校幹部)
                                    </option>
                                    <option value="2"
                                            <c:if test="${type == 2}">selected</c:if> >競賽成果
                                    </option>
                                    <option value="3"
                                            <c:if test="${type == 3}">selected</c:if> >檢定證照
                                    </option>
                                    <option value="4"
                                            <c:if test="${type == 4}">selected</c:if> >志工服務
                                    </option>
                                    <option value="5"
                                            <c:if test="${type == 5}">selected</c:if> >其它
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${type == 1}">
                    <jsp:include page="performancePage/cadre.jsp"></jsp:include>
                </c:if>
                <c:if test="${type == 2}">
                    <jsp:include page="performancePage/competition.jsp"></jsp:include>
                </c:if>
                <c:if test="${type == 3}">
                    <jsp:include page="performancePage/license.jsp"></jsp:include>
                </c:if>
                <c:if test="${type == 4}">
                    <jsp:include page="performancePage/volunteer.jsp"></jsp:include>
                </c:if>
                <c:if test="${type == 5}">
                    <jsp:include page="performancePage/other.jsp"></jsp:include>
                </c:if>
            </div>

        </div><!-- /container -->
    </div>
</div>
</body>
</html>
