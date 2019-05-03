<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="student" type="model.role.Student"--%>
<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2018/12/04
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row">
    <div class="col-sm-5">
        <jsp:include page="../../studentBasicInfo.jsp">
            <jsp:param name="student" value="${student}"/>
            <jsp:param name="config" value="${config}"/>
        </jsp:include>
    </div>
    <div class="col-sm-7">
        <div class="panel panel-info">
            <div class="panel-heading">學生資料</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-xs-8">
                        <div class="form-group">
                            <label>社群帳號</label>
                            <div class="">
                                <c:out value="${student.socialAccount}"/>
                            </div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <label>自我介紹</label>
                            <div style="white-space: pre-line"><c:out value="${student.bio}"/></div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <label>暱稱</label>
                            <div class="">
                                <c:out value="${student.nickname}"/>
                            </div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <label>我的興趣</label>
                            <div class="">
                                <c:out value="${student.interest}"/>
                            </div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <label>備用信箱</label>
                            <div class="">
                                <c:out value="${student.altMail}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="form-group">
                            <label>大頭貼</label>
                            <div class="figure" style="margin-bottom: 1rem">
                                <div class="img-thumbnail css-box"
                                     style="width: 100%; line-height: 0">
                                    <div class="content">
                                        <img alt="LOGO"
                                             src="studentAvatar?rgno=${student.rgno}&_=<%=new Date().getTime()%>"
                                             style="object-fit: cover;width: 100%; height: 100%; object-position: center"
                                             onerror="this.src='schoolLogo?_=' + new Date().getTime()"
                                             id="avatarImg"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>