<%--@elvariable id="accountList" type="model.ModelList<model.Account>"--%>
<%--@elvariable id="itemsPerPage" type="java.lang.Integer"--%>
<%--@elvariable id="page" type="java.lang.Integer"--%>
<%--@elvariable id="keyword" type="java.lang.String"--%>
<%--@elvariable id="keywordJson" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <jsp:include page="../includes/header.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/menuBar/_menuFactory.jsp"/>
    <div class="row">
        <div class="col-sm-12">
            <div style="background: #FFF; padding: .5rem">
                <div class="panel panel-info">
                    <div class="panel-heading">搜尋條件</div>
                    <div class="panel-body">
                        <form method="GET" class="form">
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-1">
                                        <label for="keyword">關鍵字</label>
                                    </div>
                                    <div class="col-xs-11">
                                        <input type="search" name="keyword" id="keyword"
                                               value="${fn:escapeXml(keyword)}"
                                               class="form-control"/>

                                        <p></p>
                                        <p>
                                            <input type="submit" value="查詢" class="btn btn-primary">
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <table class="table" style="table-layout: fixed">
                    <thead>
                    <tr>
                        <th width="40">#</th>
                        <th width="120">編號</th>
                        <th>姓名</th>
                        <th style="width: 120px;">
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${accountList.list}" var="acc">
                        <tr data-role-code="${acc.roleCode}" class="acc-row">
                            <td>${acc.seqNo}</td>
                            <td class="cell-staff-no">${fn:escapeXml(acc.regNumber)}</td>
                            <td class="cell-name">${fn:escapeXml(acc.name)}</td>
                            <td>
                                <c:if test="${acc.authority.manager}">
                                    <button class="btn btn-danger edit-btn" value="0">移除校管理員</button>
                                </c:if>
                                <c:if test="${!acc.authority.manager}">
                                    <button class="btn btn-success edit-btn" value="1">設為校管理員</button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="4">

                            <div id="pagination"></div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>

    <script>
    $('#pagination').pagination({
        items: ${accountList.total},
        itemsOnPage: ${itemsPerPage},
        hrefTextPrefix: "?keyword=" + encodeURIComponent(${keywordJson}) + "&page=",
        currentPage: ${page},
        cssStyle: 'light-theme',
        ellipsePageSet: false
    });
    </script>
    <script>

    $('.edit-btn').click(function () {
        this.disabled = true;
        let tr = $(this).parents('.acc-row');
        let staffCode = tr.find('td.cell-staff-no').text();
        let setManager = this.value * 1;

        $.ajax({
            url: '',
            method: 'POST',
            data: {
                setManager,
                staffCode,
            },
            dateType: 'json'
        }).done(r => {
            if (r) {
                location.reload();
                alert('設定完成');
            } else {
                alert('系統發生錯誤');
            }
        }).fail(r => {
            alert('系統發生錯誤');
        });
    });
    </script>
</div><!-- /container -->
</body>
</html>
