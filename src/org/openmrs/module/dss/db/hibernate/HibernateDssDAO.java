package org.openmrs.module.dss.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dss.db.DssDAO;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.chirdlutil.util.Util;

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

	public Rule getRule(int ruleId)
	{
		try
		{
			String sql = "select * from dss_rule where rule_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setInteger(0, ruleId);
			qry.addEntity(Rule.class);
			return (Rule) qry.uniqueResult();
		} catch (Exception e)
		{
			this.log.error(Util.getStackTrace(e));
		}
		return null;
	}

	public Rule getRule(String tokenName)
	{
		try
		{
			String sql = "select * from dss_rule where token_name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setString(0, tokenName);
			qry.addEntity(Rule.class);
			return (Rule) qry.uniqueResult();
		} catch (Exception e)
		{
			this.log.error(Util.getStackTrace(e));
		}
		return null;
	}

	public void deleteRule(int ruleId)
	{
		try
		{
			Rule rule = this.getRule(ruleId);
			this.sessionFactory.getCurrentSession().delete(rule);
		} catch (Exception e)
		{
			this.log.error(Util.getStackTrace(e));
		}
	}

	public Rule addOrUpdateRule(Rule rule)
	{
		try
		{
			// If the rule type for the rule to save is still null,
			// then set the rule type to the token_name
			if (rule.getRuleType() == null)
			{
				rule.setRuleType(rule.getTokenName());
				this.log.error("Rule "+rule.getTokenName()+" does not have a rule type set. "+
						"It will not be available for prioritization until the rule type is set to a form name.");
			}
			return (Rule) this.sessionFactory.getCurrentSession().merge(rule);
		} catch (Exception e)
		{
			this.log.error(Util.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Looks up a rule attribute by primary key
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttribute(int)
	 */
	public RuleAttribute getRuleAttribute(int ruleAttributeId) {
		return (RuleAttribute) sessionFactory.getCurrentSession().get(RuleAttribute.class, ruleAttributeId);
	}
	
	/**
	 * Looks up a rule attribute by name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttribute(java.lang.String)
	 */
	public RuleAttribute getRuleAttribute(String ruleAttributeName) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttribute.class); 

		crit.add(Restrictions.eq("name", ruleAttributeName)); 

		return (RuleAttribute) crit.uniqueResult();
	}
	
	/**
	 * returns all rule attribute values for a given rule id and rule attribute name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValues(java.lang.Integer, java.lang.String)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, String ruleAttributeName) {
		try {
			RuleAttribute ruleAttribute = this.getRuleAttribute(ruleAttributeName);
			
			if (ruleAttribute != null) {
				Integer ruleAttributeId = ruleAttribute.getRuleAttributeId();
				
				return getRuleAttributeValues(ruleId,ruleAttributeId);
				
			}
		}
		catch (Exception e) {
			log.error("Error in method getRuleAttributeValue", e);
		}
		return null;
	}
	
	/**
	 * Returns a list of rule attribute values for a given rule attribute id and value
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributesByValue(java.lang.Integer, java.lang.String)
	 */
	public List<RuleAttributeValue> getRuleAttributesByValue(Integer ruleAttributeId,String value){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttributeValue.class); 

		crit.add(Restrictions.eq("value", value)); 

		return crit.list();
	}
	
	/**
	 * Returns list of rule attribute values for a given rule id and rule attribute id
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValues(java.lang.Integer, java.lang.Integer)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, Integer ruleAttributeId) {
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RuleAttributeValue.class);
		crit.add(Restrictions.eq("ruleId", ruleId)); 
		crit.add(Restrictions.eq("ruleAttributeId", ruleAttributeId)); 

		return crit.list();
	}
	
	/**
	 * returns the first rule attribute value matched by rule id and rule attribute name
	 * @see org.openmrs.module.dss.db.DssDAO#getRuleAttributeValue(java.lang.Integer, java.lang.String)
	 */
	public RuleAttributeValue getRuleAttributeValue(Integer ruleId, String ruleAttributeName) {
		List<RuleAttributeValue> list = getRuleAttributeValues(ruleId, ruleAttributeName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * Saves or updates rule attribute value changes to the database
	 * @see org.openmrs.module.dss.db.DssDAO#saveRuleAttributeValue(org.openmrs.module.dss.hibernateBeans.RuleAttributeValue)
	 */
	public RuleAttributeValue saveRuleAttributeValue(RuleAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	/**
	 * @see org.openmrs.module.dss.db.DssDAO#getPrioritizedRules(java.lang.String, java.lang.Integer)
	 */
    public List<Rule> getPrioritizedRules(String type, Integer startPriority) 
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

			String sql = "select * from dss_rule where rule_type=? and "
					+ "priority >=? and priority<1000 and "
					+ "version='1.0' order by priority " + sortOrder;
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
			this.log.error(Util.getStackTrace(e));
		}
		return null;
    }
    
	public List<Rule> getNonPrioritizedRules(String type) throws DAOException
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

			String sql = "select * from dss_rule where rule_type = ? and "
					+ "priority is null and version='1.0' order by priority "
					+ sortOrder;
			SQLQuery qry = this.sessionFactory.getCurrentSession()
					.createSQLQuery(sql);
			qry.setString(0, type);
			qry.addEntity(Rule.class);
			return qry.list();
		} catch (Exception e)
		{
			this.log.error(Util.getStackTrace(e));
		}

		return null;
	}

	public List<Rule> getRules(Rule rule, boolean ignoreCase,
			boolean enableLike, String sortColumn) throws DAOException
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
			this.log.error(Util.getStackTrace(e));
		}
		return null;
	}
}
