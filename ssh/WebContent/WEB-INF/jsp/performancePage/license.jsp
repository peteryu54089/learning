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


    <link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css">

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
            url: "./licensedelete",
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
                    window.location.replace("license?page=" + data);
                }
            },
            error: function () {
                alert("刪除失敗");
            }
        });
    }

    function download(a, b) {
        window.location.href = './downloadLicense?no=' + a + '&id=' + b;
    }

    function modify(n) {
        c = confirm("確定修改此筆資料?輸入中的資料將會被清空");
        if (c) {
            $.ajax({
                url: "./licenseInfo",
                type: 'post',
                data:
                    {
                        id: n
                    },
                success: function (data) {
                    $("#modifyid").val(data.id);
                    $("#code").val(data.code);
                    $("#note").val(data.note);
                    $("#point").val(data.point);
                    $("#result").val(data.result);
                    $("#time").val(data.time);
                    $("#content").val(data.content);
                    $("#licensenumber").val(data.licensenumber);
                    $("#group").val(data.group);
                    $("#externalLink").val(data.external_link);
                    $("#document").hide();
                    $("#document").prop('required', false);
                    $("#document").val(null);
                    $("#document").prop('disabled', true);
                    $("#submit").val("修改");
                    $("#cancel").show();
                    $("#file1name").html("<a href=\""+data.document_link+"\">" + data.document_original_filename + "</a>");
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
        $("#code").val("");
        $("#note").val("");
        $("#point").val("");
        $("#result").val("");
        $("#time").val("");
        $("#content").val("");
        $("#licensenumber").val("");
        $("#group").val("");
        $("#externalLink").val("");
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
        var code = document.forms["myForm"]["code"];
        var note = document.forms["myForm"]["note"];
        var point = document.forms["myForm"]["point"];
        var result = document.forms["myForm"]["result"];
        var time = document.forms["myForm"]["time"];
        var licensenumber = document.forms["myForm"]["licensenumber"];
        var group = document.forms["myForm"]["group"];
        var content = document.forms["myForm"]["content"];
        var documentation = document.forms["myForm"]["document"];
        var video = document.forms["myForm"]["video"];
        var external_link = document.forms["myForm"]["externalLink"];

        var test_length_array = [code, note, point, result, time, licensenumber, group, content, documentation, video, external_link];
        validation &= if_length_too_long(test_length_array, license_field_length_array);
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>檢定證照</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">證照代碼</th>
                            <th class="text-center">證照類別</th>
                            <th class="text-center">分數</th>
                            <th class="text-center">分項結果</th>
                            <th class="text-center">取得証證日期時間</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center">證明文件</th>
                            <th class="text-center">影音檔案</th>
                            <th class="text-center">外部連結</th>
                            <th class="text-center">上傳時間</th>
                            <th class="text-center cell-op">操作</th>
                        </tr>

                        <c:if test="${licenseList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${licenseList.size()-1}">
                                <tr>
                                    <td>${licenseList.get(i).code}</td>
                                    <td>${licenseList.get(i).note}</td>
                                    <td>${licenseList.get(i).point}</td>
                                    <td>${licenseList.get(i).result}</td>
                                    <td>${licenseList.get(i).time}</td>
                                    <td>${licenseList.get(i).content}</td>
                                    <td>
                                        <c:if test="${licenseList.get(i).documentFile!=null}">
                                            <a href="${licenseList.get(i).documentFile._link}">
                                                    ${licenseList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${licenseList.get(i).videoFile!=null}">
                                            <a href="${licenseList.get(i).videoFile._link}">
                                                    ${licenseList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${licenseList.get(i).externalLink}" target="_blank">
                                            ${licenseList.get(i).externalLink}</a>
                                    </td>
                                    <td>${licenseList.get(i).getCreatedDateString()}</td>
                                    <c:if test="${licenseList.get(i).student_modifiable()}">
                                        <td class=" cell-op">
                                            <button onclick="deleted('${licenseList.get(i).id}')"
                                                    class="btn btn-danger">
                                                刪除
                                            </button>
                                            <button onclick="modify('${licenseList.get(i).id}')"
                                                    class="btn btn-warning">
                                                修改
                                            </button>
                                        </td>
                                    </c:if>
                                    <c:if test="${!licenseList.get(i).student_modifiable()}">
                                        <td class=" cell-op">${licenseList.get(i).selectedYear}-${licenseList.get(i).getStatusC()}</td>
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
                <div class="panel-heading"><span class="glyphicon glyphicon-user">　</span>檢定證照</div>
                <div class="panel-body">
                    <div class="form-group">
                        <form name="myForm" action="license" enctype='multipart/form-data'
                              method="POST"
                              onSubmit="return validate()">

                            <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                            <input type="text" id="delsub" name="delsub" value="0" style="display:none">
                            <input type="text" name="page" value="${page}" style="display:none">
                            <label for="code" class="required">證照代碼</label>
                            <div>
                                <select class="form-control" required name="code" id="code" required>
                                    <option value="" disabled selected>請選擇</option>
                                    <c:if test="${licenses.size() > 0}">
                                        <c:forEach var="i" begin="0" end="${licenses.size()-1}">
                                            <option value="${licenses.get(i).id}">${licenses.get(i).name}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                                <p class="text-muted">
                                    如該證照無列在代碼列表，請通知學 習歷程系統管理人員，經查核後即增加填報之選項。
                                </p>
                            </div>
                            <label for="note" class="required">證照類別</label>
                            <select class="form-control" id="note" name="note">
                                <option value="" disabled selected>(請選擇)</option>
                                <option value="1">英語能力檢定</option>
                                <option value="2">其他語言能力檢定</option>
                                <option value="3">技能檢定及技術士證照</option>
                                <option value="4">不在以上類別</option>
                            </select>
                            <label for="point">分數</label>
                            <input type="text" class="form-control" id="point" name="point"
                                   placeholder="測驗結果有分數者填入總分，無分數者請空白。">
                            <label for="result">分項結果</label> <span class="glyphicon glyphicon-info-sign"
                                                                   aria-hidden="true"
                                                                   data-toggle="tooltip"
                                                                   title="如成績有多項者請依照分項進行填報，分項間以半形,分隔。如檢測分為觀念題與實作題，若取得之分數分別為80與340，請填寫觀念題80,實作題340；或某項檢定證照分學科術科進行，或學科65，術科結果判定及格，請填寫學科65,術科及格。"></span>
                            <input type="text" class="form-control" id="result" name="result"
                                   placeholder="請輸入證照分數(如果有)。如: 觀念題80,實作題100(請將滑鼠移至上方圖示顯示更多說明)">
                            <label for="time" class="required">取得證照日期</label>
                            <input type="text" class="min_go_date form-control" id="time" name="time"
                                   placeholder="請點擊選擇年月日"
                                   required>
                            <div style="display:none">
                                <label for="licensenumber">證照字號</label>
                                <input type="text" class="form-control" id="licensenumber" name="licensenumber"
                                       placeholder="請填寫取得證照字號，如暨字證000001號，若無請保留空白。">
                                <label for="group">檢定組別</label>
                                <input type="text" class="form-control" id="group" name="group"
                                       placeholder="請填寫檢定組別或級別，無分組別者請保留空白。">
                            </div>
                            <label for="content" class="required">內容簡述</label>
                            <input type="text" class="form-control" id="content" name="content"
                                   placeholder="請100字內之檢定證照概述。" required>
                            <label for="document" class="required">證明文件</label>
                            <input type="file" class="form-control" id="document" name="document"
                                   accept="${docAllowType}"
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
</div>
