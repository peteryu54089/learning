<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <div class="card card-container">
        <p class="profile-name-card">教職員登入</p>
        <img id="profile-img" class="profile-img-card"
             src="https://upload.wikimedia.org/wikipedia/commons/3/31/Taipeitech.jpg"/>
        <p id="profile-name" class="profile-name-card"></p>
        <form class="form-signin" action="" method="POST">
            <span id="reauth-email" class="reauth-email"></span>
            <input type="text" id="inputID" name="inputID" class="form-control" placeholder="職員編號" required autofocus>
            <input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="密碼"
                   required>
            <p style="color:red;"> ${error}</p>
            <div id="remember" class="checkbox">
            </div>
            <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">登入</button>
        </form><!-- /form -->
        <a href="#" class="forgot-password">
            忘記密碼?
        </a>
    </div><!-- /card-container -->

</div><!-- /container -->
</body>

</html>
