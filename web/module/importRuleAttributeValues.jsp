<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require allPrivileges="View Encounters, View Patients, View Concept Classes" otherwise="/login.htm" redirect="/module/chirdutil/importRuleAttributeValues.form" />
<link
    href="${pageContext.request.contextPath}/moduleResources/atd/atd.css"
    type="text/css" rel="stylesheet" />
<script LANGUAGE="JavaScript">
	<!--
	// Nannette Thacker http://www.shiningstar.net
	function confirmCancel()
	{
	    var agree=confirm("Are you sure you want to stop importing rule attribute values?");
	    if (agree) {
	    	   window.location = '${pageContext.request.contextPath}/module/atd/importRuleAttributeValues.form';
	    }
	}
    // -->
</script>
<html>
    <body OnLoad="document.input.formName.focus();">
		<p><h3>Import Rule Attribute Values:</h3></p>
		<form name="input" action="importRuleAttributeValues.form" method="post" enctype="multipart/form-data">
		<table>
		   
		    <tr style="padding: 5px">
		        <td style="padding: 0px 0px 10px 0px">Rule Attribute Value csv file:</td>
		        <td style="padding: 0px 0px 10px 0px">(File headings: rule_name,location_name,location_tag_name,attribute_name,attribute_value)</td>
		        <td style="padding: 0px 0px 10px 0px">
		            <input type="file" name="dataFile" value="${dataFile}">
		        </td>         
		    </tr>
		    <c:if test="${failedFileUpload == 'true'}">
			    <tr style="padding: 5px">
				    <td colspan="3" style="padding: 0px 0px 10px 0px">
				        <font color="red">Error uploading data file.  Check server log for details!</font>
				    </td>
			    </tr>
		    </c:if>
		    <c:if test="${missingFile == 'true'}">
                <tr style="padding: 5px">
                    <td colspan="3" style="padding: 0px 0px 10px 0px">
                        <font color="red">Please specify a file.</font>
                    </td>
                </tr>
            </c:if>
            <c:if test="${incorrectExtension == 'true'}">
                <tr style="padding: 5px">
                    <td colspan="3" style="padding: 0px 0px 10px 0px">
                        <font color="red">Incorrect file extension found.  Only .csv is allowed.</font>
                    </td>
                </tr>
            </c:if>
            
		    <tr style="padding: 5px">
		        <td colspan="3" align="center"><hr size="3" color="black"/></td>
		    </tr>
		    <tr style="padding: 5px">
		       <td align="left">
		           <input type="reset" name="Clear" value="Clear" style="width:70px">
		       </td>
		       <td align="right">
		          <input type="Submit" name="Next" value="Next" style="width:70px">&nbsp;
		           <input type="button" name="Cancel" value="Cancel" onclick="confirmCancel()" style="width:70px">
		       </td>
		    </tr>
		</table>
		</form>
    </body>
</html>
<%@ include file="/WEB-INF/template/footer.jsp"%>