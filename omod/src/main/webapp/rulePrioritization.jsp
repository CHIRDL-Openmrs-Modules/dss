<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp" %>
<!DOCTYPE html>
<openmrs:require allPrivileges="View Encounters, View Patients, View Concept Classes" otherwise="/login.htm" redirect="/module/dss/rulePrioritization.form" />
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/dss/rulePrioritization.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/dss/jquery-ui-1.11.2/jquery-ui.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/dss/jquery-ui-1.11.2/jquery-ui.structure.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/dss/jquery-ui-1.11.2/jquery-ui.theme.min.css"/>
<script src="${pageContext.request.contextPath}/moduleResources/dss/jquery-1.9.1.min.js"></script>
<script src="${pageContext.request.contextPath}/moduleResources/dss/jquery-ui-1.11.2/jquery-ui.min.js"></script>
<script>var ctx = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/moduleResources/dss/rulePrioritization.js"></script>
<title>Rule Prioritization</title>
</head>
<body>

<div id="ruleContent">
    <div id="title"><h1>Rule Prioritization</h1></div>

    <form id="rulePrioritizationForm" name="rulePrioritizationForm" action="rulePrioritization.form" method="post">
        <div id="ruleTypeDiv">
	        <fieldset>
	            <select name="ruleTypeSelect" id="ruleTypeSelect">
	                <option selected="selected">Please Choose a Rule Type</option>
	                <option>Create New...</option>
	                <c:forEach items="${ruleTypes}" var="ruleType">
	                    <option>${ruleType.name}</option>
	                </c:forEach>
	            </select>
	        </fieldset>
        </div>
        
        <div id="ruleListsDiv">
            <div class="ruleListDiv">
                <h2>Available Rules</h2>
                <ul id="availableRules" class="connectedSortable">
				  <li class="ui-state-default">Item 1</li>
				  <li class="ui-state-default">Item 2</li>
				  <li class="ui-state-default">Item 3</li>
				  <li class="ui-state-default">Item 4</li>
				  <li class="ui-state-default">Item 5</li>
				</ul>
            </div>
            <div class="ruleListDiv">
                <h2>Prioritized Rules</h2>
                <ul id="prioritizedRules" class="connectedSortable">
                  <li class="ui-state-default">Item 6</li>
                  <li class="ui-state-default">Item 7</li>
                  <li class="ui-state-default">Item 8</li>
                  <li class="ui-state-default">Item 9</li>
                  <li class="ui-state-default">Item 10</li>
                </ul>
            </div>
            <div class="ruleListDiv">
                <h2>Non-Prioritized Rules</h2>
                <ul id="nonPrioritizedRules" class="connectedSortable">
                  <li class="ui-state-default">Item 11</li>
                  <li class="ui-state-default">Item 12</li>
                  <li class="ui-state-default">Item 13</li>
                  <li class="ui-state-default">Item 14</li>
                  <li class="ui-state-default">Item 15</li>
                </ul>
            </div>
        </div>
    </form>
</div>

</body>
</html>
