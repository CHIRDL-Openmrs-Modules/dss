package org.openmrs.module.dss.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.hibernateBeans.dto.RuleDTO;
import org.openmrs.module.dss.hibernateBeans.dto.RuleEntryDTO;
import org.openmrs.module.dss.service.DssService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		String availableRulesJson = request.getParameter("availableRulesSave");
		String test = request.getParameter("test");
		return new ModelAndView(new RedirectView(SUCCESS_FORM_VIEW), map);
	}

	@RequestMapping(value = "rulePrioritization.form", method = RequestMethod.GET)
	protected String initForm(HttpServletRequest request, ModelMap map) throws Exception {
		DssService service = Context.getService(DssService.class);
		List<RuleType> ruleTypes = service.getRuleTypes(false);
		map.put(PARAM_RULE_TYPES, ruleTypes);
		
		return FORM_VIEW;
	}
	
	@RequestMapping(value = "getPrioritizedRuleEntries", method = RequestMethod.GET)
	@ResponseBody
	public List<RuleEntryDTO> getPrioritizedRules(@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getPrioritizedRuleEntries(ruleType);
		return RuleEntryDTO.convertFrom(ruleEntries);
	}
	
	@RequestMapping(value = "getNonPrioritizedRuleEntries", method = RequestMethod.GET)
	@ResponseBody
	public List<RuleEntryDTO> getNonPrioritizedRuleEntries(@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getNonPrioritizedRuleEntries(ruleType);
		return RuleEntryDTO.convertFrom(ruleEntries);
	}
	
	@RequestMapping(value = "getDisassociatedRules", method = RequestMethod.GET)
	@ResponseBody
	public List<RuleEntryDTO> getDisassociatedRules(@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		List<Rule> rules = dssService.getDisassociatedRules(ruleType);
		List<RuleEntryDTO> ruleEntryDTOs = new ArrayList<>();
		for (Rule rule : rules) {
			RuleEntryDTO ruleEntryDTO = new RuleEntryDTO();
			ruleEntryDTO.setRule(RuleDTO.convertFrom(rule, null));
			ruleEntryDTOs.add(ruleEntryDTO);
		}
		
		return ruleEntryDTOs;
	}
	
	@RequestMapping(value = "saveRules", method = RequestMethod.POST)
	public void saveRules(@RequestParam(value = "availableRulesSave", required = true) String availableRulesSave, 
			@RequestParam(value = "prioritizedRulesSave", required = true) String prioritizedRulesSave, 
			@RequestParam(value = "nonPrioritizedRulesSave", required = true) String nonPrioritizedRulesSave){
		ObjectMapper mapper = new ObjectMapper();
		RuleEntryDTO[] availableRuleDTOs = null;
		RuleEntryDTO[] prioritizedRuleDTOs = null;
		RuleEntryDTO[] nonPrioritizedRuleDTOs = null;
		try {
			availableRuleDTOs = mapper.readValue(availableRulesSave, RuleEntryDTO[].class);
			prioritizedRuleDTOs = mapper.readValue(prioritizedRulesSave, RuleEntryDTO[].class);
			nonPrioritizedRuleDTOs = mapper.readValue(nonPrioritizedRulesSave, RuleEntryDTO[].class);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DssService dssService = Context.getService(DssService.class);
	}
}
