<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/5/8
  Time: 上午 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <script>
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
    <div class="">
        <div class="panel panel-primary">
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>編輯個人諮詢內容
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="singlecontent" enctype='multipart/form-data' method="POST">
                        <label for="text1" class="col-sm-4 control-label" style="font-size: large">Text1</label>
                        <div class="col-sm-8"><input type="text" id="text1" name="text1" class="form-control" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text2" class="col-sm-4" style="font-size: large">Text2</label>
                        <div class="col-sm-6"><input type="text" class="form-control" id="text2" name="text2" required>
                        </div>
                        <div class="col-sm-2">
                            <button class="btn btn-warning">button1</button>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text3" class="col-sm-4" style="font-size: large">Text3</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text3" name="text3" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text4" class="col-sm-4" style="font-size: large">Text4</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text4" name="text4" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text5" class="col-sm-4" style="font-size: large">Text5</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text5" name="text5" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text6" class="col-sm-4" style="font-size: large">Text6</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text6" name="text6" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text7" class="col-sm-4" style="font-size: large">Text7</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text7" name="text7" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text8" class="col-sm-4" style="font-size: large">Text8</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text8" name="text8" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="text9" class="col-sm-4" style="font-size: large">Text9</label>
                        <div class="col-sm-8"><input type="text" class="form-control" id="text9" name="text9" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="file1" class="col-sm-4" style="font-size: large">選擇檔案1</label>
                        <div class="col-sm-8"><input type="file" class="form-control" id="file1" name="file1" required>
                        </div>
                        <div class="col-sm-12"><br/></div>
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
