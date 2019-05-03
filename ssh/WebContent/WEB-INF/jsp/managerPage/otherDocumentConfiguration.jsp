<%--@elvariable id="otherDocumentConfig" type="model.SystemConfig.OtherDocument"--%>
<%--@elvariable id="autoBioConfig" type="model.SystemConfig.AutoBio"--%>
<%--@elvariable id="studyPlanConfig" type="model.SystemConfig.StudyPlan"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <div class="panel-heading">其他文件管理設定</div>
                <div class="panel-body">
                    <form id="form" class="form">
                        <div class="row">
                            <div class="col-xs-2">
                                <div class="form-group">
                                    <label for="otherMaxSize">大小限制 (MB)</label>
                                    <input name="otherMaxSize"
                                           id="otherMaxSize" type="number" required class="form-control size-input"
                                           min="1" max="10"
                                           value="<c:out value="${otherDocumentConfig.maxSize}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <div class="form-group">
                                    <label for="otherMaxAmount">數量限制</label>
                                    <input name="otherMaxAmount"
                                           id="otherMaxAmount" type="number" required class="form-control"
                                           min="1"
                                           value="<c:out value="${otherDocumentConfig.maxAmount}"/>"/>
                                </div>
                            </div>
                            <div class="col-xs-8">
                                <div class="form-group">
                                    <label for="otherAllowTypes">檔案格式限制</label>
                                    <input name="otherAllowTypes"
                                           id="otherAllowTypes" type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${otherDocumentConfig.allowTypesString}"/>"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">

                                <div class="form-group">
                                    <label for="autoBioMaxSize">大小限制 (MB)-自傳</label>
                                    <input id="autoBioMaxSize"
                                           name="autoBioMaxSize"
                                           type="number" required class="form-control size-input"
                                           min="1" max="10"
                                           value="<c:out value="${autoBioConfig.maxSize}"/>"
                                    />
                                </div>
                            </div>
                            <div class="col-xs-10">
                                <div class="form-group">
                                    <label for="autoBioAllowTypes">檔案格式限制</label>
                                    <input id="autoBioAllowTypes"
                                           name="autoBioAllowTypes"
                                           type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${autoBioConfig.allowTypesString}"/>"
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">
                                <div class="form-group">
                                    <label for="studyPlanMaxSize">大小限制 (MB)-學習計畫</label>
                                    <input id="studyPlanMaxSize"
                                           name="studyPlanMaxSize"
                                           type="number" required class="form-control size-input"
                                           min="1" max="10"
                                           value="<c:out value="${studyPlanConfig.maxSize}"/>"
                                    />
                                </div>
                            </div>
                            <div class="col-xs-10">
                                <div class="form-group">
                                    <label for="studyPlanAllowTypes">檔案格式限制</label>
                                    <input id="studyPlanAllowTypes"
                                           name="studyPlanAllowTypes"
                                           type="text" required class="form-control"
                                           pattern="^((\.[a-z0-9]+, )*(\.[a-z0-9]+)*)$"
                                           value="<c:out value="${studyPlanConfig.allowTypesString}"/>"
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="row">
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
        handleSizeInput(fd, 'otherMaxSize');
        handleSizeInput(fd, 'autoBioMaxSize');
        handleSizeInput(fd, 'studyPlanMaxSize');

        fetch("otherDocumentConfiguration", {
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
