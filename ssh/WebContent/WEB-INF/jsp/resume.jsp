<%--@elvariable id="resumeList" type="java.util.List<model.Resume>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 11:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<script>
$(document).ready(function () {
    $('#file1').change(function () {
        var size = document.getElementById("file1").files.item(0).size;
        if (size > ${size}) {
            alert("超過上傳文件大小限制");
            var file = document.getElementById("file1");
            file.value = null;
        }
    })
})

function download(id) {
    window.location.href = './downloadResume?id=' + id;
}

function deleted(n) {
    $.ajax({
        url: "./resumedelete",
        type: 'post',
        data:
            {
                id: n,
                page:${page}
            },
        success: function (data) {
            if (data == 0) {
                window.location.replace(location.href);
            } else {
                window.location.replace("resume?page=" + data);
            }
        },
        error: function () {
            alert("刪除失敗");
        }
    });
}

$(function () {
    $('#pagination').pagination({
        pages:${pageAmount},
        hrefTextPrefix: "?page=",
        currentPage:${page},
        edges: 2,
        cssStyle: 'light-theme',
        ellipsePageSet: false
    });
});
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>履歷表</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年度</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">標題</th>
                        <th class="text-center" width="30%">說明</th>
                        <th class="text-center">上傳時間</th>
                        <th class="text-center">操作</th>
                    </tr>
                    <c:if test="${resumeList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${resumeList.size()-1}">
                            <tr>
                                <td>${resumeList.get(i).term_year}</td>
                                <td>${resumeList.get(i).term_semester}</td>
                                <td>${resumeList.get(i).topic}</td>
                                <td>${resumeList.get(i).description}</td>
                                <td>${resumeList.get(i).getCreatedDateString()}</td>
                                <td>
                                    <button onclick="download('${resumeList.get(i).id}')"
                                            class="btn btn-warning">下載
                                    </button>
                                    <button onclick="deleted('${resumeList.get(i).id}')" class="btn btn-warning">
                                        刪除
                                    </button>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </div>
            <div id="pagination"></div>
        </div>
    </div>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>新增履歷表
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="resume" enctype='multipart/form-data' method="POST">
                        <label for="title" class="col-sm-4 control-label" style="font-size: large">標題</label>
                        <div class="col-sm-8"><input type="text" id="title" name="title" class="form-control"
                                                     placeholder="請輸入標題" required></div>
                        <div class="col-sm-12"><br/></div>
                        <label for="file1" class="col-sm-4" style="font-size: large">選擇檔案1</label>
                        <div class="col-sm-8"><input type="file" class="form-control" id="file1" name="file1" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="content" class="col-sm-4" style="font-size: large">內容簡述</label>
                        <div class="col-sm-8"><textarea rows="4" class="form-control" id="content" name="content"
                                                        required></textarea></div>
                        <div class="col-sm-4"><input type="submit" class="btn btn-info" value="新增"></div>
                        <div class="col-sm-8"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
