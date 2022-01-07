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

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRule(int)
	 */
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
			throw new DAOException(e);
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRule(java.lang.String)
	 */
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
			throw new DAOException(e);
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#addOrUpdateRule(org.openmrs.module.dss.hibernateBeans.Rule)
	 */
	public Rule addOrUpdateRule(Rule rule) throws DAOException
	{
		try 
		{
			return (Rule) this.sessionFactory.getCurrentSession().merge(rule);
		} 
		catch (Exception e) 
		{
			log.error("Error adding/updating rule", e);
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
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
			throw new DAOException(e);
		}
	}
    
    /**
     * @see org.openmrs.module.dss.db.DssDAO#getPrioritizedRuleEntries(java.lang.String, java.lang.Integer)
     */
	public List<RuleEntry> getPrioritizedRuleEntries(String ruleType, Integer startPriority) throws DAOException {
		try {
			AdministrationService adminService = Context.getAdministrationService();
			String sortOrder = adminService.getGlobalProperty("dss.ruleSortOrder");
			boolean sortAsc = "ASC".equalsIgnoreCase(sortOrder);
			Order order = Order.desc("entry.priority");
			if (sortAsc) {
				order = Order.asc("entry.priority");
			}
			
			if (startPriority == null) {
				startPriority = 0;
			}
			
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.createAlias("rule", "rule")
					.createAlias("ruleType", "ruleType")
					.add(Restrictions.eq("ruleType.name", ruleType))
					.add(Restrictions.eq("entry.retired", Boolean.FALSE))
					.add(Restrictions.eq("ruleType.retired", Boolean.FALSE))
					.add(Restrictions.ge("entry.priority", startPriority))
					.add(Restrictions.lt("entry.priority", RuleEntry.RULE_PRIORITY_RETIRE))
					.addOrder(order);
			return crit.list();
		} catch (Exception e) {
			log.error("Error retrieving prioritized rule entries for ruleType = " + ruleType, e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getNonPrioritizedRuleEntries(java.lang.String)
	 */
	public List<RuleEntry> getNonPrioritizedRuleEntries(String ruleType) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.createAlias("rule", "rule")
					.createAlias("ruleType", "ruleType")
					.add(Restrictions.eq("ruleType.name", ruleType))
					.add(Restrictions.eq("entry.retired", Boolean.FALSE))
					.add(Restrictions.eq("ruleType.retired", Boolean.FALSE))
					.add(Restrictions.isNull("entry.priority"))
					.addOrder(Order.asc("rule.tokenName"));
			return crit.list();
		} catch (Exception e) {
			log.error("Error retrieving non-prioritized rule entries for ruleType = " + ruleType, e);
			throw new DAOException(e);
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
				sortColumn = "title";
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
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRules(java.lang.String)
	 */
	public List<Rule> getRulesByType(String type) throws DAOException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT *\n");
			sql.append("  FROM dss_rule rule\n");
			sql.append("       INNER JOIN dss_rule_entry ruleEntry\n");
			sql.append("          ON rule.rule_id = ruleEntry.rule_id\n");
			sql.append("       INNER JOIN dss_rule_type ruleType\n");
			sql.append("          ON ruleEntry.rule_type_id = ruleType.rule_type_id\n");
			sql.append(" WHERE ruleType.name = :ruleName\n");
			sql.append(" AND ruleType.retired = false\n");
			sql.append(" AND ruleEntry.retired = false\n");

			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
			qry.setString("ruleName", type);
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e) {
			log.error("Error in method getRulesByType type = " + type, e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleType(org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleType saveRuleType(RuleType ruleType) throws DAOException {
		try {
			this.sessionFactory.getCurrentSession().saveOrUpdate(ruleType);
			return ruleType;
		} catch (Exception e) {
			log.error("Error saving rule type " + ruleType, e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleType(java.lang.String)
	 */
	public RuleType getRuleType(String type) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleType.class); 
	
			crit.add(Restrictions.eq("name", type)); 
			crit.add(Restrictions.eq("retired", Boolean.FALSE));
	
			return (RuleType)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule type " + type, e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleTypes(boolean)
	 */
	public List<RuleType> getRuleTypes(boolean includeRetired) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleType.class);
			crit.addOrder(Order.asc("name"));
			if (!includeRetired) {
				crit.add(Restrictions.eq("retired", Boolean.FALSE));
			}
	
			return crit.list();
		} catch (Exception e) {
			log.error("Error retrieving rule types", e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleEntry(org.openmrs.module.dss.hibernateBeans.RuleEntry)
	 */
	public RuleEntry saveRuleEntry(RuleEntry ruleEntry) throws DAOException {
		try {
			this.sessionFactory.getCurrentSession().saveOrUpdate(ruleEntry);
			return ruleEntry;
		} catch (Exception e) {
			log.error("Error saving rule entry " + ruleEntry, e);
			throw new DAOException(e);
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleEntry(org.openmrs.module.dss.hibernateBeans.Rule, org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleEntry getRuleEntry(Rule rule, RuleType ruleType) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.add(Restrictions.eq("rule", rule)).add(Restrictions.eq("ruleType", ruleType))
					.add(Restrictions.eq("retired", Boolean.FALSE));
			return (RuleEntry)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule entry rule = " + rule + " ruleType = " + ruleType, e);
			throw new DAOException(e);
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
					.add(Restrictions.eq("ruleType.name", ruleType))
					.add(Restrictions.eq("retired", Boolean.FALSE));
			return (RuleEntry)crit.uniqueResult();
		} catch (Exception e) {
			log.error("Error retrieving rule entry ruleId = " + ruleId + " ruleType = " + ruleType, e);
			throw new DAOException(e);
		}
	}

	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleReferences(org.openmrs.module.dss.hibernateBeans.Rule)
	 */
	public List<RuleEntry> getRuleReferences(Rule rule) throws DAOException {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleEntry.class, "entry")
					.add(Restrictions.eq("rule", rule)).add(Restrictions.eq("retired", Boolean.FALSE));
			return crit.list();
		} catch (Exception e) {
			log.error("Error retrieving rule references rule = " + rule, e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getDisassociatedRules(java.lang.String)
	 */
	public List<Rule> getDisassociatedRules(String ruleType) throws DAOException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT *\n");
			sql.append("  FROM dss_rule\n");
			sql.append(" WHERE rule_id NOT IN\n");
			sql.append("          (SELECT rule.rule_id\n");
			sql.append("             FROM dss_rule rule\n");
			sql.append("                  INNER JOIN dss_rule_entry ruleEntry\n");
			sql.append("                     ON rule.rule_id = ruleEntry.rule_id\n");
			sql.append("                  INNER JOIN dss_rule_type ruleType\n");
			sql.append("                     ON ruleEntry.rule_type_id = ruleType.rule_type_id\n");
			sql.append("            WHERE ruleType.name = :ruleName\n");
			sql.append("                  AND ruleType.retired = FALSE\n");
			sql.append("                  AND ruleEntry.retired = FALSE)\n");
			sql.append(" ORDER BY token_name ASC\n");
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
			qry.setString("ruleName", ruleType);
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e) {
			log.error("Error in method getDisassociatedRules ruleType = " + ruleType, e);
			throw new DAOException(e);
		}
	}
}
