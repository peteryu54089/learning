<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<body>

<script>
function selectOnChange(index) {
    window.location = "performance?type=" + index;
}
</script>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-pencil">　</span>學生歷程檔案</div>
                <div class="panel-body">
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th colspan="2" class="text-center">基本資料</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="text-center">
                                <td>學年度學期</td>
                                <td>106-2</td>
                            </tr>
                            <tr class="text-center">
                                <td>學號</td>
                                <td>810466</td>
                            </tr>
                            <tr class="text-center">
                                <td>班級代碼</td>
                                <td>1060201</td>
                            </tr>
                            <tr class="text-center">
                                <td>班級名稱</td>
                                <td>二年二班</td>
                            </tr>
                            <tr class="text-center">
                                <td>班級座號</td>
                                <td>10</td>
                            </tr>
                            <tr class="text-center">
                                <td>中文姓名</td>
                                <td> 蘇浚賢</td>
                            </tr>
                            <tr class="text-center">
                                <td>英文姓名</td>
                                <td>David</td>
                            </tr>
                            <tr class="text-center">
                                <td>科目</td>
                                <td>理化</td>
                            </tr>
                            <tr class="text-center">
                                <td>上傳檔案</td>
                                <td>
                                    <a href="#"><span class="glyphicon glyphicon-paperclip"></span>作品.pdf</a><br>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="col-md-12 text-center">
                            <button type="button" class="btn btn-primary">認證</button>
                            <button type="button" class="btn btn-danger">取消</button>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- /container -->
    </div>
</div>
</body>
</html>
