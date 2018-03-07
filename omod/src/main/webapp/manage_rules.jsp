<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<link href="${pageContext.request.contextPath}/moduleResources/dss/dss.css" type="text/css" rel="stylesheet" />
<p><b>Please choose a rule action:</b></p>
<form action="searchRules.form" method="get">
    <input type="submit" value="Search rules" />
</form>
<br>
<form action="rulePrioritization.form" method="get">
    <input type="submit" value="Prioritize rules" />
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>