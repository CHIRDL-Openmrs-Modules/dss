<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require allPrivileges="Manage DSS" otherwise="/login.htm" redirect="/module/dss/manage_rules.form" />
<link href="${pageContext.request.contextPath}/moduleResources/dss/dss.css" type="text/css" rel="stylesheet" />
<html>
<body>
<p><b>Please choose a rule action:</b></p>
<form action="manage_rules.form" method="post">
    <input type="submit" value="Search rules" />
    <input id="successViewName" name="successViewName" type="hidden" value="searchRules.form"/>
</form>
<br>
<form action="manage_rules.form" method="post">
    <input id="submitRulePrioritizationButton" name="submitRulePrioritizationButton" type="submit" value="Prioritize rules" />
    <input id="successViewName" name="successViewName" type="hidden" value="rulePrioritization.form"/>
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>
</body>
</html>