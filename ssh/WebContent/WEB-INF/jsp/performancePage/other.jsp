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
    });
    $(document).ready(function () {
        $('#video').change(function () {
            var size = document.getElementById("video").files.item(0).size;
            if (size > ${videoSize}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("video");
                file.value = null;
            }
        })
    })

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

    function download(a, b) {
        window.location.href = './downloadOther?no=' + a + '&id=' + b;
    }

    function deleted(n) {
        $.ajax({
            url: "./otherDelete",
            type: 'post',
            data: {
                id: n,
                page:${page}
            },
            success: function (data) {
                if (data == 0) {
                    window.location.replace(location.href);
                } else {
                    window.location.replace("other?page=" + data);
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
                url: "./otherInfo",
                type: 'post',
                data: {
                    id: n
                },
                success: function (data) {
                    type_change(data.type)
                    $("#modifyid").val(data.id);
                    $("#name").val(data.name);
                    $("#unit").val(data.unit);
                    $("#startTime").val(data.startTime);
                    $("#endTime").val(data.endTime);
                    $("#count").val(data.count);
                    $("#content").val(data.content);
                    $("#type_selection").val(data.type);
                    $("#type").val(data.type);
                    $("#externalLink").val(data.external_link);
                    $("#file1name").html("<a href=\""+data.document_link+"\">" + data.document_original_filename + "</a>");
                    $("#document").hide();
                    $("#document").prop('required', false);
                    $("#document").val(null);
                    $("#document").prop('disabled', true);
                    $("#submit").val("修改");
                    $("#cancel").show();
                    $("#keepfile1").show();
                    if (data.video_original_filename) {
                        $("#file2name").html("<a href=\""+data.video_link+"\">" + data.video_original_filename + "</a>");
                        $("#video").hide();
                        $("#video").val(null);
                        $("#video").prop('disabled', true);
                        $("#keepfile2").show();
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
        reset_form();
        $('.form.panel').hide();
        $("#type_selection").val("");
    }

    function modify(n) {
        c = confirm("確定修改此筆資料?輸入中的資料將會被清空");
        if (c) {
            $.ajax({
                url: "./otherInfo",
                type: 'post',
                data: {
                    id: n
                },
                success: function (data) {
                    type_change(data.type)
                    $("#modifyid").val(data.id);
                    $("#name").val(data.name);
                    $("#unit").val(data.unit);
                    $("#startTime").val(data.startTime);
                    $("#endTime").val(data.endTime);
                    $("#count").val(data.count);
                    $("#content").val(data.content);
                    $("#type_selection").val(data.type);
                    $("#type").val(data.type);
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

    function reset_form() {
        $("#delsub").val(0);
        $("#modifyid").val(0);
        $("#name").val("");
        $("#unit").val("");
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


    function type_change(type) {
        reset_form();
        var type_selection = type;
        $("#type").val(type_selection);
        $('.panel').show();
        if (type_selection == "WR") {
            $(".non-wr").each(function (index, ele) {
                $(this).removeAttr("required");
                $(this).hide();
            })
            $(".startTime-label").text("日期");
            $(".document-label").text("作品成果連結");
            $(".form.panel-heading").text("其他/作品成果");
        } else {
            $(".non-wr").each(function (index, ele) {
                $(this).attr("required",true);
                $(this).show();
            })
            $(".startTime-label").text("開始日期");
            $(".document-label").text("證明文件連結");
            if (type_selection == "ST") {
                $(".form.panel-heading").text("其他/自主學習");
            } else if (type_selection == "OA") {
                $(".form.panel-heading").text("其他/其他活動");
            }

        }
    }

    function validate() {
        var validation = true;
        var name = document.forms["myForm"]["name"];
        var unit = document.forms["myForm"]["unit"];
        var startTime = document.forms["myForm"]["startTime"];
        var endTime = document.forms["myForm"]["endTime"];
        var count = document.forms["myForm"]["count"];
        var content = document.forms["myForm"]["content"];
        var documentation = document.forms["myForm"]["document"];
        var video = document.forms["myForm"]["video"];
        var external_link = document.forms["myForm"]["externalLink"];

        var test_length_array = [name, unit, startTime, endTime, count, content, documentation, video, external_link];
        validation &= if_length_too_long(test_length_array, other_field_length_array);
        validation &= if_date_valid([startTime]);
        if($("#type_selection").val()!="WR")
        {
            validation &= if_date_valid([endTime]);
        } else {
            validation &= if_start_end_time(startTime, endTime);
        }

        if (validation == 0)
            return false;
        else
            return true;
    }


    $(document).ready(function () {
        type_change('WR');

        $('#type_selection').change(function () {
            var type_selection = $("#type_selection").val();
            type_change(type_selection);
        })
        $("#type_selection").val("WR")
    });

    </script>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>其他多元表現紀錄
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">活動類別</th>
                            <th class="text-center">名稱</th>
                            <th class="text-center ">主辦單位</th>
                            <th class="text-center startTime-label">開始日期</th>
                            <th class="text-center ">結束日期</th>
                            <th class="text-center ">時數</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center document-label">證明文件連結</th>
                            <th class="text-center">影音檔案連結</th>
                            <th class="text-center">外部影音連結</th>
                            <th class="text-center">上傳時間</th>
                            <th class="text-center op-cell">操作</th>
                        </tr>
                        <c:if test="${otherList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${otherList.size()-1}">
                                <tr>
                                    <td>${otherList.get(i).getTypeC()}</td>
                                    <td>${otherList.get(i).name}</td>
                                    <td>${otherList.get(i).unit}</td>
                                    <td>${otherList.get(i).startTime}</td>
                                    <td>${otherList.get(i).endTime}</td>
                                    <td>${otherList.get(i).count}</td>
                                    <td>${otherList.get(i).content}</td>
                                    <td>
                                        <c:if test="${otherList.get(i).documentFile!=null}">
                                            <a href="${otherList.get(i).documentFile._link}">
                                                    ${otherList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${otherList.get(i).videoFile!=null}">
                                            <a href="${otherList.get(i).videoFile._link}">
                                                    ${otherList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${otherList.get(i).externalLink}" target="_blank">
                                            ${otherList.get(i).externalLink}</a>
                                    </td>
                                    <td>${otherList.get(i).getCreatedDateString()}</td>
                                    <c:if test="${otherList.get(i).student_modifiable()}">
                                        <td class="op-cell">
                                            <button onclick="deleted('${otherList.get(i).id}')"
                                                    class="btn btn-danger">
                                                刪除
                                            </button>
                                            <button onclick="modify('${otherList.get(i).id}')"
                                                    class="btn btn-warning">
                                                修改
                                            </button>
                                        </td>
                                    </c:if>
                                    <c:if test="${!otherList.get(i).student_modifiable()}">
                                        <td class=" cell-op">${otherList.get(i).selectedYear}-${otherList.get(i).getStatusC()}</td>
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

            <div class="form panel panel-info">
                <div class="form panel-heading"><span class="glyphicon glyphicon-user"></span>其他</div>
                <div class="panel-body">
                    <form name="myForm" action="other" method="POST" enctype="multipart/form-data"
                          onSubmit="return validate()">
                        <div class="form-group">
                            <label for="type_selection" class="required">類型</label>

                            <select class="form-control" id="type_selection" required>
                                <option value="" disabled selected>(請選擇)</option>
                                <option value="ST">自主學習</option>
                                <option value="WR">作品成果</option>
                                <option value="OA">其他活動</option>
                            </select>
                        </div>

                        <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                        <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                        <input type="text" name="page" value="${page}" style="display:none">
                        <input type="text" id="type" name="type" value="" style="display:none">
                        <label for="name" class="required">名稱</label>
                        <input type="text" class="form-control" id="name" name="name"
                               placeholder="若為參與校內外活動請填入活動名稱，若為作品呈現請填入作品名稱。"
                               required>
                        <label for="unit" class="required non-wr">主辦單位</label>
                        <input type="text" class="form-control non-wr" id="unit" name="unit"
                               placeholder="若為活動請填入主辦單位，校內活動請填入學校名稱，校外活動請填入活動主辦單位；非活動者免填。">
                        <label for="startTime" class="startTime-label required">開始日期</label>
                        <input type="text" class="form-control min_go_date" id="startTime" name="startTime"
                               placeholder="請點擊選擇年月日" required>
                        <label for="endTime" class="non-wr required">結束日期</label>
                        <input type="text" class="form-control min_go_date non-wr" id="endTime" name="endTime"
                               placeholder="請點擊選擇年月日" required>
                        <label for="count" class="non-wr required">時數</label>
                        <input type="number" class="form-control non-wr" id="count" name="count"
                               min="0" placeholder="請填入服務時數，為阿拉伯數字，例如一小時請填1，非活動者免填。。" required>
                        <label for="content" class="required">內容簡述</label>
                        <div><textarea rows="4" class="form-control" id="content" name="content"
                                       placeholder="請填入內容簡述" required></textarea></div>
                        <label for="document" class="document-label required">證明文件連結</label>
                        <input type="file" class="form-control" id="document" name="document" accept="${docAllowType}"
                               required>
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
