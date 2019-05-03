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

        function deleted(n) {
            $.ajax({
                url: "./competitiondelete",
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
                        window.location.replace("competition?page=" + data);
                    }
                },
                error: function () {
                    alert("刪除失敗");
                }
            });
        }

        function download(a, b) {
            window.location.href = './downloadCompetition?no=' + a + '&id=' + b;
        }

        function modify(n) {
            c = confirm("確定修改此筆資料?輸入中的資料將會被清空");
            if (c) {
                $.ajax({
                    url: "./competitionInfo",
                    type: 'post',
                    data:
                        {
                            id: n
                        },
                    success: function (data) {
                        $("#modifyid").val(data.id);
                        $("#name").val(data.name);
                        $("#item").val(data.item);
                        $("#field").val(data.field);
                        $("#term").val(data.term);
                        $("#level").val(data.level);
                        $("#award").val(data.award);
                        $("#time").val(data.time);
                        $("#content").val(data.content);
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
			$("#name").val("");
			$("#item").val("");
			$("#field").val("");
			$("#term").val("");
			$("#level").val("");
			$("#award").val("");
			$("#time").val("");
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
		 var item = document.forms["myForm"]["item"];
		 var field = document.forms["myForm"]["field"];
		 var level = document.forms["myForm"]["level"];
		 var award = document.forms["myForm"]["award"];
		 var time = document.forms["myForm"]["time"];
		 var content = document.forms["myForm"]["content"];
		 var type = document.forms["myForm"]["type"];
		 var documentation = document.forms["myForm"]["document"];
		 var video = document.forms["myForm"]["video"];
		 var external_link = document.forms["myForm"]["externalLink"];

		 var test_length_array = [name, item, field, level, award, time, content, type , documentation, video, external_link];
		 validation &= if_length_too_long(test_length_array, competition_field_length_array);
		 validation &= if_date_valid([time]);
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>競賽成果</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">競賽名稱</th>
                            <th class="text-center">項目</th>
                            <th class="text-center">競賽領域代碼</th>
                            <th class="text-center">競賽等級代碼</th>
                            <th class="text-center">獎項</th>
                            <th class="text-center">結果公佈日期</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center">團體參與代碼</th>
                            <th class="text-center">證明文件連結</th>
                            <th class="text-center">影音檔案連結</th>
                            <th class="text-center">外部影音連結</th>
                            <th class="text-center">上傳時間</th>
                            <th class="text-center cell-op">操作</th>
                        </tr>
                        <c:if test="${competitionList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${competitionList.size()-1}">
                                <tr>
                                    <td>${competitionList.get(i).name}</td>
                                    <td>${competitionList.get(i).item}</td>
                                    <td>${field.getHashMap()[competitionList.get(i).field]}</td>
                                    <td>${competitionList.get(i).getLevelC()}</td>
                                    <td>${competitionList.get(i).award}</td>
                                    <td>${competitionList.get(i).time}</td>
                                    <td>${competitionList.get(i).content}</td>
                                    <td>${competitionList.get(i).getTypeC()}</td>
                                    <td>
                                        <c:if test="${competitionList.get(i).documentFile!=null}">
                                            <a href="${competitionList.get(i).documentFile._link}">
                                                    ${competitionList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${competitionList.get(i).videoFile!=null}">
                                            <a href="${competitionList.get(i).videoFile._link}">
                                                    ${competitionList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${competitionList.get(i).externalLink}" target="_blank">
                                            ${competitionList.get(i).externalLink}</a>
                                    </td>
                                    <td>${competitionList.get(i).getCreatedDateString()}</td>
                                    <c:if test="${competitionList.get(i).student_modifiable()}">
                                        <td class=" cell-op">
                                            <button onclick="deleted('${competitionList.get(i).id}')"
                                                    class="btn btn-danger">
                                                刪除
                                            </button>
                                            <button onclick="modify('${competitionList.get(i).id}')"
                                                    class="btn btn-warning">
                                                修改
                                            </button>
                                        </td>
                                    </c:if>
                                    <c:if test="${!competitionList.get(i).student_modifiable()}">
                                        <td class=" cell-op">${competitionList.get(i).selectedYear}-${competitionList.get(i).getStatusC()}</td>
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>競賽成果</div>
                <div class="panel-body">
                    <div class="form-group">
                        <form name="myForm" action="competition" enctype='multipart/form-data' onSubmit="return validate()"
                              method="POST">
                            <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                            <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                            <input type="text" name="page" value="${page}" style="display:none">
                            <label for="name" class="required">競賽名稱</label>
                            <input type="text" class="form-control" id="name" name="name"
                                   placeholder="請填入競賽名稱，例如2015全國程式設計競賽、第五屆全國調酒比賽。"
                                   required>
                            <label for="item">項目</label>
                            <input type="text" class="form-control" id="item" name="item"
                                   placeholder="請填入競賽項目，例如朗讀、生物科、高中組、青少年組、甲組、乙組、團體組、個人組、男生組、女生組等，請依循所參與競賽之實際分類進行填報，無分組者請空白。">
                            <label for="field"  class="required">競賽領域代碼</label>
                            <select class="form-control" id="field" name="field" required>
                                <option value="" disabled selected>(請選擇)</option>
                                <c:forEach var="i" items="${field.getHashMap()}">
                                    <option value="${i.key}">${i.value}</option>
                                </c:forEach>
                            </select>
                            <label for="level" class="required">競賽等級代碼</label>
                            <select class="form-control" id="level" name="level" required>
                                <option value="" disabled selected>(請選擇)</option>
                                <option value="1">校級</option>
                                <option value="2">縣市級</option>
                                <option value="3">全國</option>
                                <option value="4">國際</option>
                            </select>
                            <label for="award" class="required">獎項</label>
                            <input type="text" class="form-control" id="award" name="award"
                                   placeholder="請填入獎項名稱：例如未得獎、第一名、第二名、第三名、金牌獎、佳作、優勝等。"
                                   required>
                            <label for="time" class="required">結果公布日期</label>
                            <input type="text" class="min_go_date form-control" id="time" name="time" placeholder="請點擊選擇年月日"
                                   required>
                            <div class="col-sm-12"><br/></div>
                            <label for="content">內容簡述</label>
                            <div><textarea rows="4" class="form-control" id="content" name="content" placeholder="請填寫內容簡述，如作品名稱或參賽內容介紹"></textarea></div>
                            <label for="type"  class="required">團體參與或個人</label>
                            <select class="form-control" id="type" name="type" required>
                                <option value="" disabled selected>(請選擇)</option>
                                <option value="1">個人</option>
                                <option value="2">團體</option>
                            </select>
                            <label for="document" class="required">證明文件</label>
                            <input type="file" class="form-control" id="document" name="document" accept="${docAllowType}" required>
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
                            <input type="submit" id="submit" class="btn btn-info" value="送出">
                            <input type="button" id="cancel" onclick="cancelmodify()" class="btn btn-warning"
                                   style='display:none' value="取消">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
