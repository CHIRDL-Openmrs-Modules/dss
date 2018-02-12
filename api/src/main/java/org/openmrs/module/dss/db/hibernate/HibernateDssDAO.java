package org.openmrs.module.dss.db.hibernate;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dss.db.DssDAO;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;

/**
 * Hibernate implementations of Dss related database functions.
 * 
 * @author Tammy Dugan
 */
public class HibernateDssDAO implements DssDAO
{

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;

	/**
	 * Empty constructor
	 */
	public HibernateDssDAO()
	{
	}

	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Rule getRule(int ruleId) throws DAOException
	{
		try 
		{
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Rule.class);
			crit.add(Restrictions.eq("ruleId", ruleId)); 

			return (Rule)crit.uniqueResult();
		} 
		catch (Exception e) 
		{
			log.error("Error retrieving rule " + ruleId, e);
			throw e;
		}
	}

	public Rule getRule(String tokenName) throws DAOException
	{
		try 
		{
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Rule.class);
			crit.add(Restrictions.eq("tokenName", tokenName));
			
			return (Rule)crit.uniqueResult();
		} 
		catch (Exception e) 
		{
			log.error("Error retrieving rule " + tokenName, e);
			throw e;
		}
	}

	public Rule addOrUpdateRule(Rule rule) throws DAOException
	{
		try 
		{
			return (Rule) this.sessionFactory.getCurrentSession().merge(rule);
		} 
		catch (Exception e) 
		{
			log.error("Error adding/updating rule", e);
			throw e;
		}
	}
	
	/**
	 * Looks up a rule attribute by primary key
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttribute(int)
	 */
	public RuleAttribute getRuleAttribute(int ruleAttributeId) throws DAOException {
		try 
		{
			return (RuleAttribute) sessionFactory.getCurrentSession().get(
				RuleAttribute.class, ruleAttributeId);
		} 
		catch (Exception e) 
		{
			log.error("Error retrieving rule attribute " + ruleAttributeId, e);
			throw e;
		}
	}
	
	/**
	 * Looks up a rule attribute by name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttribute(java.lang.String)
	 */
	public RuleAttribute getRuleAttribute(String ruleAttributeName) throws DAOException {
		try 
		{
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttribute.class); 
			crit.add(Restrictions.eq("name", ruleAttributeName)); 

			return (RuleAttribute) crit.uniqueResult();
		} 
		catch (Exception e) 
		{
			log.error("Error retrieving rule attribute " + ruleAttributeName, e);
			throw e;
		}
	}
	
	/**
	 * returns all rule attribute values for a given rule id and rule attribute name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValues(java.lang.Integer, java.lang.String)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			String ruleAttributeName) throws DAOException {
		try {
			RuleAttribute ruleAttribute = this.getRuleAttribute(ruleAttributeName);
			
			if (ruleAttribute != null) {
				Integer ruleAttributeId = ruleAttribute.getRuleAttributeId();
				
				return getRuleAttributeValues(ruleId,ruleAttributeId);
			}
		}
		catch (Exception e) {
			log.error("Error in method getRuleAttributeValues ruleId = " + ruleId + 
				" ruleAttributeName = " + ruleAttributeName, e);
			throw e;
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Returns a list of rule attribute values for a given rule attribute id and value
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributesByValue(java.lang.Integer, java.lang.String)
	 */
	public List<RuleAttributeValue> getRuleAttributesByValue(Integer ruleAttributeId, 
			String value) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttributeValue.class); 
			crit.add(Restrictions.eq("value", value)); 

			return crit.list();
		}
		catch (Exception e) {
			log.error("Error in method getRuleAttributesByValue ruleAttributeId = " + ruleAttributeId + 
				" value = " + value, e);
			throw e;
		}
	}
	
	/**
	 * Returns list of rule attribute values for a given rule id and rule attribute id
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValues(java.lang.Integer, java.lang.Integer)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, 
			Integer ruleAttributeId) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttributeValue.class);
			crit.add(Restrictions.eq("ruleId", ruleId)); 
			crit.add(Restrictions.eq("ruleAttributeId", ruleAttributeId)); 

			return crit.list();
		}
		catch (Exception e) {
			log.error("Error in method getRuleAttributeValues ruleId = " + ruleId + 
				" ruleAttributeId = " + ruleAttributeId, e);
			throw e;
		}
	}
	
	/**
	 * returns the first rule attribute value matched by rule id and rule attribute name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValue(java.lang.Integer, java.lang.String)
	 */
	public RuleAttributeValue getRuleAttributeValue(Integer ruleId, String ruleAttributeName) 
			throws DAOException {
		try {
			List<RuleAttributeValue> list = getRuleAttributeValues(ruleId, ruleAttributeName);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getRuleAttributeValue ruleId = " + ruleId + 
				" ruleAttributeName = " + ruleAttributeName, e);
			throw e;
		}
		
		return null;
	}

	/**
	 * Saves or updates rule attribute value changes to the database
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleAttributeValue(org.openmrs.module.dss.hibernateBeans.RuleAttributeValue)
	 */
	public RuleAttributeValue saveRuleAttributeValue(RuleAttributeValue value) throws DAOException {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(value);
			return value;
		}
		catch (Exception e) {
			log.error("Error in method saveRuleAttributeValue value = " + value, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getPrioritizedRules(java.lang.String, java.lang.Integer)
	 */
    public List<Rule> getPrioritizedRules(String type, Integer startPriority) throws DAOException
    {
    	try
		{
			AdministrationService adminService = Context
					.getAdministrationService();
			String sortOrder = adminService
					.getGlobalProperty("dss.ruleSortOrder");
			if (sortOrder == null)
			{
				sortOrder = "DESC";
			}
			
			String sql = "SELECT *" + 
					"  FROM dss_rule rule" + 
					"       INNER JOIN dss_rule_entry ruleEntry" + 
					"          ON rule.rule_id = ruleEntry.rule_id" + 
					"       INNER JOIN dss_rule_type ruleType" + 
					"          ON ruleEntry.rule_type_id = ruleType.rule_type_id" + 
					" WHERE ruleType.rule_type = ?" + 
					" AND ruleType.voided = false" + 
					" AND ruleEntry.voided = false" + 
					" AND ruleEntry.priority >= ?" + 
					" AND ruleEntry.priority < 1000" + 
					" ORDER BY ruleEntry.priority " + sortOrder;
			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setString(0, type);
			if (startPriority != null) 
			{
				qry.setInteger(1, startPriority);
			} 
			else 
			{
				qry.setInteger(1, 0);
			}
			
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e)
		{
			log.error("Error in method getPrioritizedRules type = " + type + 
				" startPriority = " + startPriority, e);
			throw e;
		}
    }
    
    /**
     * @see org.openmrs.module.dss.db.DssDAO#getNonPrioritizedRules(java.lang.String)
     */
	public List<Rule> getNonPrioritizedRules(String type) throws DAOException
	{
		try
		{
			String sql = "SELECT *" + 
					"  FROM dss_rule rule" + 
					"       INNER JOIN dss_rule_entry ruleEntry" + 
					"          ON rule.rule_id = ruleEntry.rule_id" + 
					"       INNER JOIN dss_rule_type ruleType" + 
					"          ON ruleEntry.rule_type_id = ruleType.rule_type_id" + 
					" WHERE ruleType.rule_type = ?" + 
					" AND ruleType.voided = false" + 
					" AND ruleEntry.voided = false" + 
					" AND ruleEntry.priority is null";

			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setString(0, type);
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e)
		{
			log.error("Error in method getNonPrioritizedRules type = " + type, e);
			throw e;
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRules(org.openmrs.module.dss.hibernateBeans.Rule, 
	 * boolean, boolean, java.lang.String)
	 */
	public List<Rule> getRules(Rule rule, boolean ignoreCase, boolean enableLike, 
			String sortColumn) throws DAOException
	{
		try
		{
			Example example = Example.create(rule);

			if (ignoreCase)
			{
				example = example.ignoreCase();
			}

			if (enableLike)
			{
				example = example.enableLike(MatchMode.ANYWHERE);
			}
			Order order = null;
			AdministrationService adminService = Context
					.getAdministrationService();
			String sortOrder = adminService
					.getGlobalProperty("dss.ruleSortOrder");

			if (sortColumn == null)
			{
				sortColumn = "priority";
			}

			if (sortOrder == null || sortOrder.equalsIgnoreCase("ASC"))
			{
				order = Order.asc(sortColumn);
			} else
			{
				order = Order.desc(sortColumn);
			}

			List<Rule> results = this.sessionFactory.getCurrentSession()
					.createCriteria(Rule.class).add(example).addOrder(order)
					.list();

			return results;
		} catch (Exception e)
		{
			log.error("Error in method getRules rule = " + rule + 
				" ignoreCase = " + ignoreCase + " enableLike = " + enableLike + 
				" sortColumn = " + sortColumn, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRules(java.lang.String)
	 */
	public List<Rule> getRulesByType(String type) throws DAOException {
		try {
			String sql = "SELECT *" + 
					"  FROM dss_rule rule" + 
					"       INNER JOIN dss_rule_entry ruleEntry" + 
					"          ON rule.rule_id = ruleEntry.rule_id" + 
					"       INNER JOIN dss_rule_type ruleType" + 
					"          ON ruleEntry.rule_type_id = ruleType.rule_type_id" + 
					" WHERE ruleType.rule_type = ?" + 
					" AND ruleType.voided = false" + 
					" AND ruleEntry.voided = false";

			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setString(0, type);
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e) {
			log.error("Error in method getRulesByType type = " + type, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleType(org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleType saveRuleType(RuleType ruleType) throws DAOException {
		try {
			return (RuleType) this.sessionFactory.getCurrentSession().merge(ruleType);
		} catch (Exception e) {
			log.error("Error saving rule type " + ruleType, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleType(java.lang.String)
	 */
	public RuleType getRuleType(String type) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleType.class); 
	
			crit.add(Restrictions.eq("ruleType", type)); 
			crit.add(Restrictions.eq("voided", Boolean.FALSE));
	
			return (RuleType)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule type " + type, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleEntry(org.openmrs.module.dss.hibernateBeans.RuleEntry)
	 */
	public RuleEntry saveRuleEntry(RuleEntry ruleEntry) throws DAOException {
		try {
			return (RuleEntry) this.sessionFactory.getCurrentSession().merge(ruleEntry);
		} catch (Exception e) {
			log.error("Error saving rule entry " + ruleEntry, e);
			throw e;
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleEntry(org.openmrs.module.dss.hibernateBeans.Rule, org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleEntry getRuleEntry(Rule rule, RuleType ruleType) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.add(Restrictions.eq("rule", rule)).add(Restrictions.eq("ruleType", ruleType))
					.add(Restrictions.eq("voided", Boolean.FALSE));
			return (RuleEntry)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule entry rule = " + rule + " ruleType = " + ruleType, e);
			throw e;
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleEntry(java.lang.Integer, java.lang.String)
	 */
	public RuleEntry getRuleEntry(Integer ruleId, String ruleType) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.createAlias("rule", "rule")
					.createAlias("ruleType", "ruleType")
					.add(Restrictions.eq("rule.ruleId", ruleId))
					.add(Restrictions.eq("ruleType.ruleType", ruleType))
					.add(Restrictions.eq("voided", Boolean.FALSE));
			return (RuleEntry)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule entry ruleId = " + ruleId + " ruleType = " + ruleType, e);
			throw e;
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleReferences(org.openmrs.module.dss.hibernateBeans.Rule)
	 */
	public List<RuleEntry> getRuleReferences(Rule rule) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.add(Restrictions.eq("rule", rule)).add(Restrictions.eq("voided", Boolean.FALSE));
			return crit.list();
		} catch (Exception e) {
			log.error("Error retrieving rule references rule = " + rule, e);
			throw e;
		}
	}
}
