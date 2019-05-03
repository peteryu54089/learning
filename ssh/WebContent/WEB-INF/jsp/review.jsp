<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>
<script>

function toggle(source) {
    var checkboxes = document.getElementsByName('profit[]');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = source.checked;
    }
}

function verify() {
    ($('input[name="profit[]"]:checked').each(
        function () {
            $.ajax({
                type: "POST",
                url: "tprofit",
                data: {
                    id: $(this).val(),
                    status: 2
                },
                success: function (result) {
                    alert(result);
                }
            })
        }
    ));
    location.reload();
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
                            <form action="" method="GET">
                                <div class="form-group">
                                    <label for="year">選擇學期</label>
                                    <select class="form-control" id="year" name="year"
                                            onchange="this.form.submit()">
                                        <c:forEach var="i" begin="90" end="${max_year}">
                                            <option value="${i}"
                                                    <c:if test="${i == year}">selected</c:if> >${i}</option>
                                        </c:forEach>
                                    </select>
                                    <label for="sem">選擇學年度</label>
                                    <select class="form-control" id="sem" name="sem"
                                            onchange="this.form.submit()">
                                        <option value="1" <c:if test="${1 == sem}">selected</c:if>>上學期
                                        </option>
                                        <option value="2" <c:if test="${2 == sem}">selected</c:if>>下學期
                                        </option>
                                    </select>
                                    <label for="course">選擇課堂</label>
                                    <select class="form-control" id="course" name="course"
                                            onchange="this.form.submit()">
                                        <option value="0">全選</option>
                                        <c:forEach items="${courseList}" var="course">
                                            <option value="${course.course_num}" <c:if
                                                    test="${course.course_num == course_num}"> selected</c:if>>${course.course_cname}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-sm-8">
                    <div class="panel panel-info">
                        <div class="panel-heading"><span
                                class="glyphicon glyphicon-exclamation-sign">　</span>待認證
                        </div>
                        <div class="panel-body">
                            <button type="button" class="btn-primary" onclick="verify()">給予驗證</button>
                            <a href="getProfitZip?year=${year}&sem=${sem}&course=${course_num}" target="_blank">
                                <button type="button" class="btn-info">下載壓縮檔</button>
                            </a>
                            <br/><br/>
                            <table class="table table-bordered">
                                <tr class="text-center">
                                    <th><input type="checkbox" onClick="toggle(this)" id="selectAll" name="selectAll">
                                    </th>
                                    <th>科目</th>
                                    <th>學生姓名</th>
                                    <th>狀態</th>
                                    <th>連結</th>
                                </tr>
                                <c:set var="index" value="0"></c:set>
                                <c:forEach var="profit" items="${profitList}">
                                    <tr>
                                        <td><c:if test="${profit.status == \"1\" and isModifiable.get(index)}"><input
                                                type="checkbox"
                                                id="profit[]"
                                                name="profit[]"
                                                value="${profit.id}"></c:if>
                                        </td>
                                        <td>${profit.course_cname}</td>
                                        <td><c:out value="${studentList.get(index).cname}"></c:out></td>
                                        <td>
                                            <c:if test="${profit.status == \"1\"}"> <span
                                                    class="glyphicon glyphicon-question-sign"></span> 老師尚未認證 </c:if>
                                            <c:if test="${profit.status == \"-1\"}"> <span
                                                    class="glyphicon glyphicon-remove"></span> 認證未通過 </c:if>
                                            <c:if test="${profit.status == \"2\"}"> <span
                                                    class="glyphicon glyphicon-ok"></span> 認證已通過 </c:if>
                                        </td>
                                        <td><a href="tprofit?id=${profit.id}">點我連結</a></td>
                                    </tr>
                                    <c:set var="index" value="${index+1}"></c:set>
                                </c:forEach>
                                <c:if test="${fn:length(profitList) == 0}">
                                    <tr class="text-center">
                                        <td colspan="5">查無資料</td>
                                    </tr>
                                </c:if>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- /container -->
    </div>
</div>
</body>
</html>
