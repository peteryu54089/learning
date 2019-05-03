<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="cadreList" type="java.util.List<model.Cadre>"--%>
<%--@elvariable id="competitionList" type="java.util.List<model.Competition>"--%>
<%--@elvariable id="licenseList" type="java.util.List<model.License>"--%>
<%--@elvariable id="volunteerList" type="java.util.List<model.Volunteer>"--%>
<%--@elvariable id="otherList" type="java.util.List<model.Other>"--%>
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

    function download(a, b) {
        <c:if test="${type == 1}">
        window.location.href = './downloadCadre?no=' + a + '&id=' + b;
        </c:if>
        <c:if test="${type == 2}">
        window.location.href = './downloadCompetition?no=' + a + '&id=' + b;
        </c:if>
        <c:if test="${type == 3}">
        window.location.href = './downloadLicense?no=' + a + '&id=' + b;
        </c:if>
        <c:if test="${type == 4}">
        window.location.href = './downloadVolunteer?no=' + a + '&id=' + b;
        </c:if>
        <c:if test="${type == 5}">
        window.location.href = './downloadOther?no=' + a + '&id=' + b;
        </c:if>
    }
</script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-filter" data-target="cadre"
                onclick="window.location.href='./tutorViewPerformance?rgno=${rgno}&type=1'">外校幹部經歷
        </button>
        <button type="button" class="btn btn-warning btn-filter" data-target="competition"
                onclick="window.location.href='./tutorViewPerformance?rgno=${rgno}&type=2'">競賽成果
        </button>
        <button type="button" class="btn btn-danger btn-filter" data-target="license"
                onclick="window.location.href='./tutorViewPerformance?rgno=${rgno}&type=3'">檢定證照
        </button>
        <button type="button" class="btn btn-primary btn-filter" data-target="volunteer"
                onclick="window.location.href='./tutorViewPerformance?rgno=${rgno}&type=4'">志工服務
        </button>
        <button type="button" class="btn btn-info btn-filter" data-target="other"
                onclick="window.location.href='./tutorViewPerformance?rgno=${rgno}&type=5'">其它
        </button>
    </div>
</div>
<div class="col-sm-16">
    <div class="panel panel-danger">
        <div class="panel-heading"><span class="glyphicon glyphicon-user" aria-hidden="true">　</span>
            <c:if test="${type == 1}">
                幹部經歷
            </c:if>
            <c:if test="${type == 2}">
                競賽成果
            </c:if>
            <c:if test="${type == 3}">
                檢定證照
            </c:if>
            <c:if test="${type == 4}">
                志工服務
            </c:if>
            <c:if test="${type == 5}">
                幹部經歷
            </c:if>

        </div>
        <div class="panel-body">
            <table class="table table-bordered" style="text-align: center">
                <c:if test="${type == 1}">
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
                    </tr>
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
                            </tr>
                        </c:forEach>
                    </c:if>
                </c:if>
                <c:if test="${type == 2}">
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
                                <td>${competitionList.get(i).type}</td>
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
                            </tr>


                        </c:forEach>
                    </c:if>
                </c:if>
                <c:if test="${type == 3}">
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
                            </tr>
                        </c:forEach>
                    </c:if>
                </c:if>
                <c:if test="${type == 4}">
                    <tr>
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
                </c:if>
                <c:if test="${type == 5}">
                    <tr>
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
                </c:if>
            </table>
        </div>
        <div id="pagination"></div>
    </div>
</div>
</html>
