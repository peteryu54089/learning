<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--- <%@ page import="util.DateUtils" %> ---%>
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
    <jsp:include page="includes/header.jsp"></jsp:include>
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
        url = "courseRecordCheck?";
        if ($("#year").val() != "")
            url += "year=" + $("#year").val() + "&";
        if ($("#semester").val() != "")
            url += "semester=" + $("#semester").val() + "&";
        if ($("#courseName").val() != "")
            url += "courseName=" + $("#courseName").val() + "&";
        window.location = url.substr(0, url.length - 1);
    }

    $(document).ready(function () {
        $("#year").val(${year});
        $("#semester").val(${semester});
        $("#courseName").val("${courseName}");
    });


    function unlock(id) {
        if (confirm("確定要取消勾選?")) {
            $.ajax({
                url: "./courseRecordUnlock",
                type: 'post',
                data: {
                    id: id
                },
                success: function (data) {
                    window.location.replace(location.href);
                    //renew();
                }
            });
        }
    }
    $(document).ready(function () {
        renew();
    });

    jQuery(function () {
        $(":checkbox").click(function () {
            state = $(this).is(":checked");
            id = $(this).attr('name');
            if (state)
                state = "1";
            else
                state = "0";
            $.ajax({
                url: ".//courseRecordCheck",
                type: 'post',
                data: {
                    id: id,
                    check: state
                },
                success: function (data) {
                    renew();
                }
            });

        });
    });

    function renew() {
        $.ajax({
            url: ".//courseRecordCheckInfo",
            type: 'post',
            data: {
                year: ${year},
                semester: ${semester}
            },
            success: function (data) {
                $("#table1").find("tr:gt(0)").remove();
                count = 0;
                $.each(data, function (index, item) {
                    count++;
                    s = "<tr>" +
                        "<td>" + "<input type=\"checkbox\" id=\"checkbox\" name=\"" + item.id + "\"" + "checked disabled>" +
                        "</td>" +
                        "<td>" + item.term_year + "</td>" +
                        "<td>" + item.term_sem + "</td>" +
                        "<td>" + item.course_num + "</td>" +
                        "<td>" + item.course_cname + "</td>" +
                        "<td>" + item.course.names + "</td>" +
                        "<td>" + item.statusC
                    if (item.unlock) {
                        s += "<button onclick=\"unlock(" + item.id + ")\"class=\"btn btn-danger\">取消勾選 </button>";
                    }
                    s += "</td>";
                    s += "<td>" + item.content + "</td>" +
                        "<td>" + item.verify_message + "</td>" +
                        "<td>" + item.verify_name + "</td>";
                    s += "<td>";
                    $.each(item.courseRecordDocumentList, function (index, crd) {
                        s += "<a href="+ crd.dlLink+">"+ crd.original_filename + "</a>";
                        s += "</br>";
                    });
                    s += "</td>";
                    s += "</tr>";
                    $("#table1").append(s);

                });

                $("#amount").html(count);
                if (count >${maxAmount}) {
                    $("#submit").attr('disabled', true);
                } else {
                    $("#submit").attr('disabled', false);
                }
            }
        });
    }

    function submited() {
        if (confirm("確定要送出? 送出後將無法修改")) {
            $.ajax({
                url: ".//courseRecordCheck",
                type: 'post',
                data: {
                    year: ${year},
                    semester: ${semester},
                    submited: "1"
                },
                success: function (data) {
                    window.location.replace(location.href);
                    //renew();
                }
            });
        }
    }
</script>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="panel-body">
        <div class="panel panel-primary">
            <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>
                <b>${config.courseStudyRecord.activeYear} - ${config.courseStudyRecord.activeSem}
                    課程學習成果</b>
                <b>上傳時間</b>
                ${DateUtils.formatDateTime(config.courseStudyRecord.studentStartDateTime)}
                ~
                ${DateUtils.formatDateTime(config.courseStudyRecord.studentEndDateTime)}
            </div>
        </div>
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
                        <div class="row">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6">
                                <div class="col-sm-2"></div>
                                <div class="col-sm-10"><input type="button" onclick="query()" class="btn btn-warning"
                                                              value="查詢"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>課程學習成果
            </div>
            <div class="panel-body">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <th class="text-center">勾選</th>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">科目名稱</th>
                        <th class="text-center">授課教師</th>
                        <th class="text-center">狀態</th>
                        <th class="text-center">內容簡述</th>
                        <th class="text-center">驗證訊息</th>
                        <th class="text-center">認證教師</th>
                        <th class="text-center">檔案</th>
                    </tr>
                    <c:if test="${courseRecordList.size() > 0}">
                        <c:forEach var="i" begin="0" end="${courseRecordList.size()-1}">
                            <tr>
                                <td>
                                    <c:if test="${active}">
                                        <input type="checkbox" id="checkbox" name="${courseRecordList.get(i).id}"
                                            <c:if test="${courseRecordList.get(i).check == 1}">
                                                   checked
                                            </c:if>
                                        >
                                    </c:if>

                                </td>
                                <td>${courseRecordList.get(i).term_year}</td>
                                <td>${courseRecordList.get(i).term_sem}</td>
                                <td>${courseRecordList.get(i).course_num}</td>
                                <td>${courseRecordList.get(i).course_cname}</td>
                                <td>${courseRecordList.get(i).course.names}</td>
                                <td>${courseRecordList.get(i).getStatusC()}</td>
                                <td>${courseRecordList.get(i).content}</td>
                                <td>${courseRecordList.get(i).verify_message}</td>
                                <td>${courseRecordList.get(i).verify_name}</td>
                                <td>
                                    <c:forEach items="${courseRecordList.get(i).courseRecordDocumentList}" var="crd">
                                        <p>
                                            <a target="_blank" href="${crd.dlLink}"
                                            ><c:out value="${crd.original_filename}"/></a>
                                        </p>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <div id="pagination"></div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>已勾選課程學習成果
            </div>
            <div class="panel-body">
                <table class="table table-bordered" id="table1" style="text-align: center">
                    <tr>
                        <th class="text-center">勾選</th>
                        <th class="text-center">學年</th>
                        <th class="text-center">學期</th>
                        <th class="text-center">課號</th>
                        <th class="text-center">科目名稱</th>
                        <th class="text-center">授課教師</th>
                        <th class="text-center">狀態</th>
                        <th class="text-center">內容簡述</th>
                        <th class="text-center">驗證訊息</th>
                        <th class="text-center">認證教師</th>
                        <th class="text-center">檔案</th>
                    </tr>
                </table>
                <div class="panel-body">數量:
                    <div id="amount" style="display:inline">0</div>
                    <div style="display:inline">/${maxAmount}</div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <div class="col-sm-2"></div>
                            <c:if test="${active}">
                                <div class="col-sm-10"><input type="button" onclick="submited()" id="submit"
                                                              class="btn btn-warning"
                                                              value="送出"></div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
