<%@ page import="util.DateUtils" %>
<%--@elvariable id="courseList" type="java.util.List<model.Course>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/4/29
  Time: 上午 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        url = "tutorViewCourseRecord?rgno=${rgno}&";
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

</script>
<body>
<div class="container">
    <div class="panel-body">
        <div class="panel panel-danger">
            <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>學生資料</div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">班級</th>
                        <th class="text-center">座號</th>
                        <th class="text-center">學號</th>
                        <th class="text-center">姓名</th>
                    </tr>
                    <c:if test="${student!=null}">
                        <tr>
                            <td>${student.className}</td>
                            <td>${student.class_no}</td>
                            <td>${student.regNumber}</td>
                            <td>${student.stu_Cname}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
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
                                                                    <tr>
                                                                        <th>送出驗證</th>
                                                                        <td>
                                                                            <c:if test="${!\"\".equals(courseRecord.submittedDateString)}">
                                                                                已於
                                                                                <span>${courseRecord.submittedDateString}</span>
                                                                                送出
                                                                            </c:if>
                                                                        </td>
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
                                                                                    <a href="${crd.dlLink}" target="_blank"
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
                                            </td>
                                        </tr>
                                    </c:forEach>
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

</body>
</html>
