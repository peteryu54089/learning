<%--@elvariable id="student" type="model.role.Student"--%>
<%@ page import="util.DateUtils" %>
<%--@elvariable id="individualCounsel" type="model.IndividualCounsel"--%>
<%--
  Created by IntelliJ IDEA.
  User: Ching Yun Yu
  Date: 2018/5/7
  Time: 上午 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
    <script>
    let target = 0;
    let useOldFile = false;
    let editId = ${individualCounsel.id};
    <%--function validFile() {--%>
    <%--var x = document.forms["myForm"]["document"].value;--%>
    <%--var parts = x.split('.');--%>
    <%--var ext = parts[parts.length - 1];--%>
    <%--if (ext.toLowerCase() != "pdf") {--%>
    <%--alert("證明文件需要為pdf檔案");--%>
    <%--return false;--%>
    <%--}--%>
    <%--}--%>

    <%--$(document).ready(function () {--%>
    <%--$('#file1').change(function () {--%>
    <%--var size = document.getElementById("file1").files.item(0).size;--%>
    <%--if (size > ${size}) {--%>
    <%--alert("超過上傳文件大小限制");--%>
    <%--var file = document.getElementById("file1");--%>
    <%--file.value = null;--%>
    <%--}--%>
    <%--})--%>
    <%--})--%>
    $(function () {
        loadCounselor();
        <c:if test="${not empty individualCounsel}">
        $("#oldFile").text("${individualCounsel.originalFilename}");
        if ($("#oldFile").text().length != 0) {
            $("#selectFileBtn").hide();
            $("#deleteOldFile").show();
        }
        </c:if>
        <c:if test="${not empty individualCounsel.counselor}">
        $("#counselorList").val("${individualCounsel.counselor}");
        </c:if>
        <c:if test="${not empty student}">
        add(${student.regNumber}, "${student.stu_Cname}");
        </c:if>
        if (editId !== -1) {
            useOldFile = true;
            changeUseOldFile();
            $("#addOrEditBtn").val("編輯");
            console.log("Test");
        } else {
            useOldFile = false;
            changeUseOldFile();
        }
    });

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

    function setCounselorSelf() {
        $("#counselorList").val('${SelfStaffCode}');
    }

    function changeUseOldFile() {
        if ($("#upload-file-info").text().length !== 0 || editId === -1) {
            useOldFile = false;
            $("#deleteOldFile").hide();
        } else {
            $("#deleteOldFile").show();
        }
        $("#useOldFile").val(useOldFile);
    }

    function add(id, name) {
        console.log(id, name);
        target = id;
        $("#targetName").text(name);
        $("#targetName").show();
        $("#target").val(id);
        $("#deleteTargetBtn").show();
        $(".btn-target").prop("disabled", true);
        $("#selectTargetBtn").hide();
    }

    function deleteTarget() {
        target = 0;
        $("#targetName").text("");
        $("#targetName").hide();
        $("#target").val("");
        $("#deleteTargetBtn").hide();
        $(".btn-target").prop("disabled", false);
        $("#selectTargetBtn").show();
    }

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
            $("#oldFile").text("${individualCounsel.originalFilename}");
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
            row$.append($('<td/>').html('<button class=\"btn btn-primary btn-target\" type=\"button\" onclick=add(\"' + myList[i]['rgno'] + "\",\"" + myList[i]['stu_Cname'] + '") >加入</button>'));
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

    function addCounsel() {
        if ($("#target").val() === '' || $("#target").val() === '0') {
            alert('尚未選擇諮詢對象');
            return false;
        } else return true;
    }
    </script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>上傳個別諮詢管理
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form name="myForm" action="UploadIndividualCounsel" enctype='multipart/form-data'
                          onsubmit="return addCounsel()" method="POST">
                        <input name="id" value="${individualCounsel.id}" style="display:none;">
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
                                           value="${DateUtils.formatAsDateTimeInput(individualCounsel.startTime)}"
                                           required>
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
                                           value="${DateUtils.formatAsDateTimeInput(individualCounsel.endTime)}"
                                           required>
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
                                           class="form-control" value="${individualCounsel.location}">
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
                                           value="${individualCounsel.title}">
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
              maxlength="150">${individualCounsel.description}</textarea>
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
                                    <button id="selectTargetBtn" class="btn btn-primary" data-toggle="modal"
                                            type="button"
                                            data-target="#exampleModal">選擇對象
                                    </button>
                                    <input id="target" name="target" style="display: none"
                                           value="${individualCounsel.rgno}">
                                    <label id="targetName" style="display: none">${student.stu_Cname}</label>
                                    <button id="deleteTargetBtn" class="btn btn-danger" type="button"
                                            style="display: none" onclick="deleteTarget()">刪除
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
                                    <br><br>
                                    <span class='label label-info' id="upload-file-info"
                                          style="font-size: small"></span>
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
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 id="exampleModalLabel">選擇諮詢對象</h5>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-6">
                                <label for="class" style="font-size: large" class="col-sm-4">班級</label>
                                <div class="col-sm-8">
                                    <select id="class" name="class" class="form-control">
                                        <option VALUE="">ALL</option>
                                        <c:if test="${CUnitList.size() > 0}">
                                            <c:forEach var="i" begin="0" end="${CUnitList.size()-1}">
                                                <option VALUE=${CUnitList.get(i).cls_code}>${CUnitList.get(i).cunit_name}</option>
                                            </c:forEach>
                                        </c:if>

                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-6">
                                <label for="regno" style="font-size: large" class="col-sm-4">學號</label>
                                <div class="col-sm-8">
                                    <input type="text" id="regno" name="regno" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-6">
                                <label for="name" style="font-size: large" class="col-sm-4">姓名</label>
                                <div class="col-sm-8">
                                    <input type="text" id="name" name="name" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row"><br/></div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="col-sm-4"></div>
                                <div class="col-sm-8"><input type="button" onclick="query()" class="btn btn-warning"
                                                             value="查詢"></div>
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
    <!-- Modal -->
</div>
</body>
</html>

