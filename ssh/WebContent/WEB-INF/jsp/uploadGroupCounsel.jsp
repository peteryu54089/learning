<%--- <%@ page import="util.DateUtils" %> ---%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="groupCounsel" type="model.GroupCounsel"--%>

<%--@elvariable id="studentNameMap" type="java.util.Map"--%>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="resources/css/advisecourse.css">
    <link rel="stylesheet" type="text/css" href="resources/css/chosen.min.css">
    <script src="resources/javascript/chosen.jquery.min.js"></script>
</head>
<script>
let editId = ${groupCounsel.id};
let useOldFile = true;
let targetStudents = [];
$(function () {
    loadCounselor();
    <c:if test="${not empty groupCounsel}">
    $("#oldFile").text("${groupCounsel.originalFilename}");
    if ($("#oldFile").text().length != 0) {
        $("#selectFileBtn").hide();
        $("#deleteOldFile").show();
    }
    <c:forEach items="${groupCounsel.groupCounselMember}" var="member" varStatus="status">
    add("${member.registerNumber}", "${studentNameMap.get(member.registerNumber)}", false);
    </c:forEach>
    </c:if>

    <c:if test="${not empty groupCounsel.counselor}">
    $("#counselorList").val("${groupCounsel.counselor}");
    </c:if>
    if (editId !== -1) {
        useOldFile = true;
        changeUseOldFile();
        $("#addOrEditBtn").val("編輯");
    } else {
        useOldFile = false;
        changeUseOldFile();
    }
    $("#class").on('change', function () {
        if (!$(this).val()) {
            $("#addClass").hide();
        } else {
            $("#addClass").show();
        }
    });
    $('#studentModal').on('shown.bs.modal', function () {
        $('.addTarget').prop("disabled", false);
        $('#studentModal').trigger('focus')
    })
});

function changeUseOldFile() {
    if ($("#upload-file-info").text().length !== 0 || editId === -1) {
        useOldFile = false;
        $("#deleteOldFile").hide();
    } else {
        $("#deleteOldFile").show();
    }
    $("#useOldFile").val(useOldFile);
}

function deleteOld() {

    if ($("#oldFile").text().length !== 0) {
        $("#oldFile").text("");
        $("#deleteOldFile").html("取消刪除");
        $("#selectFileBtn").show();
    } else {
        $("#oldFile").text("${groupCounsel.originalFilename}");
        $("#deleteOldFile").html("刪除舊檔");
        $("#selectFileBtn").hide();
    }
    useOldFile = !useOldFile;
    changeUseOldFile();
}

function query() {
    $.ajax({
        url: "./UploadGroupCounsel",
        type: 'get',
        data: {
            action: 'studentList',
            class: $("#class").val(),
            regno: $("#regno").val(),
            name: $("#name").val()
        },
        success: function (data) {
            $("#excelDataTable tr").remove();
            $("h4").remove(":contains('數量過多')");

            if (data['pageAmount'] > 1) {
                $("#excelDataTable").after('<h4 id=\'muchResult\'>搜尋結果數量過多，請增加搜尋條件<\/h4>')
            }
            buildHtmlTable('#excelDataTable', data['studentList']);
        },
        error: function () {
            alert("查詢失敗");
        }
    });
}

function addClassQuery() {
    $.ajax({
        url: "./UploadGroupCounsel",
        type: 'get',
        data: {
            action: 'classStudentList',
            class: $("#class").val()
        },
        success: function (data) {
            $("#excelDataTable tr").remove();
            $("h4").remove(":contains('數量過多')");
            $.each(data['studentList'], function (index, item) {
                add(item["rgno"], item["stu_Cname"], false);
            });
        },
        error: function () {
            alert("查詢失敗");
        }
    });
}

function addTargetItem(target, id) {
    let targetDiv = $(".chosen-container");
    if (targetDiv.children().length === 0) {
        targetDiv.append($('<ul/>').addClass("chosen-choices"));
    }
    let li = $('<li/>').addClass("search-choice");
    let span = $('<span/>').html(target);
    let a = $('<a/>').addClass("search-choice-close");
    a.click(target, function () {
        deleteTargetItem(target, id);
    });
    $(".chosen-choices").append(li);
    li.append(span);
    span.after(a);
}

function deleteTargetItem(target, id) {
    console.log('target : ' + target + ', id : ' + id);
    let targetDiv = $(".chosen-container");
    targetStudents.splice(targetStudents.indexOf(id), 1);
    $('input[type=hidden]').each(function () {
        if ($(this).val() === id) {
            $(this).remove();
        }
    });
    $("li:contains(" + target + ")").remove();
    if ($(".chosen-choices").children().length === 0) {
        targetDiv.find($(".chosen-choices")).remove();
    }
}

function loadCounselor() {
    if ($('select#counselorList option').length == 1) { //Check condition here
        $.ajax({
            url: "./UploadGroupCounsel",
            type: 'get',
            data: {
                action: 'counselor'
            },
            success: function (data) {
                let array = data;
                if (array != '') {
                    for (let i in array) {
                        $("#counselorList").append("<option value=" + array[i].staffCode + ">" + array[i].name + "</option>");
                    }
                }
            },
            error: function (x, e) {
                alert("查詢諮詢導師錯誤");
            }
        });
    }
}

function add(id, name, fromButton = true) {
    if (!targetStudents.includes(id)) {
        targetStudents.push(id);
        $('<input>').attr({
            type: 'hidden',
            value: id,
            name: 'target[]'
        }).appendTo('form');
        addTargetItem(name, id)
    }
    if (fromButton) {
        event.srcElement.disabled = true;
    }
}

function addClass() {
    //add($("#class").val(), $("#class option:selected").text(), false);
    $("#studentModal").modal("hide");
    $("#excelDataTable tr").remove();
    addClassQuery();
}

function addYear(btn, yearCode, yearName) {
    $(btn).hide();
    $("#searchBtn").hide();
    add(yearCode, yearName, false);
}

// Builds the HTML Table out of myList.
function buildHtmlTable(selector, myList) {
    let columns = addAllColumnHeaders(myList, selector);
    let tbody = $("<tbody/>");

    for (let i = 0; i < myList.length; i++) {
        let row$ = $('<tr/>');
        for (let colIndex = 0; colIndex < columns.length; colIndex++) {
            let cellValue = myList[i][columns[colIndex]];
            if (cellValue == null) cellValue = "";
            row$.append($('<td/>').html(cellValue));
        }
        row$.append($('<td/>').html('<button class=\"addTarget btn btn-primary\" type=\"button\" onclick=add(\"' + myList[i]['rgno'] + "\",\"" + myList[i]['stu_Cname'] + '") >加入</button>'));
        row$.append($('<td/>').attr({'style': 'display:none;'}).html(myList[i]['rgno']));
        tbody.append(row$);
    }
    $(selector).append(tbody);
}

// Adds a header row to the table and returns the set of columns.
// Need to do union of keys from all records as some records may not contain
// all records.
function addAllColumnHeaders(myList, selector) {
    let columnSet = [];
    let thead = $('<thead/>');
    let headerTr$ = $('<tr/>');

    let rowHash = myList[0];
    for (let key in rowHash) {
        if ($.inArray(key, columnSet) == -1 && key !== 'rgno') {
            columnSet.push(key);
        }
        switch (key) {
            case 'className':
                headerTr$.append($('<th/>').html('班級'));
                break;
            case 'stu_Cname':
                headerTr$.append($('<th/>').html('姓名'));
                break;
            case 'regNumber':
                headerTr$.append($('<th/>').html('識別碼'));
                break;
            case 'name':
                headerTr$.append($('<th/>').html('姓名'));
                break;
            default:
        }
    }
    headerTr$.append($('<th/>').html('操作'));
    thead.append(headerTr$);
    $(selector).append(thead);

    return columnSet;
}

function setCounselorSelf() {
    $("#counselorList").val('${SelfStaffCode}');
}

function addCounsel() {
    if (targetStudents.length === 0) {
        alert("尚未選擇諮詢對象");
        return false;
    } else return true;
}
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>新增/編輯
                課程諮詢紀錄
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="UploadGroupCounsel" onsubmit="return addCounsel()"
                          enctype='multipart/form-data' method="POST">
                        <input name="id" value="${groupCounsel.id}" style="display:none;">
                        <input id="useOldFile" name="useOldFile" style="display: none">
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label for="startTime" style="font-size: large" class="col-sm-3">開始時間</label>
                                <div class="col-sm-6 date">
                                    <input type="datetime-local" id="startTime"
                                           name="startTime"
                                           class="form-control"
                                           value="${DateUtils.formatAsDateTimeInput(groupCounsel.startTime)}" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label for="endTime" style="font-size: large" class="col-sm-3">結束時間</label>
                                <div class="col-sm-6 date">
                                    <input type="datetime-local" id="endTime"
                                           name="endTime"
                                           class="form-control"
                                           value="${DateUtils.formatAsDateTimeInput(groupCounsel.endTime)}" required>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label for="location" style="font-size: large" class="col-sm-3">地點</label>
                                <div class="col-sm-6 date">
                                    <input id="location"
                                           name="location"
                                           class="form-control" value="${groupCounsel.location}">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label for="title" style="font-size: large" class="col-sm-3">諮詢標題</label>
                                <div class="col-sm-6">
                                    <input id="title" name="title" class="form-control" required
                                           value="${groupCounsel.title}">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label for="description" style="font-size: large" class="col-sm-3">諮詢內容簡述</label>
                                <div class="col-sm-6">
    <textarea id="description" name="description" class="form-control"
              style="min-width: 100%"
              maxlength="150">${groupCounsel.description}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label style="font-size: large" class="col-sm-3">諮詢老師</label>
                                <div class="col-sm-6">
                                    <select id="counselorList" name="counselor"
                                            class="form-control"
                                            onclick="loadCounselor();" required>
                                        <option selected disabled hidden value="">Select</option>
                                    </select>
                                    <br>
                                    <button class="btn btn-primary" type="button"
                                            onclick="setCounselorSelf()">本人
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label style="font-size: large" class="col-sm-3">諮詢對象</label>
                                <div class="col-sm-6">
                                    <div class="chosen-container chosen-container-multi chosen-with-drop chosen-container-active"></div>
                                    <button class="btn btn-primary" data-toggle="modal" type="button"
                                            data-target="#studentModal">選擇對象
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <label style="font-size: large" class="col-sm-3">上傳檔案</label>
                                <div class="col-sm-6">
                                    <label id="selectFileBtn" class="btn btn-primary" for="file">
                                        <input id="file" name="file" type="file" style="display:none"
                                               onchange="let f = this; $('#upload-file-info').html(function(){return f.files[0]?f.files[0].name:''}); changeUseOldFile();">選擇檔案
                                    </label>
                                    <span class='label label-info' id="upload-file-info"></span>
                                    <label id="oldFile"></label>
                                    <button class="btn btn-danger" id="deleteOldFile" type="button"
                                            style="display: none" onclick="deleteOld()">刪除舊檔
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-8">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6"><input id="addOrEditBtn" type="submit" class="btn btn-warning"
                                                             value="新增"></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- Modal -->
    <div class="modal fade" id="studentModal" tabindex="-1" role="dialog" aria-labelledby="studentModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 id="studentModalLabel">選擇諮詢對象</h5>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-8">
                                <label for="class" style="font-size: large" class="col-sm-4">班級</label>
                                <div class="col-sm-8">
                                    <select id="class" name="class" class="form-control">
                                        <option VALUE="">ALL</option>
                                        <c:if test="${CUnitList.size() > 0}">
                                            <c:forEach items="${CUnitList}" var="x">
                                                <option VALUE=${x.cls_code} data-grade>${x.cunit_name}</option>
                                            </c:forEach>
                                        </c:if>

                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-8">
                                <label for="regno" style="font-size: large" class="col-sm-4">學號</label>
                                <div class="col-sm-8">
                                    <input type="text" id="regno" name="regno" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-8">
                                <label for="name" style="font-size: large" class="col-sm-4">姓名</label>
                                <div class="col-sm-8">
                                    <input type="text" id="name" name="name" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-8">
                                <div class="col-sm-4"></div>
                                <div class="col-sm-4"><input id="searchBtn" type="button" onclick="query()"
                                                             class="btn btn-warning"
                                                             value="查詢"></div>
                                <div class="col-sm-4"><input type="button" onclick="addClass()" id="addClass"
                                                             class="btn btn-primary"
                                                             style="display:none" value="加入班級"></div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-8">
                                <div class="col-sm-4"><input type="button" onclick="addYear(this,100,'一年級全員')"
                                                             class="btn btn-primary"
                                                             value="新增一年級"></div>
                                <div class="col-sm-4"><input type="button" onclick="addYear(this,200,'二年級全員')"
                                                             class="btn btn-primary"
                                                             value="新增二年級"></div>
                                <div class="col-sm-4"><input type="button" onclick="addYear(this,300,'三年級全員')"
                                                             class="btn btn-primary"
                                                             value="新增三年級"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                    <table class="table table-striped" id="excelDataTable">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
