package org.openmrs.module.dss.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

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
	
	/** Parameter constants */
	private static final String PARAM_RULE_TYPES = "ruleTypes";

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
	@ResponseBody
	public String saveRules(@RequestParam(value = "availableRulesSave", required = true) String availableRulesSave, 
			@RequestParam(value = "prioritizedRulesSave", required = true) String prioritizedRulesSave, 
			@RequestParam(value = "nonPrioritizedRulesSave", required = true) String nonPrioritizedRulesSave,
			@RequestParam(value = "ruleType", required = true) String ruleType){
		DssService dssService = Context.getService(DssService.class);
		ObjectMapper mapper = new ObjectMapper();
		RuleEntryDTO[] availableRuleDTOs = null;
		RuleEntryDTO[] prioritizedRuleDTOs = null;
		RuleEntryDTO[] nonPrioritizedRuleDTOs = null;
		try {
			availableRuleDTOs = mapper.readValue(availableRulesSave, RuleEntryDTO[].class);
			prioritizedRuleDTOs = mapper.readValue(prioritizedRulesSave, RuleEntryDTO[].class);
			nonPrioritizedRuleDTOs = mapper.readValue(nonPrioritizedRulesSave, RuleEntryDTO[].class);
			
			reconcileAvailableRules(availableRuleDTOs, ruleType, dssService);
			reconcileNonPrioritizedRules(nonPrioritizedRuleDTOs, ruleType, dssService);
			reconcilePrioritizedRules(prioritizedRuleDTOs, ruleType, dssService);
		}
		catch (IOException e) {
			log.error("Error updating rule entries.", e);
			return "error";
		}
		
		return "success";
	}
	
	/**
	 * Retire any rule entries that are no longer associated with the provided rule type.
	 * 
	 * @param ruleEntryDTOs List of RuleEntryDTO objects from the client
	 * @param ruleType The rule type from the client
	 * @param dssService The service used to access and save rule entry information
	 */
	private void reconcileAvailableRules(RuleEntryDTO[] ruleEntryDTOs, String ruleType, DssService dssService) {
		List<Rule> rules = dssService.getDisassociatedRules(ruleType);
		
		// Populate a map of available rules from the client
		Map<Integer, RuleEntryDTO> availableRulesMap = new HashMap<>();
		for (RuleEntryDTO ruleEntryDTO : ruleEntryDTOs) {
			availableRulesMap.put(ruleEntryDTO.getRule().getRuleId(), ruleEntryDTO);
		}
		
		// Remove the current rules from the map
		for (Rule rule : rules) {
			availableRulesMap.remove(rule.getRuleId());
		}
		
		// What's left in the map needs to be retired
		Iterator<Entry<Integer, RuleEntryDTO>> availableRuleIter = availableRulesMap.entrySet().iterator();
		while (availableRuleIter.hasNext()) {
			Entry<Integer, RuleEntryDTO> entry = availableRuleIter.next();
			Integer ruleId = entry.getKey();
			RuleEntry currentRuleEntry = dssService.getRuleEntry(ruleId, ruleType);
			if (currentRuleEntry != null) {
				dssService.retireRuleEntry(currentRuleEntry, "Entry no longer associated with rule type: " + ruleType + ".");
			}
		}
	}
	
	/**
	 * Add new rule entries for a rule and rule type that are not prioritized.
	 * 
	 * @param ruleEntryDTOs List of RuleEntryDTO objects from the client
	 * @param ruleTypeName The rule type name from the client
	 * @param dssService The service used to access and save rule entry information
	 */
	private void reconcileNonPrioritizedRules(RuleEntryDTO[] ruleEntryDTOs, String ruleTypeName, DssService dssService) {
		List<RuleEntry> rules = dssService.getNonPrioritizedRuleEntries(ruleTypeName);
		
		// Populate a map of non-prioritized rules from the client
		Map<Integer, RuleEntryDTO> nonPrioritizedRulesMap = new HashMap<>();
		for (RuleEntryDTO ruleEntryDTO : ruleEntryDTOs) {
			nonPrioritizedRulesMap.put(ruleEntryDTO.getRule().getRuleId(), ruleEntryDTO);
		}
		
		// Remove the current rules from the map
		for (RuleEntry ruleEntry : rules) {
			nonPrioritizedRulesMap.remove(ruleEntry.getRule().getRuleId());
		}
		
		// Retrieve the rule type for possible use
		RuleType ruleType = dssService.getRuleType(ruleTypeName);
		
		// What's left in the map was either a rule never associated to the rule type or used to be a prioritized rule
		Iterator<Entry<Integer, RuleEntryDTO>> nonPrioritizedRuleIter = nonPrioritizedRulesMap.entrySet().iterator();
		while (nonPrioritizedRuleIter.hasNext()) {
			Entry<Integer, RuleEntryDTO> entry = nonPrioritizedRuleIter.next();
			Integer ruleId = entry.getKey();
			RuleEntry currentRuleEntry = dssService.getRuleEntry(ruleId, ruleTypeName);
			if (currentRuleEntry != null) {
				// We found a rule entry, so this means it used to be a prioritized rule
				// Retire the current rule entry and create a new one
				dssService.retireRuleEntry(currentRuleEntry, "Entry moved from prioritized to non-prioritized: " + 
						ruleType + ".");
				RuleEntry newRuleEntry = copyRuleEntry(currentRuleEntry);
				dssService.saveRuleEntry(newRuleEntry);
			} else {
				// We didn't find a rule entry, so this means it's not currently associated to the rule type
				RuleEntry newRuleEntry = createNewRuleEntry(ruleId, ruleType, dssService);
				dssService.saveRuleEntry(newRuleEntry);
			}
		}
	}
	
	/**
	 * Add new rule entries for a rule and rule type that are prioritized.
	 * 
	 * @param ruleEntryDTOs List of RuleEntryDTO objects from the client
	 * @param ruleTypeName The rule type name from the client
	 * @param dssService The service used to access and save rule entry information
	 */
	private void reconcilePrioritizedRules(RuleEntryDTO[] ruleEntryDTOs, String ruleTypeName, DssService dssService) {
		List<RuleEntry> ruleEntries = dssService.getPrioritizedRuleEntries(ruleTypeName);
		ruleEntries.addAll(dssService.getPrioritizedRuleEntries(ruleTypeName));
		
		// Populate a map of current prioritized and non-prioritized rules
		Map<Integer, RuleEntry> rulesMap = new HashMap<>();
		for (RuleEntry ruleEntry : ruleEntries) {
			rulesMap.put(ruleEntry.getRule().getRuleId(), ruleEntry);
		}
		
		// Retrieve the rule type for possible use
		RuleType ruleType = dssService.getRuleType(ruleTypeName);
		
		// Loop through and update any priorities or create new entries
		for (int i = 0; i < ruleEntryDTOs.length; i++) {
			RuleEntryDTO ruleEntryDTO = ruleEntryDTOs[i];
			
			// Set the new priority.  Add one since we want to start priorities at one but i starts at 0.
			Integer newPriority = i + 1;
			
			// See if the rule entry already exists
			Integer ruleId = ruleEntryDTO.getRule().getRuleId();
			RuleEntry existingRuleEntry = rulesMap.get(ruleId);
			if (existingRuleEntry == null) {
				// Create a new rule entry since one was not found
				RuleEntry newRuleEntry = createNewRuleEntry(ruleId, ruleType, dssService);
				newRuleEntry.setPriority(newPriority);
				dssService.saveRuleEntry(newRuleEntry);
			} else if (existingRuleEntry.getPriority() == null || existingRuleEntry.getPriority() != newPriority) {
				// Retire the existing rule entry and create a new one
				RuleEntry newRuleEntry = copyRuleEntry(existingRuleEntry);
				newRuleEntry.setPriority(newPriority);
				dssService.retireRuleEntry(existingRuleEntry, "Priority changed from " + existingRuleEntry.getPriority() + 
					" to " + newPriority + ".");
				dssService.saveRuleEntry(newRuleEntry);
			}
		}
	}
	
	/**
	 * Copy an existing rule entry without any key identifiers or retire information.
	 * 
	 * @param ruleEntryToCopy The RuleEntry object to copy
	 * @return A new RuleEntry object
	 */
	private RuleEntry copyRuleEntry(RuleEntry ruleEntryToCopy) {
		RuleEntry ruleEntry = new RuleEntry();
		ruleEntry.setRule(ruleEntryToCopy.getRule());
		ruleEntry.setRuleType(ruleEntryToCopy.getRuleType());
		
		return ruleEntry;
	}
	
	/**
	 * Creates a new rule entry.
	 * 
	 * @param ruleId The ID of the rule to associate the entry
	 * @param ruleType The rule type to associate the entry
	 * @param dssService The service used to access rule and rule type information
	 * @return A new RuleEntry object
	 */
	private RuleEntry createNewRuleEntry(Integer ruleId, RuleType ruleType, DssService dssService) {
		RuleEntry ruleEntry = new RuleEntry();
		Rule rule = dssService.getRule(ruleId);
		ruleEntry.setRule(rule);
		ruleEntry.setRuleType(ruleType);
		
		return ruleEntry;
	}
}
