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
<div class="container">
    <div class="card card-container">
        <!-- <img class="profile-img-card" src="//lh3.googleusercontent.com/-6V8xOA6M7BA/AAAAAAAAAAI/AAAAAAAAAAA/rzlHcD0KYwo/photo.jpg?sz=120" alt="" /> -->
        <img id="profile-img" class="profile-img-card"
             src="https://upload.wikimedia.org/wikipedia/commons/3/31/Taipeitech.jpg"/>
        <p id="profile-name" class="profile-name-card"></p>
        <form class="form-signin" action="LoginSID" method="POST">
            <span id="reauth-email" class="reauth-email"></span>
            <input type="text" id="inputID" name="inputID" class="form-control" placeholder="學號" required autofocus>
            <input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="密碼">
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
