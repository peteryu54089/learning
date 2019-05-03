<%--@elvariable id="isNight" type="java.lang.Boolean"--%>
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
            <div class="panel panel-info">
                <div class="panel-heading">各項紀錄下載</div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-12">
                            <form action="SubmitCourse" method="POST" class="form form-inline">
                                <div class="form-group">
                                    <label>學業表現上傳資料欄位下載</label>
                                    <label for="course_year_sem">學期</label>&nbsp;&nbsp;
                                    <input name="course_year_sem"
                                           id="course_year_sem" type="text" required class="form-control"
                                           style="display: inline-block; width: auto;"
                                           pattern="^\d{2,3}[12]$"
                                           value="1031"/>
                                </div>
                                <div class="form-group">
                                    <label for="form_idx">下載項目</label>
                                    <select id="form_idx" name="form_idx" class="form-control" required>
                                        <option value="1">1_科目名冊.xlsx</option>
                                        <option value="2">2_學生資料名冊.xlsx</option>
                                        <c:if test="${!isNight}">
                                            <option value="3">3_學生成績名冊.xlsx</option>
                                        </c:if>
                                        <option value="4">4_學生重修重讀成績名冊.xlsx</option>
                                        <c:if test="${isNight}">
                                            <option value="5">5_進修學校學生成績名冊.xlsx</option>
                                        </c:if>
                                        <option value="6">6_缺勤紀錄名冊.xlsx</option>
                                        <c:if test="${!isNight}">
                                            <option value="7">7_學生課程學習成果名冊.xlsx</option>
                                            <option value="8">8_學生重修重讀課程學習成果名冊.xlsx</option>
                                        </c:if>
                                        <c:if test="${isNight}">
                                            <option value="9">9_進修學校學生課程學習成果名冊.xlsx</option>
                                        </c:if>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <button class="btn btn-primary" type="submit">下載</button>
                                </div>
                            </form>
                        </div>
                        
                        <hr/>
                        
                        <div class="col-xs-12">
                        <%-- 
                            <form action="SubmitPerformance" method="POST" class="form">
                                <div class="form-group">
                                    <p>
                                        <label>校內幹部紀錄下載</label>
                                    </p>
                                    <p>
                                        <label for="cad_year_sem">學期</label>&nbsp;&nbsp;
                                        <input name="submitYear"
                                               id="cad_year_sem" type="text" required class="form-control"
                                               style="display: inline-block; width: auto;"
                                               pattern="^\d{2,3}[12]$"
                                               value="1062"/>
                                        <button type="submit" class="btn btn-primary" name="action" value="CADRE">
                                            	下載校級幹部提交檔案
                                        </button>
                                    </p>
                                </div>
                            </form>
							--%>
                            <hr/>
                            
                            <form action="SubmitPerformance" method="POST" class="form">
                                <div class="form-group">
                                    <p>
                                        <label>學生自填多元表現資料</label>
                                    </p>
                                    <p>
                                        <label for="per_year_sem">學年</label>&nbsp;&nbsp;
                                        <input name="term_year"
                                               id="per_year_sem" type="number" required class="form-control"
                                               style="display: inline-block; width: auto;"
                                               min="50" max="999"
                                               value="107"/>
                                        <button type="submit" class="btn btn-primary" name="action" value="PERFORMANCE">
                                        	下載學生自填表現提交檔案
                                        </button>
                                    </p>
                                </div>
                            </form>
                            
                            <hr/>
                            
 							<%-- 
                            <form action="recordExporter" method="POST" class="form" target="_blank">
                                <div class="form-group">
                                    <p>
                                        <label>自傳與讀書計畫</label>
                                    </p>
                                    <p>
                                    	<button type="submit" class="btn btn-primary" name="grades" value="3">
                                            	下載應屆畢業生之自傳與讀書計畫
                                        </button>
                                    </p>
                                </div>
                            </form>
                            --%>
                            <hr/>
                        </div>
                    </div>
                </div>
            </div> <!-- /panel panel-info -->
        </div>  <!-- /col-sm-12 -->
	</div><!-- /row -->
</div><!-- /container -->
</body>
</html>
