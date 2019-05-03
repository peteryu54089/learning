<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <jsp:include page="../includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading">設定Banner</div>
                <div class="panel-body">
                    <form id="form">
                        <fieldset class="form-group">
                            <label class="label">目前Banner</label>
                            <img src="schoolBanner?_=<%=new Date().getTime()%>"
                                 style="width: 100%;object-fit:cover;object-position:left top"/>
                        </fieldset>
                        <hr>
                        <fieldset class="form-group">
                            <label for="banner">新的Banner</label>
                            <input type="file" accept="image/*" name="banner" id="banner" class="form-control"/>
                        </fieldset>

                        <p>
                            <button type="submit" class="btn btn-primary">更新</button>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>
    $('#form').submit(function (e) {
        e.preventDefault();
        let fd = new FormData(this);
        fetch("schoolBanner", {
            credentials: 'include',
            body: fd,
            method: 'POST'
        }).then(r => {
            switch (r.status) {
                case 500:
                    alert('伺服器錯誤，請重新嘗試，或聯絡系統管理員');
                    break;
                case 304:
                    alert('沒有上傳圖片');
                    break;
                case 200:
                    location.reload(true);
                    alert('更新成功');
                    break;
            }
        });
    })
    </script>

</div><!-- /container -->
</body>
</html>
