/**
 * 
 */
package org.openmrs.module.dss.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /** Log */
	private static final Logger log = LoggerFactory.getLogger(RuleTesterController.class);
	
	/** Form view */
    private static final String FORM_VIEW = "/module/dss/ruleTester";
    
    /** Parameters */
    private static final String PARAMETER_RULE_NAME = "ruleName";
    private static final String PARAMETER_LAST_MRN = "lastMRN";
    private static final String PARAMETER_LAST_RULE_NAME = "lastRuleName";
    private static final String PARAMETER_RUN_RESULT = "runResult";
    private static final String PARAMETER_TOKEN_NAME = "tokenName";
    private static final String PARAMETER_RULES = "rules";

	@RequestMapping(method = RequestMethod.GET)
    protected String initForm(HttpServletRequest request, ModelMap map)
	{
		DssService dssService = Context.getService(DssService.class);

		String ruleName = request.getParameter(PARAMETER_RULE_NAME);
		String mrn = request.getParameter(ChirdlUtilConstants.PARAMETER_MRN);

		map.put(PARAMETER_LAST_MRN, mrn);
		map.put(PARAMETER_LAST_RULE_NAME, ruleName);

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
					parameters.put(ChirdlUtilConstants.PARAMETER_MODE, ChirdlUtilConstants.PARAMETER_VALUE_PRODUCE);
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
                        map.put(PARAMETER_RUN_RESULT, resultString.toString());
					}
				}
			} catch (Exception e)
			{
				log.error(e.getMessage());
				log.error(org.openmrs.module.chirdlutil.util.Util.getStackTrace(e));
			}
		}

		List<Rule> rules = dssService.getRules(new Rule(), true, true, PARAMETER_TOKEN_NAME);

		map.put(PARAMETER_RULES, rules);

		return FORM_VIEW;
	}

}
