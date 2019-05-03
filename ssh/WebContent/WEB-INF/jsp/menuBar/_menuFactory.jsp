<%--
  Created by IntelliJ IDEA.
  User: s911415
  Date: 2019/03/11
  Time: 19:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--@elvariable id="selectedRole" type="model.role.Role"--%>
<jsp:include page="/WEB-INF/jsp/${selectedRole.menuBar}"/>
