<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <script src="resources/javascript/input_field_validation.js"></script>
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
    $(document).ready(function () {
        $('#file2').change(function () {
            var size = document.getElementById("file2").files.item(0).size;
            if (size > ${size}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("file2");
                file.value = null;
            }
        })
    })
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

    function deleted(n) {
        $.ajax({
            url: "./studyplandelete",
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
                    window.location.replace("studyplan?page=" + data);
                }
            },
            error: function () {
                alert("刪除失敗");
            }
        });
    }

    function modify(n) {
        c = confirm("確定修改此筆資料?輸入中的資料將會被清空");
        if (c) {
            $.ajax({
                url: "./studyPlanmodify",
                type: 'post',
                data:
                    {
                        id: n
                    },
                success: function (data) {
                    $("#modifyid").val(data.id);
                    $("#title").val(data.topic);
                    $("#content").val(data.description);
                    $("#file1name").html("<a href="+ data.main_file_link + "\>" + data.main_file_name + "</a>");
                    $("#file1").hide();
                    $("#file1").prop('required', false);
                    $("#file1").val(null);
                    $("#file1").prop('disabled', true);
                    $("#submit").val("修改");
                    $("#cancel").show();
                    $("#keepfile1").show();
                    $("#keepfile1").val("刪除舊檔案");
                    $("#file1name").show();
                    if (data.sub_file_name) {
                        $("#file2name").html("<a href="+ data.sub_file_link + "\>" + data.sub_file_name + "</a>");
                        $("#file2").hide();
                        $("#file2").val(null);
                        $("#file2").prop('disabled', true);
                        $("#keepfile2").show();
                        $("#keepfile2").val("刪除舊檔案");
                        $("#file2name").show();
                    } else {
                        $("#file2name").html('');
                        $("#file2").show();
                        $("#file2").prop('disabled', false);
                        $("#keepfile2").hide();
                    }
                },
                error: function () {
                    alert("失敗");
                }
            });
        }
    }

    function keepfile(n) {
        if (n == 0) {
            targetbutton = "#keepfile1";
            filename = "#file1name";
            file = "#file1";
        }
        if (n == 1) {
            targetbutton = "#keepfile2";
            filename = "#file2name";
            file = "#file2";
        }
        if ($(targetbutton).val() == "刪除舊檔案") {
            $(targetbutton).val("取消");
            $(filename).hide();
            $(file).show();
            if (n == 0)
                $("#file1").prop('required', true);
            if (n == 1)
                $("#delsub").val(1);
            $(file).prop('disabled', false);
        } else {
            $(targetbutton).val("刪除舊檔案");
            $(filename).show();
            $(file).hide();
            if (n == 0)
                $("#file1").prop('required', false);
            if (n == 1)
                $("#delsub").val(0);
            $(file).val(null);
            $(file).prop('disabled', false);
        }
    }

    function cancelmodify() {
        $("#delsub").val(0);
        $("#modifyid").val(0);
        $("#title").val("");
        $("#content").val("");
        $("#file1name").html("");
        $("#file2name").html("");
        $("#file1").show();
        $("#file1").prop('required', true);
        $("#file1").prop('disabled', false);
        $("#file1").val(null);
        $("#keepfile1").hide();
        $("#file2").show();
        $("#file2").val(null);
        $("#file2").prop('disabled', false);
        $("#keepfile2").hide();
        $("#submit").val("新增");
        $("#cancel").hide();

    }

    function validate() {
        var validation = true;
        var title = document.forms["myForm"]["title"];
        var content = document.forms["myForm"]["content"];

        var test_length_array = [title, content];
        validation &= if_length_too_long(test_length_array, studyplan_field_length_array);
        if (validation === 0)
            return false;
        else
            return true;
    }

    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>學習計畫</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年度</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">標題</th>
                        <th class="text-center" width="30%">內容簡述</th>
                        <th class="text-center">上傳時間</th>
                        <th class="text-center">學習計畫檔案</th>
                        <th class="text-center">副檔案</th>
                        <th class="text-center">操作</th>
                    </tr>
                    <c:if test="${studyPlanList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${studyPlanList.size()-1}">
                            <tr>
                                <td>${studyPlanList.get(i).term_year}</td>
                                <td>${studyPlanList.get(i).term_semester}</td>
                                <td>${studyPlanList.get(i).topic}</td>
                                <td>${studyPlanList.get(i).description}</td>
                                <td>${studyPlanList.get(i).getCreatedDateString()}</td>
                                <td>
                                    <a href="${studyPlanList.get(i).main_link}">
                                            ${studyPlanList.get(i).main_uploadFile.fileName}

                                </td>
                                <td>
                                    <a href="${studyPlanList.get(i).sub_link}">
                                            ${studyPlanList.get(i).sub_uploadFile.fileName}
                                </td>
                                <td>
                                    <button onclick="deleted('${studyPlanList.get(i).id}')" class="btn btn-warning">刪除
                                    </button>
                                    <button onclick="modify('${studyPlanList.get(i).id}')" class="btn btn-warning">修改
                                    </button>
                                </td>
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
            <div class="panel panel-heading"><span class="glyphicon glyphicon-pencil" aria-hidden="true">　</span>新增學習計畫
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="studyplan" enctype='multipart/form-data' method="POST"
                          onSubmit="return validate()">
                        <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                        <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                        <input type="text" name="page" value="${page}" style="display:none">
                        <label for="title" class="col-sm-4 control-label required" style="font-size: large">標題</label>
                        <div class="col-sm-8"><input type="text" id="title" name="title" class="form-control"
                                                     placeholder="請輸入標題" required></div>
                        <div class="col-sm-12"><br/></div>
                        <label for="file1" class="col-sm-4 required" style="font-size: large">學習計畫檔案上傳</label>
                        <div class="col-sm-8"><input type="file" class="form-control" id="file1" name="file1"
                                                     accept="${allowType}" required>
                        </div>
                        <div style="display:inline" id="file1name"></div>
                        <input type="button" id="keepfile1" onclick="keepfile(0)" class="btn btn-warning"
                               style='display:none' value="刪除舊檔案"></button>
                        <div class="col-sm-12"><br/></div>
                        <label for="content" class="col-sm-4 required" style="font-size: large">內容簡述</label>
                        <div class="col-sm-8"><textarea rows="4" class="form-control" id="content" name="content"
                                                        required></textarea></div>
                        <div class="col-sm-12"><br/></div>
                        <label for="file2" class="col-sm-4" style="font-size: large">相關檔案</label>
                        <div class="col-sm-8"><input type="file" class="form-control" id="file2" name="file2"
                                                     accept="${allowType}"></div>
                        <div>
                            <div style="display:inline" id="file2name"></div>
                            <input type="button" id="keepfile2" onclick="keepfile(1)" class="btn btn-warning"
                                   style='display:none' value="刪除舊檔案"></button>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <div class="col-sm-4">
                            <input type="submit" id="submit" class="btn btn-info" value="新增">
                            <input type="button" id="cancel" onclick="cancelmodify()" class="btn btn-warning"
                                   style='display:none' value="取消"></button>
                        </div>
                        <div class="col-sm-8"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
