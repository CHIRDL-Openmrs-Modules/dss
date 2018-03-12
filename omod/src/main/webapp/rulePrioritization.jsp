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
        <div id="ruleListsDiv">
            <div class="ruleListDiv">
                <ul id="availableRules" class="connectedSortable">
				  <li class="ui-state-default">Item 1</li>
				  <li class="ui-state-default">Item 2</li>
				  <li class="ui-state-default">Item 3</li>
				  <li class="ui-state-default">Item 4</li>
				  <li class="ui-state-default">Item 5</li>
				  <li class="ui-state-default">Item 6</li>
                  <li class="ui-state-default">Item 7</li>
                  <li class="ui-state-default">Item 8</li>
                  <li class="ui-state-default">Item 9</li>
                  <li class="ui-state-default">Item 10</li>
                  <li class="ui-state-default">Item 11</li>
                  <li class="ui-state-default">Item 12</li>
                  <li class="ui-state-default">Item 13</li>
                  <li class="ui-state-default">Item 14</li>
                  <li class="ui-state-default">Item 15</li>
				</ul>
            </div>
            <div class="ruleListDiv">
                <ul id="prioritizedRules" class="connectedSortable">
                  <li class="ui-state-default">Item 16</li>
                  <li class="ui-state-default">Item 17</li>
                  <li class="ui-state-default">Item 18</li>
                  <li class="ui-state-default">Item 19</li>
                  <li class="ui-state-default">Item 20</li>
                  <li class="ui-state-default">Item 21</li>
                  <li class="ui-state-default">Item 22</li>
                  <li class="ui-state-default">Item 23</li>
                  <li class="ui-state-default">Item 24</li>
                  <li class="ui-state-default">Item 25</li>
                  <li class="ui-state-default">Item 26</li>
                  <li class="ui-state-default">Item 27</li>
                  <li class="ui-state-default">Item 28</li>
                  <li class="ui-state-default">Item 29</li>
                  <li class="ui-state-default">Item 30</li>
                </ul>
            </div>
            <div class="ruleListDiv">
                <ul id="nonPrioritizedRules" class="connectedSortable">
                  <li class="ui-state-default">Item 31</li>
                  <li class="ui-state-default">Item 32</li>
                  <li class="ui-state-default">Item 33</li>
                  <li class="ui-state-default">Item 34</li>
                  <li class="ui-state-default">Item 35</li>
                  <li class="ui-state-default">Item 36</li>
                  <li class="ui-state-default">Item 37</li>
                  <li class="ui-state-default">Item 38</li>
                  <li class="ui-state-default">Item 39</li>
                  <li class="ui-state-default">Item 40</li>
                  <li class="ui-state-default">Item 41</li>
                  <li class="ui-state-default">Item 42</li>
                  <li class="ui-state-default">Item 43</li>
                  <li class="ui-state-default">Item 44</li>
                  <li class="ui-state-default">Item 45</li>
                </ul>
            </div>
        </div>
    </form>
</div>

</body>
</html>
