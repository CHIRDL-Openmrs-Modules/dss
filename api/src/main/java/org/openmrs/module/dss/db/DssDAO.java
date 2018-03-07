package org.openmrs.module.dss.db;

import java.util.List;

import org.openmrs.api.db.DAOException;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dss related database functions
 * 
 * @author Tammy Dugan
 */
@Transactional
public interface DssDAO {

	/**
	 * Looks up a rule from the dss_rule table by rule_id
	 * @param ruleId unique identifier for rule in the dss_rule table
	 * @return Rule from the dss_rule table
	 * @throws DAOException
	 */
	public Rule getRule(int ruleId) throws DAOException;
	
	/**
	 * Looks up a rule from the dss_rule table by token name
	 * @param tokenName name that is used to register a rule with the openmrs LogicService
	 * @return Rule from the dss_rule table
	 * @throws DAOException
	 */
	public Rule getRule(String tokenName) throws DAOException;
	
	/**
	 * Get non-prioritized rules based on type.
	 * 
	 * @param type Only rules with this rule type will be returned.
	 * @return List of non-prioritized Rule objects with the provided type.
	 * @throws DAOException
	 */
	public List<Rule> getNonPrioritizedRules(String type) throws DAOException;
		
	/**
	 * Returns a list of rules from the dss_rule table that match the criteria
	 * assigned to the rule parameter
	 * @param rule Rule whose assigned attributes indicate the restrictions
	 * of the dss_rule table query
	 * @param ignoreCase String attributes assigned in the Rule parameter should
	 * be matched in the dss_rule query regardless of case
	 * @param enableLike String attributes assigned in the Rule parameter should
	 * be matched in the dss_rule query using LIKE instead of exact matching
	 * @param sortColumn The column name used to sort the results (optional)
	 * @return List<Rule>
	 * @throws DAOException
	 */
	public List<Rule> getRules(Rule rule,boolean ignoreCase, boolean enableLike, 
			String sortColumn) throws DAOException;
	
	/**
	 * Returns a list of rules based on rule type.
	 * 
	 * @param type The rule type to search.
	 * @return List of Rule objects containing the provided rule type.
	 * @throws DAOException
	 */
	public List<Rule> getRulesByType(String type) throws DAOException;
	
	/**
	 * Adds a new rule to the dss_rule table
	 * @param rule new rule to add to the dss_rule table
	 * @return Rule added to the dss_rule table
	 * @throws DAOException
	 */
	public Rule addOrUpdateRule(Rule rule) throws DAOException;
	
	/**
	 * Get prioritized rules based on type and start priority
	 * 
	 * @param type Only rules with this rule type will be returned.
	 * @param startPriority Only rules with a priority equal to or greater than this will be returned.
	 * @return
	 * @throws DAOException
	 */
	public List<Rule> getPrioritizedRules(String type, Integer startPriority) throws DAOException;

	/**
	 * 
	 * Looks up a rule attribute by name
	 * 
	 * @param ruleAttributeName
	 * @return
	 * @throws DAOException
	 */
	public RuleAttribute getRuleAttribute(String ruleAttributeName) throws DAOException;

	/**
	 * 
	 * Looks up a rule attribute by primary key
	 * 
	 * @param ruleAttributeId
	 * @return
	 * @throws DAOException
	 */
	public RuleAttribute getRuleAttribute(int ruleAttributeId) throws DAOException;
	
	/**
	 * 
	 * returns the first rule attribute value matched by rule id and rule attribute name
	 * 
	 * @param ruleId
	 * @param ruleAttributeName
	 * @return
	 * @throws DAOException
	 */
	public RuleAttributeValue getRuleAttributeValue(Integer ruleId, 
			String ruleAttributeName) throws DAOException; 
	
	/**
	 * 
	 * returns all rule attribute values for a given rule id and rule attribute name
	 * 
	 * @param ruleId
	 * @param ruleAttributeName
	 * @return
	 * @throws DAOException
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			String ruleAttributeName) throws DAOException; 
	
	/**
	 * 
	 * Returns list of rule attribute values for a given rule id and rule attribute id
	 * 
	 * @param ruleId
	 * @param ruleAttributeId
	 * @return
	 * @throws DAOException
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			Integer ruleAttributeId) throws DAOException;
	
	/**
	 * 
	 * Saves or updates rule attribute value changes to the database
	 * 
	 * @param value
	 * @return
	 * @throws DAOException
	 */
	public RuleAttributeValue saveRuleAttributeValue(RuleAttributeValue value) throws DAOException;
	
	/**
	 * Returns a list of rule attribute values for a given rule attribute id and value
	 * 
	 * @param ruleAttributeId
	 * @param value
	 * @return
	 * @throws DAOException
	 */
	public List<RuleAttributeValue> getRuleAttributesByValue(Integer ruleAttributeId, 
			String value) throws DAOException;
	
	/**
	 * Adds or updates a rule type.
	 * 
	 * @param ruleType The rule type to add or update.
	 * @return The added or updated rule type.
	 * @throws DAOException
	 */
	public RuleType saveRuleType(RuleType ruleType) throws DAOException;
	
	/**
	 * Returns a rule type based on the type provided.
	 * 
	 * @param type The rule type
	 * @return RuleType object matching the provided type of null if one can't be found
	 * @throws DAOException
	 */
	public RuleType getRuleType(String type) throws DAOException;
	
	/**
	 * Returns a list of rule types.
	 * 
	 * @param includeRetired whether or not to include retired rule types in the list
	 * @return List of RuleType objects
	 * @throws DAOException
	 */
	public List<RuleType> getRuleTypes(boolean includeRetired) throws DAOException;
	
	/**
	 * Adds or updates a rule entry.
	 * 
	 * @param ruleEntry The rule entry to add or update.
	 * @return The added or updated rule entry.
	 * @throws DAOException
	 */
	public RuleEntry saveRuleEntry(RuleEntry ruleEntry) throws DAOException;
	
	/**
	 * Returns a rule entry based on rule and rule type.
	 * 
	 * @param rule The rule used to retrieve the rule entry.
	 * @param ruleType The rule type used to retrieve the rule entry.
	 * @return RuleEntry object or null if one cannot be found.
	 * @throws DAOException
	 */
	public RuleEntry getRuleEntry(Rule rule, RuleType ruleType) throws DAOException;
	
	/**
	 * Returns a rule entry based on rule ID and rule type.
	 * 
	 * @param ruleId The rule ID used to retrieve the rule entry.
	 * @param ruleType The rule type used to retrieve the rule entry.
	 * @return RuleEntry object or null if one cannot be found.
	 * @throws DAOException
	 */
	public RuleEntry getRuleEntry(Integer ruleId, String ruleType) throws DAOException;
	
	/**
	 * Returns all rule entries referencing the provided rule.
	 * 
	 * @param rule The rule used to find the rule entry references.
	 * @return List of RuleEntry objects referencing the provided rule.
	 * @throws DAOException
	 */
	public List<RuleEntry> getRuleReferences(Rule rule) throws DAOException;
}
