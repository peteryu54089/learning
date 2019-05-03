<%--@elvariable id="student" type="model.role.Student"--%>
<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--@elvariable id="avatarSizeLimit" type="java.lang.Integer"--%>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-4">
            <jsp:include page="studentBasicInfo.jsp">
                <jsp:param name="student" value="${student}"/>
                <jsp:param name="config" value="${config}"/>
            </jsp:include>
        </div>
        <div class="col-sm-8">
            <div class="panel panel-info">
                <div class="panel-heading">修改基本資料</div>
                <div class="panel-body">
                    <form id="form" class="form" enctype="multipart/form-data" method="POST">
                        <div class="row">
                            <div class="col-xs-8">
                                <div class="form-group">
                                    <label for="socialAccount">社群帳號</label>
                                    <input id="socialAccount" name="socialAccount"
                                           type="text" class="form-control"
                                           value="<c:out value="${student.socialAccount}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="bio">自我介紹</label>
                                    <textarea id="bio" name="bio" rows="3"
                                              class="form-control"
                                    ><c:out value="${student.bio}"/></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="nickname">暱稱</label>
                                    <input id="nickname" name="nickname"
                                           type="text" class="form-control"
                                           value="<c:out value="${student.nickname}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="interest">我的興趣</label>
                                    <input id="interest" name="interest"
                                           type="text" class="form-control"
                                           value="<c:out value="${student.interest}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="email">主要信箱</label>
                                    <input id="email" name="email"
                                           type="email" class="form-control"
                                           value="<c:out value="${student.email}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="altMail">備用信箱</label>
                                    <input id="altMail" name="altMail"
                                           type="email" class="form-control"
                                           value="<c:out value="${student.altMail}"/>">
                                </div>
                                <div class="form-group">
                                    <label for="phone">行動電話號碼</label>
                                    <input id="phone" name="phone"
                                           type="tel" class="form-control"
                                           value="<c:out value="${student.mobile_telno}"/>">
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="avatar">大頭貼</label>

                                    <div class="figure" style="margin-bottom: 1rem">
                                        <div class="img-thumbnail css-box"
                                             style="width: 100%; line-height: 0">
                                            <div class="content">
                                                <img alt="LOGO" src="studentAvatar?_=<%=new Date().getTime()%>"
                                                     style="object-fit: cover;width: 100%; height: 100%; object-position: center"
                                                     id="avatarImg"/>
                                            </div>
                                        </div>
                                    </div>

                                    <p>
                                        <input type="file" accept="image/*" id="avatar" name="avatar"
                                               class="form-control">
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
    let avatarImg = $('#avatarImg');
    avatarImg.on('error', function () {
        this.src = 'schoolLogo?_=' + new Date().getTime();
    });
    $('#avatar').change(function (e) {
        let f = this.files[0];
        if (f) {
            if (f.size >${avatarSizeLimit}) {
                this.value = '';
                alert('檔案過大，請選擇1MB以內的檔案');
                return;
            }
            let r = new FileReader();
            r.onload = () => {
                avatarImg.prop('src', r.result);
            };
            r.readAsDataURL(this.files[0]);
        } else {
            avatarImg.prop('src', 'studentAvatar?_=' + new Date().getTime());
        }
    });

    $('#form').submit(function (e) {
        e.preventDefault();
        let fd = new FormData(this);
        fetch("studentBasicInfoModifyServlet", {
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
