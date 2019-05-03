<%--@elvariable id="config" type="model.SystemConfig"--%>
<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 2017/7/21
  Time: 下午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="util.DateUtils" %>
<%--@elvariable id="cadreList" type="java.util.List<model.Cadre>"--%>
<%--@elvariable id="competitionList" type="java.util.List<model.Competition>"--%>
<%--@elvariable id="licenseList" type="java.util.List<model.License>"--%>
<%--@elvariable id="volunteerList" type="java.util.List<model.Volunteer>"--%>
<%--@elvariable id="otherList" type="java.util.List<model.Other>"--%>
<% request.setCharacterEncoding("UTF-8"); %>
<head>
    <jsp:include page="../includes/header.jsp"></jsp:include>
    <script>
    $(document).ready(function () {
        renew();
    });

    jQuery(function () {
        $(":checkbox").click(function () {
            state = $(this).is(":checked");
            id = $(this).attr('name');
            k = id.split("_");
            if (state)
                state = "1";
            else
                state = "0";
            $.ajax({
                url: "./performanceCheck",
                type: 'post',
                data: {
                    type: k[0],
                    id: k[1],
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
            url: "./performanceCheckInfo",
            type: 'post',
            data: {},
            success: function (data) {
                $("#table1").find("tr:gt(0)").remove();
                count = 0;
                $.each(data, function (index, item) {
                    count++;
                    let s = "<tr>" +
                        "<td>" + "<input type=\"checkbox\" id=\"checkbox\"" + "checked disabled>" +
                        "</td>" +
                        "<td>" + item.typeC + "</td>" +
                        "<td>" + item.name + "</td>" +
                        "<td>" + item.date + "</td>" +
                        "<td>" + item.content + "</td>" +
                        "<td>" +
                        "<a href=\"" + item.document_link + "\">" + item.document_original_filename +
                        "</td>";
                    if (item.video_original_filename != null) {
                        s += "<td>" +
                            "<a href=\"" + item.video_link + "\">" + item.video_original_filename +
                            "</td>"
                    } else {
                        s += "<td>--</td>";
                    }
                    s += "<td><a href=\"" + item.externalLink + "\"target=\"_blank\">" + item.externalLink + "</a></td>";
                    s += "<td>" + item.status;
                    if (item.unlock == "true") {
                        s += "<button onclick=\"unlock(" + item.type + ',' + item.id + ")\"class=\"btn btn-danger\">取消勾選 </button>";
                    }
                    s += "</td></tr>";
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
                url: ".//performanceCheck",
                type: 'post',
                data: {
                    submited: "1"
                },
                success: function (data) {
                    window.location.replace(location.href);
                    //renew();
                }
            });
        }
    }

    function unlock(type, id) {
        if (confirm("確定要取消勾選?")) {
            $.ajax({
                url: ".//performanceUnlock",
                type: 'post',
                data: {
                    type: type,
                    id: id
                },
                success: function (data) {
                    window.location.replace(location.href);
                    //renew();
                }
            });
        }
    }
    </script>
</head>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>

    <script src="resources/javascript/datepicker-zh-TW.js"></script>
    <script src="resources/javascript/dateTth.js"></script>


    <div class="row">
        <div class="col-sm-12">

            <div class="panel panel-primary">
                <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>
                    <b>${config.performance.submitYear}
                        多元學習成果</b>
                    <b>上傳時間</b>
                    ${DateUtils.formatDateTime(config.performance.studentStartDateTime)}
                    ~
                    ${DateUtils.formatDateTime(config.performance.studentEndDateTime)}
                </div>
            </div>


            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>幹部經歷</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">勾選</th>
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
                        </tr>

                        <c:if test="${cadreList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${cadreList.size()-1}">
                                <tr>
                                    <td><input type="checkbox" name="cadre_${cadreList.get(i).id}"
                                    <c:if test="${cadreList.get(i).check == 1}">
                                               checked
                                    </c:if>
                                    ></td>
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
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>競賽成果</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">勾選</th>
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
                        </tr>
                        <c:if test="${competitionList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${competitionList.size()-1}">
                                <tr>
                                    <td><input type="checkbox" name="competition_${competitionList.get(i).id}"
                                    <c:if test="${competitionList.get(i).check == 1}">
                                               checked
                                    </c:if>
                                    ></td>
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
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>檢定證照</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">勾選</th>
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
                        </tr>

                        <c:if test="${licenseList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${licenseList.size()-1}">
                                <tr>
                                    <td><input type="checkbox" name="license_${licenseList.get(i).id}"
                                    <c:if test="${licenseList.get(i).check == 1}">
                                               checked
                                    </c:if>
                                    ></td>
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
                                </tr>
                            </c:forEach>
                        </c:if>


                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>志工服務</div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">勾選</th>
                            <th class="text-center">服務名稱</th>
                            <th class="text-center">服務單位</th>
                            <th class="text-center">開始日期</th>
                            <th class="text-center">結束日期</th>
                            <th class="text-center">時數</th>
                            <th class="text-center">內容簡述</th>
                            <th class="text-center">證明文件連結</th>
                            <th class="text-center">影音檔案連結</th>
                            <th class="text-center">外部影音連結</th>
                            <th class="text-center">上傳時間</th>
                        </tr>
                        <c:if test="${volunteerList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${volunteerList.size()-1}">
                                <tr>
                                    <td><input type="checkbox" name="volunteer_${volunteerList.get(i).id}"
                                    <c:if test="${volunteerList.get(i).check == 1}">
                                               checked
                                    </c:if>
                                    ></td>
                                    <td>${volunteerList.get(i).name}</td>
                                    <td>${volunteerList.get(i).place}</td>
                                    <td>${volunteerList.get(i).startTime}</td>
                                    <td>${volunteerList.get(i).endTime}</td>
                                    <td>${volunteerList.get(i).count}</td>
                                    <td>${volunteerList.get(i).content}</td>
                                    <td>
                                        <c:if test="${volunteerList.get(i).documentFile!=null}">
                                            <a href="${volunteerList.get(i).documentFile._link}">
                                                    ${volunteerList.get(i).documentFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${volunteerList.get(i).videoFile!=null}">
                                            <a href="${volunteerList.get(i).videoFile._link}">
                                                    ${volunteerList.get(i).videoFile.fileName}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${volunteerList.get(i).externalLink}" target="_blank">
                                                ${volunteerList.get(i).externalLink}</a>
                                    </td>
                                    <td>${volunteerList.get(i).getCreatedDateString()}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-danger">
                <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>其他多元表現紀錄
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th class="text-center">勾選</th>
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
                        </tr>
                        <c:if test="${otherList.size() > 0}">
                            <c:forEach var="i" begin="0" end="${otherList.size()-1}">
                                <tr>
                                    <td><input type="checkbox" name="other_${otherList.get(i).id}"
                                    <c:if test="${otherList.get(i).check == 1}">
                                               checked
                                    </c:if>
                                    ></td>
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
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
                <div id="pagination"></div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel panel-info">
            <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true">　</span>已勾選多元表現
            </div>
            <div class="panel-body">
                <table class="table table-bordered" id="table1" style="text-align: center">
                    <tr>
                        <th class="text-center">勾選</th>
                        <th class="text-center">多元表現類型</th>
                        <th class="text-center">名稱/證照代碼</th>
                        <th class="text-center">日期</th>
                        <th class="text-center">內容簡述</th>
                        <th class="text-center">文件</th>
                        <th class="text-center">影音</th>
                        <th class="text-center">外部連結</th>
                        <th class="text-center">狀態</th>
                    </tr>
                </table>
                <div class="panel-body">數量:
                    <div id="amount" style="display:inline">0</div>
                    <div style="display:inline">/${maxAmount}</div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-6">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10"><input type="button" onclick="submited()" id="submit"
                                                          class="btn btn-warning"
                                                          value="送出"></div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
