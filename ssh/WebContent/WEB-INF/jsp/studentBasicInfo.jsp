<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--@elvariable id="student" type="model.role.Student"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="panel panel-danger">
    <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>使用者資訊
    </div>
    <div class="panel-body">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th colspan="2" class="text-center">基本資料</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${config!=null}">
                <tr class="text-center">
                    <td>學校</td>
                    <td>(<c:out value="${config.schoolInfo.id}"/>) <c:out value="${config.schoolInfo.name}"/></td>
                </tr>
            </c:if>
            <tr class="text-center">
                <td>學年度學期</td>
                <td><c:out value="${student.term_year}-${student.term_semester}"/> </td>
            </tr>
            <tr class="text-center">
                <td>學號</td>
                <td><c:out value="${student.regNumber}"/></td>
            </tr>
            <tr class="text-center">
                <td>班級代碼</td>
                <td><c:out value="${student.classCode}"/></td>
            </tr>
            <tr class="text-center">
                <td>班級名稱</td>
                <td><c:out value="${student.className}"/></td>
            </tr>
            <tr class="text-center">
                <td>班級座號</td>
                <td><c:out value="${student.class_no}"/></td>
            </tr>
            <tr class="text-center">
                <td>中文姓名</td>
                <td><c:out value="${student.stu_Cname}"/></td>
            </tr>
            <tr class="text-center">
                <td>英文姓名</td>
                <td><c:out value="${student.stu_Ename}"/></td>
            </tr>
            <tr class="text-center">
                <td>身分證字號</td>
                <td><c:out value="${student.idno}"/></td>
            </tr>
            <tr class="text-center">
                <td>電子郵件地址</td>
                <td><c:out value="${student.email}"/></td>
            </tr>
            <tr class="text-center">
                <td>行動電話號碼</td>
                <td><c:out value="${student.mobile_telno}"/></td>
            </tr>
            </tbody>
        </table>
    </div>
</div> <!-- end of student basic info-->
