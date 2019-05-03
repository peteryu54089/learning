<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--@elvariable id="clubSyncSuccess" type="java.lang.Boolean"--%>
<%@ page import="util.DateUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="includes/header.jsp"/>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <c:if test="${clubSyncSuccess!=null}">
        <c:if test="${clubSyncSuccess}">
            <div class="alert alert-success">
                <strong>成功!</strong> 資料同步完成
            </div>
        </c:if>
        <c:if test="${!clubSyncSuccess}">
            <div class="alert alert-danger">
                <strong>失敗!</strong> 同步時發生錯誤，請聯絡管理員
            </div>
        </c:if>
    </c:if>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>
                    同步社團幹部資料
                </div>
                <div class="panel-body">
                    <form class="form" method="POST" onsubmit="this.sub.disabled = true">
                        <div class="form-group">
                            <label>最後同步時間</label>
                            <span class="form-control form-control-static">
                                ${DateUtils.formatDateTime(config.syncStatus.lastSyncedStuClubTime)}
                            </span>
                        </div>

                        <button type="submit" class="btn btn-primary" name="sub">
                            同步
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
