package org.openmrs.module.dss.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutil.util.ChirdlUtilConstants;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.service.DssService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "module/dss/searchRules.form")
public class SearchRulesController {
    
    /** Logger for this class and subclasses */
	private static final Logger log = LoggerFactory.getLogger(SearchRulesController.class);
    
    /** Form view */
    private static final String FORM_VIEW = "/module/dss/searchRules";
    
    /** Parameters */
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_RULES = "rules";
    private static final String PARAMETER_RUN_SEARCH = "runSearch";
    private static final String PARAMETER_CLASS_FILENAME = "classFilename";
    private static final String PARAMETER_INSTITUTION = "institution";
    private static final String PARAMETER_SPECIALIST = "specialist";
    private static final String PARAMETER_PURPOSE = "purpose";
    private static final String PARAMETER_EXPLANATION = "explanation";
    private static final String PARAMETER_CITATIONS = "citations";
    private static final String PARAMETER_LINKS = "links";
    private static final String PARAMETER_DATA = "data";
    private static final String PARAMETER_LOGIC = "logic";
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_KEYWORDS = "keywords";
    private static final String PARAMETER_AUTHOR = "author";
    
    /**
     * Form initialization method.
     * 
     * @param request The HTTP request information
     * @param map The map to populate for return to the client
     * @return The form view name
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String initForm(HttpServletRequest request, ModelMap map) {
        Rule rule = new Rule();
        if (StringUtils.isNotBlank(request.getParameter(PARAMETER_RUN_SEARCH))) {
            String[] parameters = new String[] { PARAMETER_TITLE, PARAMETER_AUTHOR, PARAMETER_KEYWORDS, PARAMETER_ACTION, 
                    PARAMETER_LOGIC, PARAMETER_DATA, PARAMETER_LINKS, PARAMETER_CITATIONS, PARAMETER_EXPLANATION, 
                    PARAMETER_PURPOSE, PARAMETER_SPECIALIST, PARAMETER_INSTITUTION, PARAMETER_CLASS_FILENAME };
            for (String parameter : parameters) {
                populateParameter(request, rule, parameter, map);
            }
            
            DssService dssService = Context.getService(DssService.class);
            List<Rule> rules = dssService.getRules(rule, true, true, null);
            map.put(PARAMETER_RULES, rules);
            
            map.put(PARAMETER_RUN_SEARCH, Boolean.TRUE);
        } else {
            map.put(PARAMETER_RUN_SEARCH, Boolean.FALSE);
        }
        
        return FORM_VIEW;
    }
    
    /**
     * Populates the provided Rule and ModelMap with the parameter from the HTTP request.
     * 
     * @param request The HTTP request containing the parameter value
     * @param rule The rule to populate wit the parameter value
     * @param parameterName The name of the parameter
     * @param map The map to populate with the parameter value to send back to the client
     */
    private void populateParameter(HttpServletRequest request, Rule rule, String parameterName, ModelMap map) {
        String value = request.getParameter(parameterName);
        String ruleValue = value;
        String mapValue = value;
        if (StringUtils.isBlank(value)) {
            ruleValue = null;
            mapValue = ChirdlUtilConstants.GENERAL_INFO_EMPTY_STRING;
        }
        
        map.put(parameterName, mapValue);
        
        switch (parameterName) {
            case PARAMETER_TITLE:
                rule.setTitle(ruleValue);
                break;
            case PARAMETER_AUTHOR:
                rule.setAuthor(ruleValue);
                break;
            case PARAMETER_KEYWORDS:
                rule.setKeywords(ruleValue);
                break;
            case PARAMETER_ACTION:
                rule.setAction(ruleValue);
                break;
            case PARAMETER_LOGIC:
                rule.setLogic(ruleValue);
                break;
            case PARAMETER_DATA:
                rule.setData(ruleValue);
                break;
            case PARAMETER_LINKS:
                rule.setLinks(ruleValue);
                break;
            case PARAMETER_CITATIONS:
                rule.setCitations(ruleValue);
                break;
            case PARAMETER_EXPLANATION:
                rule.setExplanation(ruleValue);
                break;
            case PARAMETER_PURPOSE:
                rule.setPurpose(ruleValue);
                break;
            case PARAMETER_SPECIALIST:
                rule.setSpecialist(ruleValue);
                break;
            case PARAMETER_INSTITUTION:
                rule.setInstitution(ruleValue);
                break;
            case PARAMETER_CLASS_FILENAME:
                rule.setClassFilename(ruleValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid parameter: " + parameterName);
        }
    }
}
