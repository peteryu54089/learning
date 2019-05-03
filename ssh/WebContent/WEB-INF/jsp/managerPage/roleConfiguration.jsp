<%@ page import="com.google.gson.Gson" %>
<%--@elvariable id="accountList" type="model.ModelList<Account>"--%>
<%--@elvariable id="itemsPerPage" type="java.lang.Integer"--%>
<%--@elvariable id="page" type="java.lang.Integer"--%>
<%--@elvariable id="roleList" type="java.util.List<model.Authority.RoleIndex>"--%>
<%--@elvariable id="ignoreRoles" type="List<Authority.RoleIndex>"--%>
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
                                        <input type="search" name="keyword" id="keyword" value="${fn:escapeXml(keyword)}"
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
                        <th width="150">姓名</th>
                        <th>身分</th>
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
                            <td class="cell-roles-str">${fn:escapeXml(acc.authority.currentRolesStr)}</td>
                            <td>
                                <button class="btn btn-warning edit-btn">修改權限</button>
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
    <!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <form class="modal-content" id="form">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">修改權限</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-3">
                            <div class="form-group">
                                <label>員工編號: </label>
                                <span id="modal-staff-code"></span>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="form-group">
                                <label>姓名: </label>
                                <span id="modal-name"></span>
                            </div>
                        </div>

                        <p class="clearfix"></p>

                        <c:forEach items="${roleList}" var="role">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <input type="checkbox" value="${role.value()}" name="role"
                                           id="role_ckb_${role.value()}"/>
                                    <label for="role_ckb_${role.value()}">${fn:escapeXml(role.toString())}</label>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <input type="hidden" id="staffCode" name="staffCode" value="-1"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
                    <button type="submit" class="btn btn-primary">送出</button>
                </div>
            </form>

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
    let roles = $(document.getElementsByName('role'));

    function setRoleValue(num) {
        num *= 1;
        roles.prop('checked', false);
        roles.each(function () {
            let n = this.value * 1;
            if ((n & num) > 0)
                this.checked = true;
        });
    }

    function getRoleValue() {
        let sum = 0;
        roles.each(function () {
            if (this.checked) {
                let n = this.value * 1;
                sum |= n;
            }
        });

        return sum;
    }

    <c:forEach items="${ignoreRoles}" var="r">
    roles.filter("[value=${r.value()}]").prop('disabled', true).parent().addClass('disabled');
    </c:forEach>
    </script>
    <script>
    let form = $('#form');
    let modal = $('#myModal');
    let showEditForm = function (data) {
        setRoleValue(data.roleCode);
        $('#modal-staff-code').text(data.staffCode);
        $('#modal-name').text(data.name);
        $('#staffCode').val(data.staffCode);
        modal.modal('show');
    };
    form.submit(function (e) {
        e.preventDefault();
        let fd = new FormData(this);
        fd.append('roleCode', getRoleValue());

        let fetchParams = {
            body: fd,
            cache: 'no-cache',
            credentials: 'same-origin',
            method: 'POST',
            redirect: 'follow',
            referrer: 'no-referrer',
        };

        return fetch(this.action, fetchParams)
            .then(response => {
                if (Math.floor(response.status / 100) === 2) {
                    alert('送出成功');
                    location.reload();
                } else {
                    alert('系統發生錯誤，請聯絡系統管理員');
                }
            })
    });

    $('.edit-btn').click(function () {
        let tr = $(this).parents('.acc-row');
        let staffCode = tr.find('td.cell-staff-no').text();
        let name = tr.find('td.cell-name').text();
        let roleCode = tr.data('roleCode') * 1;

        showEditForm({
            staffCode,
            name,
            roleCode
        });
    });
    </script>
</div><!-- /container -->
</body>
</html>
