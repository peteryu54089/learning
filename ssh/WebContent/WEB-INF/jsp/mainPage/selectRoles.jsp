<%--@elvariable id="roles" type="java.util.List<model.Authority.RoleIndex>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../includes/header.jsp"/>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <form method="POST">
                <table class="table" style="width: auto;margin: 0 auto;background: #FFF">
                    <thead>
                    <tr class="info">
                        <th colspan="2">請選擇要登入的身分</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${roles}" var="role">
                        <tr>
                            <th>
                                    ${role.toString()}
                            </th>
                            <td>
                                <button class="btn btn-default" name="role" value="${role.value()}">登入</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td align="right" colspan="2">
                            <p></p>
                            <a href="logout" class="btn btn-warning">登出</a>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </form>
        </div>
    </div>
</div><!-- /container -->
</body>
</html>
