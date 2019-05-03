<%--@elvariable id="documentList" type="java.util.List<model.Document>"--%>
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
<script src="resources/javascript/input_field_validation.js"></script>
<script>
$(document).ready(function () {
    $('#file1').change(function () {
        var size = this.files.item(0).size;
        if (size > ${size}) {
            alert("超過上傳文件大小限制");
            var file = document.getElementById("file1");
            file.value = null;
        }
    })
})


function deleted(n) {
    $.ajax({
        url: "./documentdelete",
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
                window.location.replace("document?page=" + data);
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

function validate() {
    var validation = true;
    var title = document.forms["myForm"]["title"];
    var content = document.forms["myForm"]["content"];

    var test_length_array = [title, content];
    validation &= if_length_too_long(test_length_array, document_field_length_array);
    if (validation === 0)
        return false;
    else
        return true;
}
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="" id="mainOtherDocContainer">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>其他文件管理
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center;table-layout: fixed; width:100%">
                    <tr>
                        <th class="text-center">學年度</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">標題</th>
                        <th class="text-center" width="30%">說明</th>
                        <th class="text-center">上傳時間</th>
                        <th class="text-center">原檔名</th>
                        <th class="text-center">操作</th>
                    </tr>
                    <c:if test="${documentList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${documentList.size()-1}">
                            <tr>
                                <td>${documentList.get(i).term_year}</td>
                                <td>${documentList.get(i).term_semester}</td>
                                <td>${documentList.get(i).topic}</td>
                                <td>${documentList.get(i).description}</td>
                                <td>${documentList.get(i).createdDateString}</td>
                                <td>${documentList.get(i).uploadFile.fileName}</td>
                                <td>
                                    <b>
                                        <a href="${documentList.get(i).link}">
                                                下載
                                        </a>
                                    </b>

                                    <c:if test="${size>0}">
                                    <button onclick="deleted('${documentList.get(i).id}')"
                                            class="btn btn-danger">刪除
                                    </button>
                                    </c:if>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </div>
            <div id="pagination"></div>
        </div>
    </div>

    <c:if test="${size>0}">
        <div class="">
            <div class="panel panel-primary">
                <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>新增文件
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <form name="myForm" action="document" enctype='multipart/form-data' method="POST"
                              onSubmit="return validate()">
                            <label for="title" class="col-sm-4 control-label required" style="font-size: large">標題</label>
                            <div class="col-sm-8"><input type="text" id="title" name="title" class="form-control"
                                                         placeholder="請輸入標題" required></div>
                            <div class="col-sm-12"><br/></div>
                            <label for="file1" class="col-sm-4 required" style="font-size: large">選擇檔案1</label>
                            <div class="col-sm-8"><input type="file" class="form-control" id="file1" name="file1"
                                                         accept="${AllowType}" required>
                            </div>
                            <div class="col-sm-12"><br/></div>
                            <label for="content" class="col-sm-4 required" style="font-size: large">內容簡述</label>
                            <div class="col-sm-8"><textarea rows="4" class="form-control" id="content" name="content"
                                                            required></textarea></div>
                            <div class="col-sm-4"><input type="submit" class="btn btn-info" value="新增"></div>
                            <div class="col-sm-8"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
