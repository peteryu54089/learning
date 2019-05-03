<%--@elvariable id="performanceConfig" type="model.SystemConfig.Performance"--%>
 <%--- <%@ page import="util.DateUtils" %> ---%>
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
                <div class="panel-heading">多元表現提交設定</div>
                <div class="panel-body">
                    <form id="form" class="form">
                        <div class="row">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="submitYear">勾選學期</label>
                                    <input name="submitYear"
                                           id="submitYear" type="number" required class="form-control"
                                           min="50" max="999"
                                           value="<c:out value="${performanceConfig.submitYear}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="maxSubmitAmount">多元表現項目勾選數量</label>
                                    <input name="maxSubmitAmount"
                                           id="maxSubmitAmount" type="number" required class="form-control"
                                           min="1"
                                           value="<c:out value="${performanceConfig.maxSubmitAmount}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="studentStartDate">學生勾選起始日</label>
                                    <input name="studentStartDate"
                                           id="studentStartDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(performanceConfig.studentStartDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="studentEndDate">學生勾選截止日</label>
                                    <input name="studentEndDate"
                                           id="studentEndDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(performanceConfig.studentEndDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="maxDocSize">文件大小限制 (MB)</label>
                                    <input name="maxDocSize"
                                           id="maxDocSize" type="number" required class="form-control size-input"
                                           min="1" max="10"
                                           value="<c:out value="${performanceConfig.maxDocSize}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="allowDocTypes">允許文件檔案類型</label>
                                    <input name="allowDocTypes"
                                           id="allowDocTypes" type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${performanceConfig.allowDocTypesString}"/>"/>
                                    <p class="text-muted">
                                        每個副檔名使用"<span style="color: red">, </span>"分離
                                    </p>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="maxVideoSize">影音大小限制 (MB)</label>
                                    <input name="maxVideoSize"
                                           id="maxVideoSize" type="number" required class="form-control size-input"
                                           min="1" max="10" disabled
                                           value="<c:out value="${performanceConfig.maxVideoSize}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="allowVideoTypes">允許影音檔案類型</label>
                                    <input name="allowVideoTypes"
                                           id="allowVideoTypes" type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${performanceConfig.allowVideoTypesString}"/>"/>
                                    <p class="text-muted">
                                        每個副檔名使用"<span style="color: red">, </span>"分離
                                    </p>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>
                        </div>

                        <p class="text-right">
                            <button type="submit" class="btn btn-primary">
                                &emsp;&emsp;&emsp;更新&emsp;&emsp;&emsp;
                            </button>
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
        handleSizeInput(fd, 'maxDocSize');
        handleSizeInput(fd, 'maxVideoSize');
        fetch("performanceConfiguration", {
            credentials: 'include',
            body: fd,
            method: 'POST'
        }).then(r => {
            switch (r.status) {
                case 400:
                    alert('輸入格式錯誤，請確認輸入資料是否正確');
                    break;
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
