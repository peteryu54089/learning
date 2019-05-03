<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <jsp:include page="performanceBar.jsp"></jsp:include>

    <script src="resources/javascript/datepicker-zh-TW.js"></script>
    <script src="resources/javascript/dateTth.js"></script>
	<script src="resources/javascript/input_field_validation.js"></script>

    <script>
    $(document).ready(function () {
        $('#document').change(function () {
            var size = document.getElementById("document").files.item(0).size;
            if (size > ${docSize}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("document");
                file.value = null;
            }
        })
    })
    $(document).ready(function () {
        $('#video').change(function () {
            var size = document.getElementById("video").files.item(0).size;
            if (size > ${videoSize}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("video");
                file.value = null;
            }
        })
    });
    $(function () {
        let hrefPrefix = new URLSearchParams(location.search);
        hrefPrefix.delete('page');
        let hrefPrefixStr = hrefPrefix.toString();
        if (hrefPrefixStr === '') {
            hrefPrefixStr = "?page=";
        } else {
            hrefPrefixStr = "?" + hrefPrefixStr + "&page=";
        }
        $('#pagination').pagination({
            pages:${pageAmount},
            hrefTextPrefix: hrefPrefixStr,
            currentPage:${page},
            edges: 2,
            cssStyle: 'light-theme',
            ellipsePageSet: false
        });
    });

    function keepfile(n) {
        if (n == 0) {
            targetbutton = "#keepfile1";
            filename = "#file1name";
            file = "#document";
        }
        if (n == 1) {
            targetbutton = "#keepfile2";
            filename = "#file2name";
            file = "#video";
        }
        if ($(targetbutton).val() == "刪除舊檔案") {
            $(targetbutton).val("取消");
            $(filename).hide();
            $(file).show();
            if (n == 0)
                $("#document").prop('required', true);
            if (n == 1)
                $("#delsub").val(1);
            $(file).prop('disabled', false);
        } else {
            $(targetbutton).val("刪除舊檔案");
            $(filename).show();
            $(file).hide();
            if (n == 0)
                $("#document").prop('required', false);
            if (n == 1)
                $("#delsub").val(0);
            $(file).val(null);
            $(file).prop('disabled', false);
        }
    }

    function download(a, b) {
        window.location.href = './downloadVolunteer?no=' + a + '&id=' + b;
    }

    function deleted(n) {
        $.ajax({
            url: "./volunteerDelete",
            type: 'post',
            data: {
                id: n,
                page:${page}
            },
            success: function (data) {
                if (data == 0) {
                    window.location.replace(location.href);
                } else {
                    window.location.replace("volunteer?page=" + data);
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
                url: "./volunteerInfo",
                type: 'post',
                data: {
                    id: n
                },
                success: function (data) {
                    $("#modifyid").val(data.id);
                    $("#name").val(data.name);
                    $("#place").val(data.place);
                    $("#startTime").val(data.startTime);
                    $("#endTime").val(data.endTime);
                    $("#count").val(data.count);
                    $("#content").val(data.content);
                    $("#externalLink").val(data.external_link);
                    $("#file1name").html("<a href=\""+data.document_link+"\">" + data.document_original_filename + "</a>");
                    $("#document").hide();
                    $("#document").prop('required', false);
                    $("#document").val(null);
                    $("#document").prop('disabled', true);
                    $("#submit").val("修改");
                    $("#cancel").show();
                    $("#keepfile1").show();
                    $("#keepfile1").val("刪除舊檔案");
                    $("#file1name").show();
                    if (data.video_original_filename) {
                        $("#file2name").html("<a href=\""+data.video_link+"\">" + data.video_original_filename + "</a>");
                        $("#video").hide();
                        $("#video").val(null);
                        $("#video").prop('disabled', true);
                        $("#keepfile2").show();
                        $("#keepfile2").val("刪除舊檔案");
                        $("#file2name").show();
                    } else {
                        $("#file2name").html('');
                        $("#video").show();
                        $("#video").prop('disabled', false);
                        $("#keepfile2").hide();
                    }

                },
                error: function () {
                    alert("失敗");
                }
            });
        }
    }

    function cancelmodify() {
        $("#delsub").val(0);
        $("#modifyid").val(0);
        $("#name").val("");
        $("#place").val("");
        $("#startTime").val("");
        $("#endTime").val("");
        $("#count").val("");
        $("#content").val("");
        $("#type").val("");
        $("#externalLink").val("");
        $("#file1name").html("");
        $("#file2name").html("");
        $("#document").show();
        $("#document").prop('required', true);
        $("#document").prop('disabled', false);
        $("#document").val(null);
        $("#keepfile1").hide();
        $("#video").show();
        $("#video").val(null);
        $("#video").prop('disabled', false);
        $("#keepfile2").hide();
        $("#submit").val("新增");
        $("#cancel").hide();
    }

    function validate() {
        var validation = true;
        var name = document.forms["myForm"]["name"];
        var place = document.forms["myForm"]["place"];
        var startTime = document.forms["myForm"]["startTime"];
        var endTime = document.forms["myForm"]["endTime"];
        var count = document.forms["myForm"]["count"];
        var content = document.forms["myForm"]["content"];
        var documentation = document.forms["myForm"]["document"];
        var video = document.forms["myForm"]["video"];
        var external_link = document.forms["myForm"]["externalLink"];

        var test_length_array = [name, place, startTime, endTime, count, content, documentation, video, external_link];
        validation &= if_length_too_long(test_length_array, volunteer_field_length_array);
        validation &= if_date_valid([startTime, endTime]);
        validation &= if_start_end_time(startTime, endTime);
        if (validation == 0)
            return false;
        else
            return true;
    }

    $(document).ready(function () {
        $('#document').change(function () {
            var size = document.getElementById("document").files.item(0).size;
            if (size > ${docSize}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("document");
                file.value = null;
            }
        })
    })
    </script>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>志工服務</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">服務名稱</th>
                            <th class="text-center">服務單位</th>
                            <th class="text-center">開始日期</th>
                            <th class="text-center">結束日期</th>
                            <th class="text-center">時數</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center">證明文件連結</th>
                            <th class="text-center">影音檔案連結</th>
                            <th class="text-center">外部影音連結</th>
                            <th class="text-center">上傳時間</th>
                            <th class="text-center cell-op">操作</th>
                        </tr>
                        <c:if test="${volunteerList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${volunteerList.size()-1}">
                                <tr>
                                    <td>${volunteerList.get(i).name}</td>
                                    <td>${volunteerList.get(i).place}</td>
                                    <td>${volunteerList.get(i).startTime}</td>
                                    <td>${volunteerList.get(i).endTime}</td>
                                    <td>${volunteerList.get(i).count}</td>
                                    <td>${volunteerList.get(i).content}</td>
                                    <td>
                                        <c:if test="${volunteerList.get(i).documentFile!=null}">
                                            <a href="${volunteerList.get(i).documentFile._link}">
                                                    ${volunteerList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${volunteerList.get(i).videoFile!=null}">
                                            <a href="${volunteerList.get(i).videoFile._link}">
                                                    ${volunteerList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${volunteerList.get(i).externalLink}" target="_blank">
                                            ${volunteerList.get(i).externalLink}</a>
                                    </td>
                                    <td>${volunteerList.get(i).getCreatedDateString()}</td>
                                    <c:if test="${volunteerList.get(i).student_modifiable()}">
                                        <td class=" cell-op">
                                            <button onclick="deleted('${volunteerList.get(i).id}')"
                                                    class="btn btn-danger">
                                                刪除
                                            </button>
                                            <button onclick="modify('${volunteerList.get(i).id}')"
                                                    class="btn btn-warning">
                                                修改
                                            </button>
                                        </td>
                                    </c:if>
                                    <c:if test="${!volunteerList.get(i).student_modifiable()}">
                                        <td class=" cell-op">${volunteerList.get(i).selectedYear}-${volunteerList.get(i).getStatusC()}</td>
                                    </c:if>
                                </tr>


                            </c:forEach>
                        </c:if>
                    </table>
                </div>
                <div id="pagination"></div>
            </div>
        </div>
    </div>

    <div class="row submit-row">
        <div class="col-sm-12">
            <div class="panel panel-info">
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>志工服務</div>
                <div class="panel-body">
                    <div class="form-group">
                        <form name="myForm" action="volunteer" onSubmit="return validate()" method="POST"
                              enctype="multipart/form-data">
                            <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                            <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                            <input type="text" name="page" value="${page}" style="display:none">
                            <label for="name" class="required">服務名稱</label>
                            <input type="text" class="form-control" id="name" name="name" placeholder="請填入服務名稱。"
                                   required>
                            <label for="place" class="required">服務單位</label>
                            <input type="text" class="form-control" id="place" name="place" placeholder="請填入服務單位名稱。"
                                   required>
                            <label for="startTime" class="required">開始日期</label>
                            <input type="text" class="form-control min_go_date" id="startTime" name="startTime"
                                   placeholder="請點擊選擇年月日" required>
                            <label for="endTime" class="required">結束日期</label>
                            <input type="text" class="form-control min_go_date" id="endTime" name="endTime"
                                   placeholder="請點擊選擇年月日" required>
                            <label for="count" class="required">時數</label>
                            <input type="number" class="form-control" id="count" name="count"
                                   placeholder="請填入服務時數，為阿拉伯數字，例如一小時請填1。" required>
                            <label for="content" class="required">內容簡述</label>
                            <div><textarea rows="4" class="form-control" id="content" name="content"
                                           placeholder="請100字內之服務內容簡述，例如服務台值班、打掃環境等" required></textarea></div>
                            <label for="document" class="required">證明文件連結</label>
                            <input type="file" class="form-control" id="document" name="document"
                                   accept="${docAllowType}" required>
                            <div style="display:inline" id="file1name"></div>
                            <div>
                                <input type="button" id="keepfile1" onclick="keepfile(0)" class="btn btn-danger"
                                       style='display:none' value="刪除舊檔案">
                            </div>
                            <label for="video">影音檔案</label>
                            <input type="file" class="form-control" id="video" name="video" accept="${videoAllowType}">
                            <div style="display:inline" id="file2name"></div>
                            <div>
                                <input type="button" id="keepfile2" onclick="keepfile(1)" class="btn btn-danger"
                                       style='display:none' value="刪除舊檔案">
                            </div>
                            <label for="externalLink">影音連結</label>
                            <input type="text" class="form-control" id="externalLink" name="externalLink"
                                   placeholder="非必填，有上傳影音才可填影音連結">
                            <br/>
                            <input type="submit" class="btn btn-info" value="送出">
                            <input type="button" id="cancel" onclick="cancelmodify()" class="btn btn-warning"
                                   style='display:none' value="取消">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
