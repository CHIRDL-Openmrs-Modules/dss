<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<link href="${pageContext.request.contextPath}/moduleResources/dss/dss.css" type="text/css" rel="stylesheet" />

<p>
Please choose a rule to test:
</p>
<form name="input" action="ruleTester.form" method="get">
<select name="ruleName">
<c:forEach items="${rules}" var="rule">
<option value="${rule.tokenName}"
<c:if test="${rule.tokenName==lastRuleName}">
selected
</c:if>
>${rule.tokenName}</option>
</c:forEach>
</select>
or enter a rule name to execute:
<input type="text" name="nonMLMRulename" value="${nonMLMRulename}"/><br />
<p>Rule parameters (comma delimited)</p>
<input type="text" name="parameters" value="${parameters}"/>
<p>
Please enter the patient's mrn:
</p>
<input type="text" name="mrn" value="${lastMRN}"/>
<input type="submit" value="Test Rule">
</form>
<p>
Result from running <b>${lastRuleName}</b> was:
</p><br/><br/>
<c:if test="${!empty runResult}">

 <b>${runResult}</b>

</c:if>


<%@ include file="/WEB-INF/template/footer.jsp" %>