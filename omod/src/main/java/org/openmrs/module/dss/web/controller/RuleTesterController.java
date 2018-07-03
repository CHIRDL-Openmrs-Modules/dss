/**
 * 
 */
package org.openmrs.module.dss.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.logic.result.Result;
import org.openmrs.module.chirdlutil.util.ChirdlUtilConstants;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.service.DssService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author tmdugan
 * 
 */
@Controller
@RequestMapping(value = "module/dss/ruleTester.form")
public class RuleTesterController
{

	protected final Log log = LogFactory.getLog(getClass());
	
	/** Form view */
    private static final String FORM_VIEW = "/module/dss/ruleTester";

	@RequestMapping(method = RequestMethod.GET)
    protected String initForm(HttpServletRequest request, ModelMap map)
	{
		DssService dssService = Context.getService(DssService.class);

		String mode = "PRODUCE";

		String ruleName = request.getParameter("ruleName");
		String mrn = request.getParameter("mrn");

		map.put("lastMRN", mrn);
		map.put("lastRuleName", ruleName);

		if (mrn != null)
		{
			try
			{
				PatientService patientService = Context.getPatientService();
				
				PatientIdentifierType identifierType = 
				        patientService.getPatientIdentifierTypeByName(ChirdlUtilConstants.IDENTIFIER_TYPE_MRN);
				List<PatientIdentifierType> identifierTypes = new ArrayList<>();
				identifierTypes.add(identifierType);
				// CHICA-1151 getPatientsByIdentifier(String, boolean) has been removed using the fix from CHICA-977 Use 
				// getPatientsByIdentifier() as a temporary solution to openmrs TRUNK-5089
				List<Patient> patients = patientService.getPatientsByIdentifier(null, mrn, identifierTypes, false); 

				if (!patients.isEmpty())
				{
				    Patient patient = patients.get(0);
					HashMap<String, Object> parameters = new HashMap<>();
					parameters.put("mode", mode);
					Rule rule = new Rule();
					rule.setTokenName(ruleName);
					Rule currRule = dssService.getRule(ruleName);
					if (currRule != null && currRule.checkAgeRestrictions(patient))
					{
						currRule.setParameters(parameters);
						Result result = dssService.runRule(patient, currRule);
						StringBuilder resultString = new StringBuilder();
                        for (Result currResult : result)
                        {
                            resultString.append(currResult.toString());
                            resultString.append("<br/><br/>");
                        }
                        map.put("runResult", resultString.toString());
					}
				}
			} catch (Exception e)
			{
				this.log.error(e.getMessage());
				this.log.error(org.openmrs.module.chirdlutil.util.Util.getStackTrace(e));
			}
		}

		List<Rule> rules = dssService.getRules(new Rule(), true, true, "tokenName");

		map.put("rules", rules);

		return FORM_VIEW;
	}

}
