<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2018/11/26
  Time: 22:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="panel panel-info">
    <div class="panel-heading"><span class="glyphicon glyphicon-exclamation-sign">　</span>通知</div>
    <div class="panel-body">
        <ul id="announcement-list">
            <li class="template">
                <p class="publish-date badge"></p>
                <p class="content"></p>
            </li>
        </ul>
        <script>
        (function () {
            let list = $("#announcement-list");
            let templateLi = list.find('.template').clone(true);
            list.empty();
            $.ajax({
                url: "./announcement",
                dataType: 'json',
                cache: false,
            }).done(function (annList) {
                annList.forEach(ann => {
                    let li = templateLi.clone(true);
                    li.find('.publish-date').text(
                        DateUtils.toMingoDate(Date.parse(ann.publishStart)) + ' ~ ' + DateUtils.toMingoDate(Date.parse(ann.publishEnd))
                    );
                    let lenLimit = 20;
                    let contentElem = li.find('.content');
                    if (ann.content.length > lenLimit) {
                        let cutContent = ann.content.substr(0, lenLimit) + ' ...';
                        contentElem.text(cutContent);
                        li.addClass('hasMore');
                        contentElem.data('fullContent', ann.content);
                        contentElem.data('cutContent', cutContent);
                    } else {
                        contentElem.text(ann.content);
                    }

                    list.append(li);
                });

                list.find('li.hasMore').click(function () {
                    let contentElem = $(this).find('.content');
                    if (this.classList.contains('showFull')) {
                        this.classList.remove('showFull');
                        contentElem.text(contentElem.data('cutContent'));
                    } else {
                        this.classList.add('showFull');
                        contentElem.text(contentElem.data('fullContent'));
                    }
                });
            })
        })();
        </script>
    </div>
</div>