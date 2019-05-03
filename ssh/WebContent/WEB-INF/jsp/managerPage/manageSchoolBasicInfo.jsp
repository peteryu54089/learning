<%--@elvariable id="config" type="model.SystemConfig"--%>
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
                <div class="panel-heading">設定學校資訊</div>
                <div class="panel-body">
                    <form id="form" class="form">
                        <div class="row">
                            <div class="col-xs-8">
                                <div class="form-group">
                                    <label for="schoolId">學校編號</label>
                                    <input id="schoolId"
                                           type="text" class="form-control" disabled
                                           value="<c:out value="${config.schoolInfo.id}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="name">學校名稱</label>
                                    <input name="name"
                                           id="name" type="text" required class="form-control"
                                           value="<c:out value="${config.schoolInfo.name}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="address">學校地址</label>
                                    <input name="address"
                                           id="address" type="text" required class="form-control"
                                           value="<c:out value="${config.schoolInfo.address}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="phone">學校電話</label>
                                    <input name="phone"
                                           id="phone" type="tel" required class="form-control"
                                           value="<c:out value="${config.schoolInfo.phone}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="website">學校網址</label>
                                    <input name="website"
                                           id="website" type="url" required class="form-control"
                                           value="<c:out value="${config.schoolInfo.website}"/>"/>
                                </div>

                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="logo">學校LOGO (校徽)</label>

                                    <div class="figure" style="margin-bottom: 1rem">
                                        <div class="img-thumbnail css-box"
                                             style="width: 60%; line-height: 0">
                                            <div class="content">
                                                <img alt="LOGO" src="schoolLogo?_=<%=new Date().getTime()%>"
                                                     style="object-fit: cover;width: 100%; height: 100%; object-position: center"
                                                     id="logoImg"/>
                                            </div>
                                        </div>
                                    </div>

                                    <p>
                                        <input type="file" accept="image/*" id="logo" name="logo" class="form-control">
                                    </p>
                                </div>
                            </div>
                        </div>

                        <p class="text-right">
                            <button type="submit" class="btn btn-primary">更新</button>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>
    $('#logo').change(function (e) {
        let logoImg = $('#logoImg');
        if (this.files[0]) {
            let r = new FileReader();
            r.onload = () => {
                logoImg.prop('src', r.result);
            };
            r.readAsDataURL(this.files[0]);
        } else {
            logoImg.prop('src', 'schoolLogo?_=' + new Date().getTime());
        }
    });
    $('#form').submit(function (e) {
        e.preventDefault();
        let fd = new FormData(this);
        fetch("schoolBasicInfoMange", {
            credentials: 'include',
            body: fd,
            method: 'POST'
        }).then(r => {
            switch (r.status) {
                case 500:
                    alert('伺服器錯誤，請重新嘗試，或聯絡系統管理員');
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
