package org.openmrs.module.dss.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.service.DssService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for configFormAttributeValue.form
 * 
 * @author Steve McKee
 */
@Controller
@RequestMapping(value = "module/dss/")
public class RulePrioritizationController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/** Form view */
	private static final String FORM_VIEW = "/module/dss/rulePrioritization";
	
	/** Success form view */
	private static final String SUCCESS_FORM_VIEW = "rulePrioritization.form";
	
	/** Parameter constants */
	private static final String PARAM_RULE_TYPE = "ruleType";
	private static final String PARAM_RULE_TYPES = "ruleTypes";

	@RequestMapping(value = "rulePrioritization.form", method = RequestMethod.POST)
	protected ModelAndView processSubmit(HttpServletRequest request, HttpServletResponse response, Object object) 
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView(new RedirectView(SUCCESS_FORM_VIEW), map);
	}

	@RequestMapping(value = "rulePrioritization.form", method = RequestMethod.GET)
	protected String initForm(HttpServletRequest request, ModelMap map) throws Exception {
		DssService service = Context.getService(DssService.class);
		List<RuleType> ruleTypes = service.getRuleTypes(false);
		map.put(PARAM_RULE_TYPES, ruleTypes);
		
		return FORM_VIEW;
	}
	
	@RequestMapping(value = "getPrioritizedRules", method = RequestMethod.GET)
	@ResponseBody
	public List<Rule> getPrioritizedRules(@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		return dssService.getPrioritizedRules(ruleType);
	}
	
	@RequestMapping(value = "getNonPrioritizedRules", method = RequestMethod.GET)
	@ResponseBody
	public List<Rule> getNonPrioritizedRules(@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		return dssService.getNonPrioritizedRules(ruleType);
	}
}
