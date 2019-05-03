<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>高中生歷程系統</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">

    <script>
    function validate() {
        var x = document.forms["myForm"]["document"].value;
        var parts = x.split('.');
        var ext = parts[parts.length - 1];
        if (ext.toLowerCase() != "pdf") {
            alert("證明文件需要為PDF檔案");
            return false;
        }
    }

    $(window).on('load', function () {
        $('#myModal').modal('show');
    });

    $(document).ready(function () {
        $('#document').change(function () {
            var size = document.getElementById("document").files.item(0).size;
            if (size > ${size}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("document");
                file.value = null;
            }
        })
    })
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="col-sm-12">
        <div class="panel panel-success">
            <div class="panel-heading">課程成果新增</div>
            <div class="panel-body">
                <form action="" id="myForm" enctype='multipart/form-data' onsubmit="return validate()" method="POST">
                    <div class="form-group">
                        <label for="name">學生姓名:</label>
                        <input type="text" class="form-control" id="name" name="name" value="${student.cname}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="reg">學生學號:</label>
                        <input type="text" class="form-control" id="reg" name="reg" value="${student.reg_no}"
                               readonly>
                    </div>
                    <div class="form-group">
                        <label for="year">學年度:</label>
                        <input type="text" class="form-control" id="year" name="year"
                               value="${course.term_year}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="sem">學期:</label>
                        <input type="text" class="form-control" id="sem" name="sem" value="${course.term_sem}"
                               readonly>
                    </div>
                    <div class="form-group">
                        <label for="teacher">授課老師:</label>
                        <input type="text" class="form-control" id="teacher" name="teacher"
                               value="${teacher.staff_cname}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="cname">課程名稱:</label>
                        <input type="text" class="form-control" id="cname" name="cname" value="${course.course_cname}"
                               readonly>
                    </div>
                    <div class="form-group">
                        <label for="comment">成果簡述:</label>
                        <textarea class="form-control" rows="5" id="comment" name="comment" maxlength="200"
                                  required></textarea>
                    </div>
                    <div>
                        <label for="document">證明文件連結</label>
                        <input type="file" class="form-control" id="document" name="document" required>
                    </div>
                    <input type="hidden" name="code" id="code" value="${course.course_num}">
                    <br/>
                    <input type="submit" class="btn btn-info" value="送出">
                </form>
            </div>
        </div>
    </div>
</div><!-- /container -->

<div id="myModal" class="modal fade" authority="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="panel panel-danger">
                    <div class="panel-heading">說明</div>
                    <div class="panel-body">
                        ${student.cname}同學您好: <br/>
                        由於您尚未新增${course.term_year} 學年度 ${course.term_sem} <u>${course.course_cname}</u> 課程之學習成果。 <br/>
                        因此如果欲新增本堂課之成果紀錄，請先新增資料。 <br/><br/>
                        <p><span class="glyphicon glyphicon-warning-sign"> 請注意學年度、學期及基本資料是否正確。</span></p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">我知道了</button>
            </div>
        </div>

    </div>
</div>

</body>
</html>
