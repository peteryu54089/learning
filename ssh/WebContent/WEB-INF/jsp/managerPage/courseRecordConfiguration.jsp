<%--@elvariable id="courseConfig" type="model.SystemConfig.CourseStudyRecord"--%>
<%--- <%@ page import="util.DateUtils" %> ---%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
                <div class="panel-heading">課程成果提交設定</div>
                <div class="panel-body">
                    <form id="form" class="form">
                        <div class="row">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="activeYear">上傳學年</label>
                                    <input name="activeYear"
                                           id="activeYear" type="number" required class="form-control"
                                           value="<c:out value="${courseConfig.activeYear}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="activeSem">上傳學期</label>
                                    <input name="activeSem"
                                           id="activeSem" type="number" required class="form-control"
                                           value="<c:out value="${courseConfig.activeSem}"/>"
                                           min="1" max="4"
                                    />
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="studentStartDate">學生上傳起始日</label>
                                    <input name="studentStartDate"
                                           id="studentStartDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(courseConfig.studentStartDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="studentEndDate">學生上傳截止日</label>
                                    <input name="studentEndDate"
                                           id="studentEndDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(courseConfig.studentEndDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>

                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="teacherStartDate">教師驗證起始日</label>
                                    <input name="teacherStartDate"
                                           id="teacherStartDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(courseConfig.teacherStartDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="teacherEndDate">教師驗證截止日</label>
                                    <input name="teacherEndDate"
                                           id="teacherEndDate" type="datetime-local" required class="form-control"
                                           value="<c:out value="${DateUtils.formatAsDateTimeInput(courseConfig.teacherEndDateTime)}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>

                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="maxAmountPerRecord">每個成果可上傳之檔案數量</label>
                                    <input name="maxAmountPerRecord"
                                           id="maxAmountPerRecord" type="number" required class="form-control"
                                           min="1"
                                           value="<c:out value="${courseConfig.maxAmountPerRecord}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="maxSubmitAmount">每學期可勾選課程學習成果之數量</label>
                                    <input name="maxSubmitAmount"
                                           id="maxSubmitAmount" type="number" required class="form-control"
                                           min="1"
                                           value="<c:out value="${courseConfig.maxSubmitAmount}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="maxSize">大小限制 (MB)</label>
                                    <input name="maxSize"
                                           id="maxSize" type="number" required class="form-control size-input"
                                           min="1" max="10"
                                           value="<c:out value="${courseConfig.maxSize}"/>"/>
                                </div>
                                <div class="form-group">
                                    <label for="allowTypes">允許檔案類型</label>
                                    <input name="allowTypes"
                                           id="allowTypes" type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${courseConfig.allowTypesString}"/>"/>
                                    <p class="text-muted">
                                        每個副檔名使用"<span style="color: red">, </span>"分離
                                    </p>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <hr>
                            </div>

                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label for="mailInterval">通知教師認證Email頻率 (天)</label>
                                    <input name="mailInterval"
                                           id="mailInterval" type="number" required class="form-control"
                                           min="1"
                                           value="<c:out value="${courseConfig.mailInterval}"/>"/>
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
        handleSizeInput(fd, 'maxSize');
        fetch("courseRecordConfiguration", {
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
