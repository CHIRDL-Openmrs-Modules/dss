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
    <form id="rulePrioritizationForm" method="POST" action="rulePrioritization.form">
        <div id="ruleTypeDiv">
	        <fieldset>
	            <select name="ruleTypeSelect" id="ruleTypeSelect">
	                <option selected="selected">Please Choose a Rule Type</option>
	                <option value="Create New">Create New...</option>
	                <c:forEach items="${ruleTypes}" var="ruleType">
	                    <option value="${ruleType.name}">${ruleType.name}</option>
	                </c:forEach>
	            </select>
	        </fieldset>
        </div>
        
        <div id="ruleListTitlesDiv">
            <div class="ruleListTitleDiv">
                <h2>Available Rules</h2>
            </div>
            <div class="ruleListTitleDiv">
                <h2>Prioritized Rules</h2>
            </div>
            <div class="ruleListTitleDiv">
                <h2>Non-Prioritized Rules</h2>
            </div>
        </div>
        <div id="progressBarsDiv">
            <div id="availableRulesPB" class="progressBarDiv"><img src="${pageContext.request.contextPath}/moduleResources/dss/images/ajax-progress.gif"></div>
            <div id="prioritizedRulesPB" class="progressBarDiv"><img src="${pageContext.request.contextPath}/moduleResources/dss/images/ajax-progress.gif"></div>
            <div id="nonPrioritizedRulesPB" class="progressBarDiv"><img src="${pageContext.request.contextPath}/moduleResources/dss/images/ajax-progress.gif"></div>
        </div>
        <div id="ruleListsDiv">
            <div class="ruleListDiv">
                <ul id="availableRules" class="connectedSortable">
				</ul>
            </div>
            <div class="ruleListDiv">
                <ul id="prioritizedRules" class="connectedSortable">
                </ul>
            </div>
            <div class="ruleListDiv">
                <ul id="nonPrioritizedRules" class="connectedSortable">
                </ul>
            </div>
        </div>
        <div class="submit">
           <input type="button" id="submitButton" value="Save"/>
        </div>
        <div id="errorDialog" title="Error" class="ui-dialog-titlebar ui-widget-header" style="overflow-x: hidden;">
            <div style="margin: 0 auto;text-align: center;">
                <div id="errorMessage" style="color:#000000;">${errorMessage}</div>
            </div>
        </div>
        <div id="submitConfirmationDialog" title="Confirm Save" class="ui-dialog-titlebar ui-widget-header" style="overflow-x: hidden;">
            <div style="margin: 0 auto;text-align: center;">
                <div style="color:#000000;">Are you sure you want to save the changes?</div>
            </div>
        </div>
        <input id="availableRulesSave" name="availableRulesSave" type="hidden" value=""/>
        <input id="prioritizedRulesSave" name="prioritizedRulesSave" type="hidden" value=""/>
        <input id="nonPrioritizedRulesSave" name="nonPrioritizedRulesSave" type="hidden" value=""/>
        <input id="test" name="test" type="hidden" value="This is my test."/>
    </form>
</div>

</body>
</html>
