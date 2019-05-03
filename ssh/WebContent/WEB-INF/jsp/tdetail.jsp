<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>

<script>
function verify() {
    <c:if test="${systemSet.tInterval == 0}">
    alert('警告: 驗證後不可修改');
    </c:if>
    <c:if test="${dueDate != null}">
    return confirm("可修改期限為${dueDate}，確定要修改驗證嗎？");
    </c:if>
    return confirm("驗證後${systemSet.tInterval}天內可修改，確定要驗證嗎？");
}
</script>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-pencil">　</span>學生歷程檔案</div>
                <div class="panel-body">
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th colspan="2" class="text-center">基本資料</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="text-center">
                                <td>學年度學期</td>
                                <td>${course.term_year} - ${course.term_sem}</td>
                            </tr>
                            <tr class="text-center">
                                <td>科目</td>
                                <td>${course.course_cname}</td>
                            </tr>
                            <tr class="text-center">
                                <td>開課班級</td>
                                <td>${course.class_cname}</td>
                            </tr>
                            <tr class="text-center">
                                <td>學生姓名</td>
                                <td>${student.cname}</td>
                            </tr>
                            <tr class="text-center">
                                <td>學號</td>
                                <td>${student.reg_no}</td>
                            </tr>
                            <tr class="text-center">
                                <td>學生班級</td>
                                <td>${student.class_cname}</td>
                            </tr>
                            <tr class="text-center">
                                <td>學生座號</td>
                                <td>${student.class_no}</td>
                            </tr>
                            <tr class="text-center">
                                <td>成果簡述</td>
                                <td><c:out value="${profit.content}"></c:out></td>
                            </tr>
                            <tr class="text-center">
                                <td>上傳檔案</td>
                                <td>
                                    <a href="download?file=${profit.document}&id=${profit.id}&i=${profit.idno}"><span
                                            class="glyphicon glyphicon-paperclip"></span> ${profit.document}
                                    </a><br>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <c:if test="${not empty teacher}">
                            <div class="col-md-12 text-center">
                                <c:if test="${!isModifiable}"><p><span class="glyphicon glyphicon-ban-circle"
                                                                       style="color:red"> 目前無法修改</span></p></c:if>
                                <form action="" method="POST">
                                    <input type="hidden" name="status" value="2">
                                    <input type="hidden" name="id" value="${profit.id}">
                                    <button onclick="return verify()" type="submit" class="btn btn-primary" <c:if
                                            test="${profit.status == \"2\" or !isModifiable}"> disabled</c:if>>給予認證
                                    </button>
                                </form>
                                <form action="" method="POST">
                                    <input type="hidden" name="status" value="-1">
                                    <input type="hidden" name="id" value="${profit.id}">
                                    <button onclick="return verify()" type="submit" class="btn btn-danger" <c:if
                                            test="${profit.status == \"-1\" or !isModifiable}"> disabled</c:if>>不予認證
                                    </button>
                                </form>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div><!-- /container -->
    </div>
</div>
</body>
</html>
