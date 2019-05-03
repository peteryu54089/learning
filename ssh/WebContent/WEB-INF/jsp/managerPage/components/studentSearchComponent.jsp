<%--@elvariable id="classList" type="java.util.List<model.CUnit>"--%>
<%--@elvariable id="q_class" type="java.lang.String"--%>
<%--@elvariable id="q_regno" type="java.lang.String"--%>
<%--@elvariable id="q_name" type="java.lang.String"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2018/12/03
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="panel panel-primary">
    <div class="panel-heading"><span class="glyphicon glyphicon-calendar" aria-hidden="true">　</span>
        <c:out value="${param[\"title\"]}"/>
    </div>
    <div class="panel-body">
        <form method="GET" class="form">
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-4">
                        <div class="row">
                            <label for="class" class="col-sm-2 text-right">班級</label>
                            <div class="col-sm-10">
                                <select id="class" name="class" class="form-control">
                                    <option value="">ALL</option>
                                    <c:forEach var="c" items="${classList}">
                                        <option VALUE="<c:out value="${c.cls_code}"/>"
                                                <c:if test="${c.cls_code.equals(q_class)}">
                                                    selected
                                                </c:if>
                                        >
                                            <c:out value="${c.cunit_name}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="row">
                            <label for="regno" class="col-sm-2 text-right">學號</label>
                            <div class="col-sm-10">
                                <input type="text" id="regno" name="regno" class="form-control"
                                       value="<c:out value="${q_regno}"/>"
                                >
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="row">
                            <label for="name" class="col-sm-2 text-right">姓名</label>
                            <div class="col-sm-10">
                                <input type="text" id="name" name="name" class="form-control"
                                       value="<c:out value="${q_name}"/>"
                                >
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="form-group text-right">
                <input type="submit" class="btn btn-primary"
                                                  value="&emsp;&emsp;查詢&emsp;&emsp;">
            </div>
        </form>
    </div>
</div>