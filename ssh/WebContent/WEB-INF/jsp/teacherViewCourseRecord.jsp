<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/25
  Time: 下午 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <jsp:include page="includes/header.jsp"></jsp:include>
</head>
<script>
function query(n) {
    url = "teacherViewCourseRecord?courseno=${courseno}&";
    if ($("#stuName").val() != "")
        url += "stuname=" + $("#stuName").val() + "&";
    if ($("#status").val() != "")
        url += "status=" + $("#status").val() + "&";
    window.location = url.substr(0, url.length - 1);
}

$(document).ready(function () {
    $("#stuName").val("${stu_name}");
    $("#status").val(${status});
});
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


</script>
<body>
<div class="container">
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>班級資料</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">班別</th>
                        <th class="text-center">課名</th>
                        <th class="text-center">授課教師</th>
                    </tr>
                    <c:if test="${course!=null}">
                        <tr>
                            <td>${course.term_year}</td>
                            <td>${course.term_sem}</td>
                            <td>${course.course_no}</td>
                            <td>${course.class_Cname}</td>
                            <td>${course.course_Cname}</td>
                            <td>${course.names}</td>
                        </tr>
                    </c:if>
                </table>
            </div>

        </div>
    </div>

    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>查詢課程學習成果
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="stuName" style="font-size: large" class="col-sm-2">學生</label>
                            <div class="col-sm-10">
                                <input type="text" id="stuName" name="stuName" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row"><br/></div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <label for="status" style="font-size: large" class="col-sm-2">狀態</label>
                            <div class="col-sm-10">
                                <select id="status" name="status" class="form-control">
                                    <option VALUE="">ALL</option>
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
                            <div class="col-sm-10"><input type="button" onclick="query()" value="查詢"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>課程成果</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">驗證教師</th>
                        <th class="text-center">學生</th>
                        <th class="text-center">送出日期</th>
                        <th class="text-center">驗證日期</th>
                        <th class="text-center">狀態</th>
                        <th class="text-center">描述</th>
                        <th class="text-center">驗證訊息</th>
                        <th class="text-center">操作</th>
                    </tr>
                    <c:if test="${courseRecordList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${courseRecordList.size()-1}">
                            <tr>
                                <td> ${courseRecordList.get(i).verify_name}</td>
                                <td> ${courseRecordList.get(i).stu_name}</td>
                                <td>${courseRecordList.get(i).getSubmittedDateString()}</td>
                                <td>${courseRecordList.get(i).getVerifyDateString()}</td>
                                <td>${courseRecordList.get(i).getStatusC()}</td>
                                <td>${courseRecordList.get(i).content}</td>
                                <td>${courseRecordList.get(i).verify_message }</td>
                                <td>
                                    <c:if test="${courseRecordList.get(i).isVerifyable()}">
                                    <button class="btn btn-warning" data-toggle="modal"
                                            data-target="#exampleModal"
                                            data-record_id="${courseRecordList.get(i).id}">驗證
                                    </button>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </div>
            <div id="pagination"></div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="學習成果上傳" id="exampleModalLabel">驗證課程成果</h5>
            </div>
            <div class="modal-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">驗證教師</th>
                        <th class="text-center">學生</th>
                        <th class="text-center">檔案</th>
                        <th class="text-center">送出日期</th>
                        <th class="text-center">驗證日期</th>
                        <th class="text-center">狀態</th>
                        <th class="text-center">描述</th>
                        <th class="text-center">驗證訊息</th>
                    </tr>
                    <tr>
                        <td id="cstaff">1</td>
                        <td id="cstudent">2</td>
                        <td id="cfile">3</td>
                        <td id="csubmitted_at">4</td>
                        <td id="cverified_at">5</td>
                        <td id="cstatus">6</td>
                        <td id="ccontent">7</td>
                        <td id="cverify_message">8</td>
                    </tr>
                </table>
                <div class="form-group">
                    <form name="myForm" action="courseRecordVerified" enctype='multipart/form-data' method="POST">
                        <input type="text" id="verifyid" name="verifyid" value="0" style="display:none">
                        <input type="text" id="courseno" name="courseno" value="${courseno}" style="display:none">
                        <input type="text" id="page" name="page" value=${page} style="display:none">
                        <div>
                            <label for="content" class="col-sm-4" style="font-size: large">驗證結果</label>
                            <div class="col-sm-12">
                                <select id="verified" name="status" class="form-control">
                                    <option VALUE="2">未驗證</option>
                                    <option VALUE="3">驗證成功</option>
                                    <option VALUE="4">驗證失敗</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-12"><br/></div>
                        <label for="content" class="col-sm-4" style="font-size: large">驗證訊息(若為驗證失敗則為必填)</label>
                        <div class="col-sm-8"><textarea rows="4" class="form-control" id="content" name="content"
                                                        required></textarea></div>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                        <input id="submit" type="submit" class="btn btn-primary" value="驗證"></button>
                    </form>
                </div>
            </div>
        </div>
        <script>
        $('#exampleModal').on('show.bs.modal', function (event) {
            var modal = $(this);
            modal.find('#content').val("");
            modal.find('#verified').val("2");
            record_id = $(event.relatedTarget).data('record_id');
            $.ajax({
                url: "./courseRecordInfo",
                type: 'post',
                data: {
                    id: record_id
                },
                success: function (data) {
                    modal.find('#cstaff').text(data.staff);
                    modal.find('#cstudent').text(data.student);
                    dn = data.documentname.substr(1, data.documentname.length - 2).split(",")
                    di = data.documentid.substr(1, data.documentid.length - 2).split(",")
                    dl = data.dlink.substr(1, data.dlink.length - 2).split(",")
                    modal.find('#cfile').html("");
                    for (i=0;i<dn.length;i++)
                    {
                        modal.find('#cfile').append("<a href=\""+dl[i]+"\")\">" + dn[i] + "</a>");
                        modal.find('#cfile').append("<br>");
                    }
                    //$("#cfile").html("<a href=\"javascript:download(" + data.id + ")\">" + data.original_filename + "</a>");
                    modal.find('#csubmitted_at').text(data.submittedDate);
                    modal.find('#cverified_at').text(data.verifyDate);
                    modal.find('#cstatus').text(data.statusC);
                    modal.find('#ccontent').text(data.content);
                    modal.find('#cverify_message').text(data.verify_message);
                    modal.find('#content').val(data.verify_message);
                    modal.find('#verified').val(data.status);
                    $("#verifyid").val(data.id);
                    if ($('#exampleModal').find('#verified').val() == "2") {
                        $('#exampleModal').find('#submit').prop('disabled', true);
                    } else {
                        $('#exampleModal').find('#submit').prop('disabled', false);
                    }
                },
                error: function () {
                    alert("error");
                }
            })
        });

        $(document).ready(function () {
            $('#exampleModal').find('#verified').change(function () {
                $('#exampleModal').find('#submit').prop('disabled', true);
                if ($(this).val() == "3") {
                    $('#exampleModal').find('#submit').prop('disabled', false);
                    $('#exampleModal').find('#content').prop('required', false);
                }
                if ($(this).val() == "4") {
                    $('#exampleModal').find('#submit').prop('disabled', false);
                    $('#exampleModal').find('#content').prop('required', true);
                }
            })
        })
        </script>
</body>
</html>
