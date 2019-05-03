<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../includes/banner.jsp"/>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="manage">高中生歷程系統</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href=main>首頁</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#"><span class="glyphicon glyphicon-user"></span> ${account.getName()} 管理員</a></li>
            <li><a href="logout"><span class="glyphicon glyphicon-log-in"></span> 登出</a></li>
        </ul>
    </div>
</nav>
