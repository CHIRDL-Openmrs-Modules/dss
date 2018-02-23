package org.openmrs.module.dss.service;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.logic.result.Result;
import org.openmrs.module.dss.DssRule;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;

/**
 * Defines services used by this module
 *  
 * @author Tammy Dugan
 *
 */
public interface DssService
{
	/**
	 * Runs a list of rules and returns a string result
	 * 
	 * @param p Patient to run the rules for
	 * @param ruleList List of rules to run
	 * @return String result as a string
	 * @throws APIException
	 */
	public String runRulesAsString(Patient p, List<Rule> ruleList) throws APIException;

	/**
	 * Runs a single rule and returns the result as an openmrs Result object
	 * 
	 * @param p Patient to run the rules for
	 * @param rule single rule to evaluate
	 * @return String result as an openmrs Result object
	 * @throws APIException
	 */
	public Result runRule(Patient p, Rule rule) throws APIException;

	/**
	 * Runs a list of rules and returns an arraylist of openmrs Result objects
	 * 
	 * @param p Patient to run the rules for
	 * @param ruleList list of rules to evaluate
	 * @return ArrayList of openmrs Result objects
	 * @throws APIException
	 */
	public ArrayList<Result> runRules(Patient p, List<Rule> ruleList) throws APIException;

	/**
	 * Looks up a rule from the dss_rule table by rule_id
	 * 
	 * @param ruleId unique id for a rule in the dss_rule table
	 * @return Rule rule from the dss_rule table
	 * @throws APIException
	 */
	public Rule getRule(int ruleId) throws APIException;

	/**
	 * Looks up a rule from the dss_rule table by token name
	 * 
	 * @param tokenName name that is used to register a rule with the openmrs LogicService
	 * @return Rule from the dss_rule table
	 * @throws APIException
	 */
	public Rule getRule(String tokenName) throws APIException;
	
	/**
	 * Adds a new rule to the dss_rule table
	 * 
	 * @param classFilename name of the compiled class file that contains the 
	 * executable rule
	 * @param rule DssRule to save to the dss_rule table
	 * @return Rule rule that was added to the dss_rule table
	 * @throws APIException
	 */
	public Rule addRule(String classFilename, DssRule rule) throws APIException;

	/**
	 * Get prioritized rules based on type and start priority
	 * 
	 * @param type Only rules with this rule type will be returned.
	 * @param startPriority Only rules with a priority equal to or greater than this will be returned.
	 * @return
	 * @throws APIException
	 */
	public List<Rule> getPrioritizedRules(String type) throws APIException;
	
	/**
	 * Get non prioritized rules based on type.
	 * 
	 * @param type Only rules with this rule type will be returned.
	 * @return List of Rule objects with the provided type.
	 * @throws APIException
	 */
	public List<Rule> getNonPrioritizedRules(String type) throws APIException;
		
	/**
	 * Returns a list of rules from the dss_rule table that match the criteria
	 * assigned to the rule parameter
	 * 
	 * @param rule Rule whose assigned attributes indicate the restrictions
	 * of the dss_rule table query
	 * @param ignoreCase String attributes assigned in the Rule parameter should
	 * be matched in the dss_rule query regardless of case
	 * @param enableLike String attributes assigned in the Rule parameter should
	 * be matched in the dss_rule query using LIKE instead of exact matching
	 * @param sortColumn The column name for which to sort the results (optional)
	 * @return List<Rule>
	 * @throws APIException
	 */
	public List<Rule> getRules(Rule rule, boolean ignoreCase, boolean enableLike, 
			String sortColumn) throws APIException;
	
	/**
	 * Returns a list of rules based on rule type.
	 * 
	 * @param type The rule type to search.
	 * @return List of Rule objects containing the provided rule type.
	 * @throws APIException
	 */
	public List<Rule> getRulesByType(String type) throws APIException;

	/**
	 * Loads a rule into the openmrs LogicService in preparation for executing it
	 * 
	 * @param rule name that the rule will be stored under in the openmrs LogicService
	 * @return Rule object
	 * @throws APIException
	 */
	public org.openmrs.logic.Rule loadRule(String rule, boolean updateRule) throws APIException;
	
	/**
	 * Get prioritized rules based on type and start priority
	 * 
	 * @param type Only rules with this rule type will be returned.
	 * @param startPriority Only rules with a priority equal to or greater than this will be returned.
	 * @return List of Rule objects with the provided type and that start at the prvoided priority 
	 * (or 0 if not stated).
	 * @throws APIException
	 */
	public List<Rule> getPrioritizedRules(String type, Integer startPriority) throws APIException;

	/**
	 * 
	 * Looks up a rule attribute by name
	 * 
	 * @param ruleAttributeName
	 * @return
	 * @throws APIException
	 */
	public RuleAttribute getRuleAttribute(String ruleAttributeName)  throws APIException;
	
	/**
	 * 
	 * Looks up a rule attribute by primary key
	 * 
	 * @param ruleAttributeId
	 * @return
	 * @throws APIException
	 */
	public RuleAttribute getRuleAttribute(Integer ruleAttributeId) throws APIException;
	
	/**
	 * 
	 * returns the first rule attribute value matched by rule id and rule attribute name
	 * 
	 * @param ruleId
	 * @param ruleAttributeName
	 * @return
	 * @throws APIException
	 */
	public RuleAttributeValue getRuleAttributeValue(Integer ruleId, 
			String ruleAttributeName) throws APIException;
	
	/**
	 * 
	 * returns all rule attribute values for a given rule id and rule attribute name
	 * 
	 * @param ruleId
	 * @param ruleAttributeName
	 * @return
	 * @throws APIException
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			String ruleAttributeName) throws APIException;
	
	/**
	 * 
	 * Returns list of rule attribute values for a given rule id and rule attribute id
	 * 
	 * @param ruleId
	 * @param ruleAttributeId
	 * @return
	 * @throws APIException
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			Integer ruleAttributeId) throws APIException;
	
	/**
	 * 
	 * Saves or updates rule attribute value changes to the database
	 * 
	 * @param value
	 * @return
	 * @throws APIException
	 */
	public RuleAttributeValue saveRuleAttributeValue(RuleAttributeValue value) throws APIException;
	
	/**
	 * Returns a list of rule attribute values for a given rule attribute id and value
	 * 
	 * @param ruleAttributeId
	 * @param value
	 * @return
	 * @throws APIException
	 */
	public List<RuleAttributeValue> getRuleAttributesByValue(Integer ruleAttributeId, 
			String value) throws APIException;
	
	/**
	 * Adds or updates a rule type.
	 * 
	 * @param ruleType The rule type to add or update.
	 * @return The added or updated rule type.
	 * @throws APIException
	 */
	public RuleType saveRuleType(RuleType ruleType) throws APIException;
	
	/**
	 * Retires a rule type.
	 * 
	 * @param ruleType The rule type to retire.
	 * @param reason The reason for retiring the rule type.
	 * @throws APIException
	 */
	public void retireRuleType(RuleType ruleType, String reason) throws APIException;
	
	/**
	 * Returns a rule type based on the type provided.
	 * 
	 * @param type The rule type
	 * @return RuleType object matching the provided type of null if one can't be found
	 * @throws APIException
	 */
	public RuleType getRuleType(String type) throws APIException;
	
	/**
	 * Adds or updates a rule entry.
	 * 
	 * @param ruleEntry The rule entry to add or update.
	 * @return The added or updated rule entry.
	 * @throws APIException
	 */
	public RuleEntry saveRuleEntry(RuleEntry ruleEntry) throws APIException;
	
	/**
	 * Retires a rule entry.
	 * 
	 * @param ruleEntry The rule entry to retire.
	 * @param reason The reason for retiring the rule entry.
	 * @throws APIException
	 */
	public void retireRuleEntry(RuleEntry ruleEntry, String reason) throws APIException;
	
	/**
	 * Returns a rule entry based on rule and rule type.
	 * 
	 * @param rule The rule used to retrieve the rule entry.
	 * @param ruleType The rule type used to retrieve the rule entry.
	 * @return RuleEntry object or null if one cannot be found.
	 * @throws APIException
	 */
	public RuleEntry getRuleEntry(Rule rule, RuleType ruleType) throws APIException;
	
	/**
	 * Returns a rule entry based on rule ID and rule type.
	 * 
	 * @param ruleId The rule ID used to retrieve the rule entry.
	 * @param ruleType The rule type used to retrieve the rule entry.
	 * @return RuleEntry object or null if one cannot be found.
	 * @throws APIException
	 */
	public RuleEntry getRuleEntry(Integer ruleId, String ruleType) throws APIException;
	
	/**
	 * Returns all rule entries referencing the provided rule.
	 * 
	 * @param rule The rule used to find the rule entry references.
	 * @return List of RuleEntry objects referencing the provided rule.
	 * @throws APIException
	 */
	public List<RuleEntry> getRuleReferences(Rule rule) throws APIException;
}