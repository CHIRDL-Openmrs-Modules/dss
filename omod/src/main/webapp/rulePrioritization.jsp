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
	                <option selected="selected" value="Please Choose a Rule Type">Please Choose a Rule Type</option>
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
        <div id="searchFieldsDiv">
            <div class="searchFieldDiv">
                <input type="text" name="availableRuleSearch" id="availableRuleSearch" value="" class="text ui-widget-content ui-corner-all" oninput="filterAvailableRules()" placeholder="Filter available rules..." title="Type in a rule name">
            </div>
            <div class="searchFieldDiv">
                <input type="text" name="prioritizedRuleSearch" id="prioritizedRuleSearch" value="" class="text ui-widget-content ui-corner-all" oninput="locatePrioritizedRules(true)" placeholder="Locate prioritized rules..." title="Type in a rule name">
            </div>
            <div class="searchFieldDiv">
                <input type="text" name="nonPrioritizedRuleSearch" id="nonPrioritizedRuleSearch" value="" class="text ui-widget-content ui-corner-all" oninput="filterNonPrioritizedRules()" placeholder="Filter non-prioritized rules..." title="Type in a rule name">
            </div>
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
        <div id="hrDiv">
            <hr>
        </div>
        <div id="progressBarsDiv">
            <div id="mainPB" class="fullWidthProgressBarDiv"><img id="mainProgressImage" src="${pageContext.request.contextPath}/moduleResources/dss/images/ajax-progress.gif"></div>
        </div>
        <div class="submit">
           <input type="button" id="submitButton" value="Save Changes"/>
        </div>
        <div id="errorDialog" title="Error" class="ui-dialog-titlebar ui-widget-header" style="overflow-x: hidden;">
            <div style="margin: 0 auto;text-align: center;">
                <div id="errorMessage" style="color:#000000;">${errorMessage}</div>
            </div>
        </div>
        <div id="successDialog" title="Success" class="ui-dialog-titlebar ui-widget-header" style="overflow-x: hidden;">
            <div style="margin: 0 auto;text-align: center;">
                <div id="successMessage" style="color:#000000;"></div>
            </div>
        </div>
        <div id="submitConfirmationDialog" title="Confirm Save" class="ui-dialog-titlebar ui-widget-header" style="overflow-x: hidden;">
            <div style="margin: 0 auto;text-align: center;">
                <div style="color:#000000;">Are you sure you want to save the changes?</div>
            </div>
        </div>
    </form>
    <div id="newRuleTypeDialog" title="Create New Rule Type">
	  <p class="validateTips"></p>
	  <form>
	    <fieldset>
	      <label for="ruleTypeName">Name</label>
	      <input type="text" name="ruleTypeName" id="ruleTypeName" value="" class="text ui-widget-content ui-corner-all">
	      <label for="ruleTypeDescription">Description</label>
	      <textarea rows="9" cols="40" maxlength=255 name="ruleTypeDescription" id="ruleTypeDescription" class="text ui-widget-content ui-corner-all"></textarea>
	 
	      <!-- Allow form submission with keyboard without duplicating the dialog button -->
	      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
	    </fieldset>
	  </form>
	</div>
</div>

</body>
</html>
