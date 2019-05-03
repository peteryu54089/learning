<%--@elvariable id="list" type="java.util.List<model.Announcement>"--%>
<%--- <%@ page import="model.Announcement" %>  ---%>
<%--- <%@ page import="util.DateUtils" %> ---%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                <table class="table" style="table-layout: fixed">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>發布區間</th>
                        <th>內文</th>
                        <th style="width: 130px;">
                            <div class="text-right">
                                <button class="btn btn-sm btn-success" id="addNewBtn">新增公告
                                </button>
                            </div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="a">
                        <tr data-json="${fn:escapeXml(Announcement.getGson().toJson(a))}" class="ann-row">
                            <td>${a.id}</td>
                            <td>${DateUtils.formatDate(a.publishStart)} ~ ${DateUtils.formatDate(a.publishEnd)}</td>
                            <td>${fn:escapeXml(a.content)}</td>
                            <td>
                                <button class="btn btn-warning edit-btn">修改</button>
                                <button class="btn btn-danger del-btn">刪除</button>
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
        <div class="modal-dialog">

            <!-- Modal content-->
            <form class="modal-content" id="form">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">新增/編輯公告</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label for="publishStart">公告起始日</label>
                                <input type="datetime-local" name="publishStart" id="publishStart" class="form-control"
                                       required/>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label for="publishEnd">公告截止日</label>
                                <input type="datetime-local" name="publishEnd" id="publishEnd" class="form-control"
                                       required/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="content">內文</label>
                        <textarea name="content" id="content" cols="30" rows="10" class="form-control"
                                  required></textarea>
                    </div>
                    <input type="hidden" id="id" name="id" value="-1"/>
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
        items: ${amount},
        itemsOnPage: ${itemsPerPage},
        hrefTextPrefix: "?page=",
        currentPage:${page},
        cssStyle: 'light-theme',
        ellipsePageSet: false
    });
    </script>
    <script>
    let form = $('#form');
    let modal = $('#myModal');
    let showEditForm = function (data) {
        let keys = 'publishStart|publishEnd|content|id'.split('|');
        keys.forEach(k => {
            form.find('#' + k).val(data[k]);
        });
        modal.modal('show');
        setTimeout(function () {
            modal.find('#content').select();
        }, 250);
    };
    form.submit(function (e) {
        e.preventDefault();
        let dataArr = $(this).serializeArray(true);
        let data = {};
        dataArr.forEach(v => {
            data[v.name] = v.value;
        });

        data.publishStart = new Date(Date.parse(data.publishStart)).toJSON();
        data.publishEnd = new Date(Date.parse(data.publishEnd)).toJSON();

        data.id *= 1;

        let fetchParams = {
            body: JSON.stringify(data),
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {
                'content-type': 'application/json'
            },
            method: 'POST',
            redirect: 'follow',
            referrer: 'no-referrer',
        };

        if (data.id === -1) {
            return fetch("./announcementManage", fetchParams)
                .then(response => {
                    if (Math.floor(response.status / 100) === 2) {
                        alert('送出成功');
                        location.href = ('./announcementManage');
                    }
                })
        } else {
            fetchParams.method = 'PUT';
            return fetch("./announcementManage", fetchParams)
                .then(response => {
                    if (Math.floor(response.status / 100) === 2) {
                        alert('送出成功');
                        location.reload();
                    }
                })
        }
    });

    $('#addNewBtn').click(function () {
        let now = new Date();
        showEditForm({
            id: -1,
            publishStart: DateUtils.formatDate(now) + 'T' + DateUtils.formatTime(now),
            publishEnd: DateUtils.formatDate(now.getTime() + 86400 * 1000 * 7) + 'T' + DateUtils.formatTime(now),
            content: ""
        });
    });

    $('.edit-btn').click(function () {
        let tr = $(this).parents('.ann-row');
        let data = {...tr.data('json')};
        
        console.log('data.publishStart:' + data.publishStart);
        console.log('data.publishEnd:' + data.publishEnd);
        
        data.publishStart = DateUtils.toInputValue(data.publishStart);
        data.publishEnd = DateUtils.toInputValue(data.publishEnd);
        showEditForm(data);
    });

    $('.del-btn').click(function () {
        let tr = $(this).parents('.ann-row');
        let data = {...tr.data('json')};
        if (confirm("你確定要刪除此公告?")) {
            let fetchParams = {
                cache: 'no-cache',
                credentials: 'same-origin',
                headers: {
                    'content-type': 'application/json'
                },
                method: 'DELETE',
                redirect: 'follow',
                referrer: 'no-referrer',
            };
            fetch("./announcementManage?id=" + data.id, fetchParams)
                .then(response => {
                    if (Math.floor(response.status / 100) === 2) {
                        alert('刪除成功');
                        location.reload();
                    }
                })
        }
    });
    </script>
</div><!-- /container -->
</body>
</html>
