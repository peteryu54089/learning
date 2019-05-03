<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="form-group performance-bar">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-filter" data-target="cadre" onclick="window.location.href='<%=request.getContextPath()%>/cadre'">外校幹部經歷
        </button>
        <button type="button" class="btn btn-warning btn-filter" data-target="competition" onclick="window.location.href='<%=request.getContextPath()%>/competition'">競賽成果
        </button>
        <button type="button" class="btn btn-danger btn-filter" data-target="license" onclick="window.location.href='<%=request.getContextPath()%>/license'">檢定證照
        </button>
        <button type="button" class="btn btn-primary btn-filter" data-target="volunteer" onclick="window.location.href='<%=request.getContextPath()%>/volunteer'">志工服務
        </button>
        <button type="button" class="btn btn-info btn-filter" data-target="other" onclick="window.location.href='<%=request.getContextPath()%>/other'" >其它</button>
    </div>
</div>
