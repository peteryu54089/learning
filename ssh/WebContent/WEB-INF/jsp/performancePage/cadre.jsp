<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
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

    function deleted(n) {
        $.ajax({
            url: "./cadredelete",
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
                    window.location.replace("cadre?page=" + data);
                }
            },
            error: function () {
                alert("刪除失敗");
            }
        });
    }

    function download(a, b) {
        window.location.href = './downloadCadre?no=' + a + '&id=' + b;
    }

    function modify(n) {
        c = confirm("確定修改此筆資料?輸入中的資料將會被清空");
        if (c) {
            $.ajax({
                url: "./cadreInfo",
                type: 'post',
                data:
                    {
                        id: n
                    },
                success: function (data) {
                    $("#modifyid").val(data.id);
                    $("#unit").val(data.unit);
                    $("#startTime").val(data.startTime);
                    $("#endTime").val(data.endTime);
                    $("#term").val(data.term);
                    $("#jobLevel").val(data.level);
                    $("#job").val(data.job);
                    $("#externalLink").val(data.external_link);
                    $("#content").val(data.content);
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

    function cancelmodify() {
        $("#delsub").val(0);
        $("#modifyid").val(0);
        $("#unit").val("");
        $("#startTime").val("");
        $("#endTime").val("");
        $("#term").val("");
        $("#jobLevel").val("0");
        $("#job").val("");
		$("#content").val("");
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
	 var unit = document.forms["myForm"]["unit"];
	 var startTime = document.forms["myForm"]["startTime"];
	 var endTime = document.forms["myForm"]["endTime"];
	 var term = document.forms["myForm"]["term"];
	 var job = document.forms["myForm"]["job"];
	 var jobLevel = document.forms["myForm"]["jobLevel"]
	 var content = document.forms["myForm"]["content"];
	 var documentation = document.forms["myForm"]["document"];
	 var video = document.forms["myForm"]["video"];
	 var external_link = document.forms["myForm"]["externalLink"];

	 var test_length_array = [unit, startTime, endTime, term, jobLevel, job, content, documentation, video, external_link];
	 validation &= if_length_too_long(test_length_array, cadre_field_length_array);
	 validation &= if_date_valid([startTime, endTime]);
	 validation &= if_start_end_time(startTime, endTime);
	 validation &= if_semester_valid(term);
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>幹部經歷</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">來源</th>
                            <th class="text-center">單位名稱</th>
                            <th class="text-center">日期</th>
                            <th class="text-center">學期</th>
                            <th class="text-center">幹部等級</th>
                            <th class="text-center">職務</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center">證明文件</th>
                            <th class="text-center">影音檔案</th>
                            <th class="text-center">外部連結</th>
                            <th class="text-center">上傳時間</th>
                            <th class="text-center cell-op">操作</th>
                        </tr>

                        <%--@elvariable id="cadreList" type="java.util.List<model.Cadre>"--%>
                        <c:if test="${cadreList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${cadreList.size()-1}">
                                <tr>
                                    <td>${cadreList.get(i).getSourceC()}</td>
                                    <td>${cadreList.get(i).unit}</td>
                                    <td>${cadreList.get(i).startTime}-${cadreList.get(i).endTime}</td>
                                    <td>${cadreList.get(i).term}</td>
                                    <td>${cadreList.get(i).getLevelC()}</td>
                                    <td>${cadreList.get(i).job}</td>
                                    <td>${cadreList.get(i).content}</td>
                                    <td>
                                        <c:if test="${cadreList.get(i).documentFile!=null}">
                                            <a href="${cadreList.get(i).documentFile._link}">
                                                    ${cadreList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${cadreList.get(i).videoFile!=null}">
                                            <a href="${cadreList.get(i).videoFile._link}">
                                                    ${cadreList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${cadreList.get(i).externalLink}" target="_blank">
                                            ${cadreList.get(i).externalLink}</a>
                                    </td>
                                    <td>${cadreList.get(i).getCreatedDateString()}</td>
                                    <c:if test="${cadreList.get(i).student_modifiable()}">
                                        <td class=" cell-op">
                                            <button onclick="deleted('${cadreList.get(i).id}')" class="btn btn-danger">
                                                刪除
                                            </button>
                                            <button onclick="modify('${cadreList.get(i).id}')" class="btn btn-warning">
                                                修改
                                            </button>
                                        </td>
                                    </c:if>
                                    <c:if test="${!cadreList.get(i).student_modifiable()}">
                                        <td class=" cell-op">${cadreList.get(i).selectedYear}-${cadreList.get(i).getStatusC()}</td>
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>幹部經歷(非本校幹部)</div>
                <div class="panel-body">
                    <div class="form-group">
                        <form name="myForm" action="cadre" enctype='multipart/form-data' onSubmit="return validate()"
                              method="POST">
                            <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                            <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                            <input type="text" name="page" value="${page}" style="display:none">
                            <label for="unit" class="required">單位名稱</label>
                            <input type="text" class="form-control" id="unit" name="unit"
                                   placeholder="請填入單位名稱。如外校熱舞社、兼任外校學生議會等。"
                                   required>
                            <label for="startTime" class="required">開始日期</label>
                            <input type="text" id="startTime" name="startTime" class="min_go_date form-control"
                                   placeholder="請點擊選擇年月日" required>
                            <label for="endTime" class="required">結束日期</label>
                            <input type="text" id="endTime" name="endTime" class="min_go_date form-control"
                                   placeholder="請點擊選擇年月日" required>
                            <label for="term" class="required">學年度學期</label>
                            <input type="text" class="form-control" id="term" name="term"
                                   placeholder="請填入學年度學期，例如:106年上學期請填入1061、107年下學期請填入1072。" required>
                            <label for="jobLevel" class="required">幹部等級</label>
                            <select class="form-control" id="jobLevel" name="jobLevel" required>
                                <option value="" disabled selected>(請選擇)</option>
                                <option value="3">社團幹部(如社長)</option>
                                <option value="4">實習幹部(如工廠領班、器材管理)</option>
                                <option value="5">其他幹部</option>
                            </select>
                            <label for="job" class="required">擔任職務</label>
                            <input type="text" class="form-control" id="job" name="job"
                                   placeholder="請填入擔任的職務，例如社長、副社長、秘書長、隊長、副議長等。"
                                   required>
                            <label for="content">內容簡述</label>
                            <div><textarea rows="4" class="form-control" id="content" name="content" placeholder="請填入幹部經歷內容簡述"></textarea></div>
                            <label for="document" class="required">證明文件</label>
                            <input type="file" class="form-control" id="document" name="document" accept="${docAllowType}" required>
                            <div style="display:inline" id="file1name"></div>
                            <div>
                                <input type="button" id="keepfile1" onclick="keepfile(0)" class="btn btn-danger"
                                       style='display:none' value="刪除舊檔案"></button>
                            </div>
                            <label for="video">影音檔案</label>
                            <input type="file" class="form-control" id="video" name="video" accept="${videoAllowType}">
                            <div style="display:inline" id="file2name"></div>
                            <div>
                                <input type="button" id="keepfile2" onclick="keepfile(1)" class="btn btn-danger"
                                       style='display:none' value="刪除舊檔案"></button>
                            </div>
                            <label for="externalLink">影音連結</label>
                            <input type="text" class="form-control" id="externalLink" name="externalLink"
                                   placeholder="非必填，有上傳影音才可填影音連結">
                            <br/>
                            <input type="submit" id="submit" class="btn btn-info" value="送出">
                            <input type="button" id="cancel" onclick="cancelmodify()" class="btn btn-warning"
                                   style='display:none' value="取消"></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
