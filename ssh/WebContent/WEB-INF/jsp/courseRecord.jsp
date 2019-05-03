<%--@elvariable id="courseList" type="java.util.List<model.Course>"--%>
<%--- <%@ page import="util.DateUtils" %> ---%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <jsp:include page="includes/header.jsp"/>
</head>
<script>

    url = "?page=";
    if (location.href.split("?page")[0].split("?").length >= 2) {
        url = "?" + location.href.split('?')[1].split('&page')[0] + "&page=";
    }
    $(function () {
        $('#pagination').pagination({
            pages:${pageAmount},
            hrefTextPrefix: url,
            currentPage:${page},
            edges: 2,
            cssStyle: 'light-theme',
            ellipsePageSet: false
        });
    });

    function query(n) {
        url = "courseRecord?rgno=${rgno}&";
        if ($("#year").val() != "")
            url += "year=" + $("#year").val() + "&";
        if ($("#semester").val() != "")
            url += "semester=" + $("#semester").val() + "&";
        if ($("#courseName").val() != "")
            url += "courseName=" + $("#courseName").val() + "&";
        if ($("#verifystate").val() != "")
            url += "verifystate=" + $("#verifystate").val() + "&";
        window.location = url.substr(0, url.length - 1);
    }

    $(document).ready(function () {
        $("#year").val(${year});
        $("#semester").val(${semester});
        $("#courseName").val("${courseName}");
        $("#verifystate").val("${verifystate}")
    });

    function download(a) {
        window.location.href = './downloadCourseRecord?id=' + a;
    }

    function submited(n) {
        $.ajax({
            url: "./courseRecordsubmit",
            type: 'post',
            data: {
                id: n,
            },
            success: function (data) {
                window.location.replace(location.href);
            },
            error: function () {
                alert("刪除失敗");
            }
        });
    }

    function deleted(n) {
        $.ajax({
            url: "./courseRecorddelete",
            type: 'post',
            data: {
                id: n,
            },
            success: function (data) {
                window.location.replace(location.href);
            },
            error: function () {
                alert("刪除失敗");
            }
        });
    }

    $(document).ready(function () {
        <c:forEach var="i" begin="1" end="${fileAmount}">
        $('#file${i}').change(function () {
            var size = document.getElementById("file${i}").files.item(0).size;
            if (size > ${size}) {
                alert("超過上傳文件大小限制");
                var file = document.getElementById("file${i}");
                file.value = null;
            }
        })
        </c:forEach>
    })
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢課程學習成果
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="year" style="font-size: large" class="col-sm-2">學年</label>
                            <div class="col-sm-10">
                                <select id="year" name="year" class="form-control">
                                    <!--TODO 抓年度-->
                                    <option VALUE="">ALL</option>
                                    <option VALUE="103">103</option>
                                    <option VALUE="104">104</option>
                                    <option VALUE="105">105</option>
                                    <option VALUE="106">106</option>
                                    <option VALUE="107">107</option>
                                    <option VALUE="108">108</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="semester" style="font-size: large" class="col-sm-2">學期</label>
                            <div class="col-sm-10">
                                <select id="semester" name="semester" class="form-control">
                                    <!--TODO 抓當前學期-->
                                    <option VALUE="">ALL</option>
                                    <option VALUE="1">1</option>
                                    <option VALUE="2">2</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="courseName" style="font-size: large" class="col-sm-2">科目</label>
                            <div class="col-sm-10">
                                <input type="text" id="courseName" name="courseName" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="verifystate" style="font-size: large" class="col-sm-2">狀態</label>
                            <div class="col-sm-10">
                                <select id="verifystate" name="verifystate" class="form-control">
                                    <!--TODO 抓當前學期-->
                                    <option VALUE="">ALL</option>
                                    <option VALUE="1">未送出</option>
                                    <option VALUE="2">未驗證</option>
                                    <option VALUE="3">驗證成功</option>
                                    <option VALUE="4">驗證失敗</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10">
                                <input type="button" onclick="query()" value="查詢" class="btn btn-primary">
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <div class="" id="main-table">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>課程學習成果
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <thead>
                    <tr>
                        <th class="text-center">學期</th>
                        <th class="text-center">科目名稱</th>
                        <th class="text-center">授課教師</th>
                        <th class="text-center" style="width: 70%">上傳課程學習成果</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="courseList.size()==0">
                        <tr>
                            <td colspan="4" align="center">
                                <p>找不到任何課程</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:forEach items="${courseList}" var="course">
                        <tr>
                            <td>${course.term_year} - ${course.term_sem}</td>
                            <td>
                                <span title="#${course.course_no}"
                                      data-toggle="tooltip">
                                        ${course.course_Cname}
                                </span>
                            </td>
                            <td>${course.names}</td>
                            <td>
                                <table class="table table-bordered course-record-table" style="text-align: left">
                                    <thead>
                                    <tr>
                                        <th class="cell-created-at">建立時間</th>
                                        <th class="cell-file-cnt">檔案數量</th>
                                        <th class="cell-verify-status">狀態</th>
                                        <th class="cell-op">操作</th>
                                    </tr>
                                    </thead>
                                    <c:forEach items="${course.courseRecordList}" var="courseRecord">
                                        <tr>
                                            <td class="cell-created-at">
                                                    ${DateUtils.formatDateTime(courseRecord.created_at)}
                                            </td>
                                            <td class="cell-file-cnt">
                                                    ${courseRecord.courseRecordDocumentList.size()}
                                            </td>
                                            <td class="cell-verify-status">
                                                <c:if test="${!\"\".equals(courseRecord.verifyDateString)}">
                                                    ${courseRecord.verifyDateString}
                                                </c:if>&nbsp;
                                                <span><c:out value="${courseRecord.statusC}"/></span>
                                            </td>

                                            <td class="cell-op">
                                                <button class="btn btn-default view-btn" data-toggle="modal"
                                                        data-target="#viewCourseRecDoc_${courseRecord.id}"
                                                        data-record_id="${courseRecord.id}">檢視
                                                </button>

                                                <!-- Modal -->
                                                <div id="viewCourseRecDoc_${courseRecord.id}" class="modal fade"
                                                     role="dialog">
                                                    <div class="modal-dialog modal-lg">
                                                        <!-- Modal content-->
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <button type="button" class="close"
                                                                        data-dismiss="modal">&times;
                                                                </button>
                                                                <h4 class="modal-title">檢視成果資訊</h4>
                                                            </div>
                                                            <div class="modal-body">
                                                                <table class="table" style="table-layout: fixed">
                                                                    <tbody>
                                                                    <tr>
                                                                        <th>學期</th>
                                                                        <td>${course.term_year}
                                                                            - ${course.term_sem}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>科目名稱</th>
                                                                        <td><c:out value="${course.course_Cname}"/></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>授課教師</th>
                                                                        <td><c:out value="${course.names}"/></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>狀態</th>
                                                                        <td><c:out
                                                                                value="${courseRecord.statusC}"/></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>驗證教師</th>
                                                                        <td><c:out value="${courseRecord.verify_name}"/></td>
                                                                    </tr>
                                                                    <c:if test="${!\"\".equals(courseRecord.verify_message)}">
                                                                        <tr>
                                                                            <th>驗證訊息</th>
                                                                            <td>
                                                                                <c:out value="${courseRecord.verify_message}"/>
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>

                                                                    <tr>
                                                                        <th>檔案</th>
                                                                        <td>
                                                                            <c:forEach
                                                                                    items="${courseRecord.courseRecordDocumentList}"
                                                                                    var="crd">
                                                                                <p>
                                                                                    <a target="_blank" href="${crd.dlLink}"
                                                                                    ><c:out value="${crd.original_filename}"/></a>
                                                                                </p>
                                                                            </c:forEach>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>內容簡述</th>
                                                                        <td>
                                                                            <div><c:out
                                                                                    value="${courseRecord.content}"/></div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>送出驗證</th>
                                                                        <td>
                                                                            <c:if test="${courseRecord.isSubmittable()}">
                                                                                <button onclick="submited('${courseRecord.id}')"
                                                                                        class="btn btn-warning">送出
                                                                                </button>
                                                                            </c:if>
                                                                            <c:if test="${!\"\".equals(courseRecord.submittedDateString)}">
                                                                                已於
                                                                                <span>${courseRecord.submittedDateString}</span>
                                                                                送出
                                                                            </c:if>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>

                                                                </table>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-default close-btn"
                                                                        data-dismiss="modal">關閉
                                                                </button>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>

                                                <c:if test="${courseRecord.student_modifiable()}">
                                                    <button class="btn btn-warning mod-btn" data-toggle="modal"
                                                            data-target="#exampleModal"
                                                            data-record_id="${courseRecord.id}"
                                                            data-course_no="${course.course_no}">修改
                                                    </button>
                                                    <button onclick="deleted('${courseRecord.id}')"
                                                            class="btn btn-danger">刪除
                                                    </button>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tfoot>
                                    <tr>
                                        <c:if test="${course.isUploadable()}">
                                        <td colspan="4" align="right">
                                            <button class="btn btn-primary add-btn" data-toggle="modal"
                                                    data-target="#exampleModal"
                                                    data-course_no="${course.course_no}">新增
                                            </button>
                                        </td>
                                        </c:if>
                                    </tr>
                                    </tfoot>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
                <div id="pagination"></div>
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
                <h5 class="學習成果上傳" id="exampleModalLabel">上傳課程成果</h5>

            </div>
            <div class="modal-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">課名</th>
                        <th class="text-center">授課教師</th>
                    </tr>
                    <tr>
                        <td id="cyear">1</td>
                        <td id="csem">2</td>
                        <td id="cno">3</td>
                        <td id="cname">4</td>
                        <td id="names">5</td>
                    </tr>
                </table>
                <div class="form-group">
                    <form name="myForm" action="courseRecordUpload" enctype='multipart/form-data' method="POST">
                        <input type="text" id="modifyid" name="modifyid" value="0" style="display:none">
                        <input type="text" id="course_no" name="course_no" value="0" style="display:none">
                        <input type="text" id="page" name="page" value=${page} style="display:none">
                        <div class="col-sm-12"><br/></div>
                        <label for="content" class="col-sm-4 required" style="font-size: large">內容簡述</label>
                        <div class="col-sm-8"><textarea rows="4" class="form-control" id="content" name="content"
                                                        required></textarea></div>
                        <div class="col-sm-12"><br/></div>
                        <label class="col-sm-4" style="font-size: large">成果檔案上傳</label>
                        <div class="col-sm-8"><br/></div>
                        <c:forEach var="i" begin="1" end="${fileAmount}">
                            <div><input type="file" class="form-control" id="file${i}" name="file${i}"
                                        accept="${allowType}"></div>
                            <div style="display:inline" id="file${i}name"></div>
                            <input type="button" id="keepfile${i}" onclick="keepfile(${i})" class="btn btn-warning"
                                   style='display:none' value="刪除舊檔案"></button>
                            <input type="text" id="delfile${i}" name="delfile${i}" value="0" style="display:none">
                        </c:forEach>
                        <div><br/></div>
                        <div class="model-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                            <input id="submit" type="submit" class="btn btn-primary" value="上傳"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function keepfile(n) {
        target = $("#keepfile" + n);
        if (target.val() == "刪除舊檔案") {
            $("#keepfile" + n).val("取消");
            $("#file" + n + "name").hide();
            $("#file" + n).show();
            $("#file" + n).prop('disabled', false);
            $("#delfile" + n).val($("#delfile" + n).val() * -1);
        } else {
            $("#keepfile" + n).val("刪除舊檔案");
            $("#file" + n + "name").show();
            $("#file" + n).hide();
            $("#file" + n).val(null);
            $("#file" + n).prop('disabled', false);
            $("#delfile" + n).val($("#delfile" + n).val() * -1);
        }
    }

    $('#exampleModal').on('show.bs.modal', function (event) {
        var modal = $(this);
        course_no = $(event.relatedTarget).data('course_no');
        $.ajax({
            url: "./courseInfo",
            type: 'post',
            data: {
                course_no: course_no
            },
            success: function (data) {
                modal.find('#cyear').text(data.term_year);
                modal.find('#csem').text(data.term_sem);
                modal.find('#cno').text(data.course_num);
                $("#course_no").val(data.course_num);
                modal.find('#cname').text(data.course_cname);
                modal.find('#names').text(data.names);
            },
            error: function () {
                alert("error");
            }
        });
        record_id = $(event.relatedTarget).data('record_id');
        if (record_id) {
            $.ajax({
                url: "./courseRecordInfo",
                type: 'post',
                data: {
                    id: record_id
                },
                success: function (data) {
                    modal.find('#content').val(data.content);
                    <c:forEach var="i" begin="1" end="${fileAmount}">
                    dn = data.documentname.substr(1, data.documentname.length - 2).split(",")
                    di = data.documentid.substr(1, data.documentid.length - 2).split(",")
                    dl = data.dlink.substr(1, data.dlink.length - 2).split(",")
                    $("#file${i}").val(null);
                    if (dn[${i}-1] != "" && dn[${i}-1] != null) {
                        $("#file${i}").hide();
                        $("#file${i}").prop('disabled', true);
                        $("#file${i}name").html("<a href=\""+dl[${i}-1]+"\")\">" + dn[${i}-1] + "</a>");
                        $('#file${i}name').show();
                        $("#keepfile${i}").show();
                        $("#delfile${i}").val(di[${i}-1] * -1);
                    } else {
                        $("#file${i}").show();
                        $("#file${i}").prop('disabled', false);
                        $('#file${i}name').hide();
                        $("#keepfile${i}").hide();
                    }
                    </c:forEach>
                    $("#submit").val("修改");
                    $("#modifyid").val(data.id);
                },
                error: function () {
                    alert("error");
                }
            });
        } else {
            $("#submit").val("新增");
            $("#modifyid").val(0);
            modal.find('#content').val("");
            <c:forEach var="i" begin="1" end="${fileAmount}">
            $("#delfile${i}").val(0);
            $("#file${i}").show();
            $("#file${i}").prop('disabled', false);
            $('#file${i}name').hide();
            $("#keepfile${i}").hide();
            </c:forEach>
        }
    })
</script>
</body>
</html>
