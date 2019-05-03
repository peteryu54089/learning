<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 下午 12:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<script>
function validate() {

}

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
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-file" aria-hidden="true">　</span>生涯規劃書管理</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">Label1</th>
                        <th class="text-center">Label2</th>
                        <th class="text-center" width="40%">Label3</th>
                        <th class="text-center" width="30%">Label4</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>2</td>
                        <td>
                            <button class="btn btn-warning">button1</button>
                            <button class="btn btn-primary">button2</button>
                        </td>
                        <td>
                            <button class="btn btn-info">button3</button>
                            <button class="btn btn-danger">button4</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>新增生涯規劃書
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="autobiography" enctype='multipart/form-data'
                          onsubmit="return validate()" method="POST">
                        <div class="rows">
                            <label for="select1" class="col-sm-4 control-label" style="font-size: large">select1</label>
                            <div class="col-sm-8"><select id="select1" name="select1" class="form-control">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                            </select>
                            </div>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <div class="rows">
                            <div class="col-sm-1"></div>
                            <label for="text1" class="col-sm-3" style="font-size: large">Label6</label>
                            <div class="col-sm-6"><input type="text" class="form-control" id="text1" name="text1"
                                                         required></div>
                            <div class="col-sm-2">
                                <button class="btn btn-info">button5</button>
                            </div>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <div class="rows">
                            <div class="col-sm-1"></div>
                            <label for="text2" class="col-sm-3" style="font-size: large">Label7</label>
                            <div class="col-sm-6"><input type="text" class="form-control" id="text2" name="text2"
                                                         required></div>
                            <div class="col-sm-2">
                                <button class="btn btn-info">button6</button>
                            </div>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <div class="rows">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-3">
                                <button class="btn btn-primary">button7</button>
                            </div>
                            <div class="col-sm-8"></div>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <div class="rows">
                            <div class="col-sm-3"><input type="submit" class="btn btn-info" value="button8"></div>
                            <div class="col-sm-9"></div>
                        </div>
                        <div class="col-sm-12" style="height: 50px"><br/></div>
                        <div class="rows">
                            <div class="col-sm-2"><label for="file1" style="font-size: large">選擇檔案1</label></div>
                            <div class="col-sm-5"><input type="file" id="file1" name="file1" class="form-control"
                                                         required></div>
                            <div class="col-sm-2">
                                <button class="btn btn-danger">button9</button>
                            </div>
                        </div>
                    </form>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
