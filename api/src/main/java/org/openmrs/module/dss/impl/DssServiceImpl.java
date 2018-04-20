package org.openmrs.module.dss.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicService;
import org.openmrs.logic.impl.LogicCriteriaImpl;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.token.TokenService;
import org.openmrs.module.chirdlutil.util.IOUtil;
import org.openmrs.module.chirdlutil.util.Util;
import org.openmrs.module.dss.CompilingClassLoader;
import org.openmrs.module.dss.DssRule;
import org.openmrs.module.dss.DssRuleProvider;
import org.openmrs.module.dss.db.DssDAO;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.service.DssService;

/**
 * Defines implementations of services used by this module
 * 
 * @author Tammy Dugan
 *
 */
public class DssServiceImpl implements DssService
{

	private Log log = LogFactory.getLog( this.getClass() );
	
	private DssDAO dao;
	
	private static Map<String, org.openmrs.logic.Rule> loadedRuleMap = new HashMap<String, org.openmrs.logic.Rule>();

	/**
	 * Empty constructor
	 */
	public DssServiceImpl()
	{
	}

	/**
	 * @return DssDAO
	 */
	public DssDAO getDssDAO()
	{
		return this.dao;
	}

	/**
	 * Sets the DAO for this service. The dao
	 * allows interaction with the database.
	 * @param dao
	 */
	public void setDssDAO(DssDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#runRulesAsString(org.openmrs.Patient, 
	 * java.util.List)
	 */
	public String runRulesAsString(Patient p, List<Rule> ruleList) throws APIException
	{
		ArrayList<Result> results = this.runRules(p, ruleList);
		String reply = "";
		
		if(results == null || results.size()==0)
		{
			return "No rules run!!!!";
		}
				
		for(Result result:results)
		{
			if(result != null)
			{
				reply += result+"\n";
			}
		}
		return reply;
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#runRule(org.openmrs.Patient, 
	 * org.openmrs.module.dss.hibernateBeans.Rule)
	 */
	public Result runRule(Patient p, Rule rule) throws APIException
	{
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(rule);
		
		ArrayList<Result> results = 
			this.runRules(p, ruleList);
		
		//Since we ran only one rule, we will only have
		//one result object in the list of returned results
		//the single result object could have multiple results
		//depending on how the single rule was evaluated
		if(results != null && results.size()>0)
		{
			return results.get(0);
		}
		
		return Result.emptyResult();
	}
		
	/**
	 * @see org.openmrs.module.dss.service.DssService#runRules(org.openmrs.Patient, 
	 * java.util.List)
	 */
	public ArrayList<Result> runRules(Patient p, List<Rule> ruleList) throws APIException
	{
		ArrayList<Result> results = new ArrayList<Result>();
		Map<String,Object> parameters = null;
		String ruleName = null;
		LogicService logicSvc = Context.getLogicService();
		DssRuleProvider ruleProvider = new DssRuleProvider();
		String threadName = Thread.currentThread().getName();
		
		try
		{		
			for (Rule rule : ruleList)
			{		
				ruleName = rule.getTokenName();
				parameters = rule.getParameters();
				if (parameters == null) {
					parameters = new HashMap<String,Object>();
				}
				parameters.put("ruleProvider", ruleProvider);
				
				try
				{
					this.loadRule(ruleName, false);
				} catch(APIAuthenticationException e){
					//ignore a privilege exception
				}
				catch (Exception e1)
				{
					log.error("Error loading rule: "+ruleName);
					log.error(e1.getMessage());
					log.error(Util.getStackTrace(e1));
					results.add(null);
					continue;
				}
				
				long startTime = System.currentTimeMillis();
				Result result;
				try
				{
					result = logicSvc.eval(p.getPatientId(), new LogicCriteriaImpl(ruleName),parameters);
					results.add(result);

				} catch(APIAuthenticationException e){
					//ignore a privilege exception
				} catch (Exception e)
				{
					log.error("Error evaluating rule: "+ruleName);
					log.error(e.getMessage());
					log.error(Util.getStackTrace(e));
					results.add(null);
					continue;
				}
				
				long elapsedTime = System.currentTimeMillis()-startTime;
				
				if(elapsedTime > 100){
					System.out.println("logicSvc.eval time(" + ruleName + ", " + threadName + "): "+elapsedTime);
				}	
			}

			return results;

		} catch (Exception e)
		{
			log.error("Error running rules.");
			log.error(e.getMessage());
			log.error(Util.getStackTrace(e));
			return null;
		} 
	}

	/**
	 * @see org.openmrs.module.dss.service.DssService#loadRule(java.lang.String, boolean)
	 */
	public org.openmrs.logic.Rule loadRule(String rule, boolean updateRule) throws APIException
	{ 
		org.openmrs.logic.Rule loadedRule = loadedRuleMap.get(rule);
		if (loadedRule != null && !updateRule) {
			// The rule has already been loaded, and an update is not needed.
			return loadedRule;
		}
		
		// Create a CompilingClassLoader
		CompilingClassLoader ccl = CompilingClassLoader.getInstance();

		AdministrationService adminService = Context.getAdministrationService();
		String rulePackagePrefix = Util.formatPackagePrefix(adminService
					.getGlobalProperty("dss.rulePackagePrefix"));

		Class<?> clas = null;

		// try to load the class dynamically
		if (!rule.contains(rulePackagePrefix))
		{
			try
			{
				clas = ccl.loadClass(rulePackagePrefix + rule);
			} catch (Exception e)
			{
				//ignore this exception
			}
		} else
		{
			try
			{
				clas = ccl.loadClass(rule);
			} catch (Exception e)
			{
				//ignore this exception
			}
		}

		// try to load the class from the class library
		if (clas == null)
		{	
			String defaultPackagePrefixProp = adminService.getGlobalProperty("dss.defaultPackagePrefix");
			List<String> defaultPackagePrefixes = Util.formatPackagePrefixes(defaultPackagePrefixProp, ",");
			if (defaultPackagePrefixes.size() > 0)
			{
				int cnt = 0;
				while ((clas == null) && (cnt < defaultPackagePrefixes.size())) 
				{
					String defaultPackagePrefix = defaultPackagePrefixes.get(cnt++);
					if (!rule.contains(defaultPackagePrefix))
					{
						try
						{
							clas = ccl.loadClass(defaultPackagePrefix + rule);
						} catch (Exception e)
						{
							//ignore this exception
						}
					}
				}
			}
		}

		// try to load the class as it is
		if (clas == null)
		{
			try
			{
				clas = ccl.loadClass(rule);
			} catch (Exception e)
			{
				//ignore this exception
			}
		}

		if (clas == null)
		{
			throw new APIException("Could not load class for rule: " + rule);
		}

		Object obj = null;
		try {
	        obj = clas.newInstance();
        }
        catch (Exception e) {
	        log.error("",e);
        }

		if (!(obj instanceof org.openmrs.logic.Rule))
		{
			throw new APIException("Could not load class for rule: " + rule
					+ ". The rule must implement the Rule interface.");
		}
		
		loadedRule = (org.openmrs.logic.Rule)obj;

		try {
			Context.getService(TokenService.class).registerToken(rule, new DssRuleProvider(), clas.getName());
	        loadedRuleMap.put(rule, loadedRule);
        }
        catch (Exception e) {
	        log.error("",e);
        }
        
        return loadedRule;
	}

	/**
	 * @see org.openmrs.module.dss.service.DssService#getRule(int)
	 */
	public Rule getRule(int ruleId) throws APIException
	{
		return getDssDAO().getRule(ruleId);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRule(java.lang.String)
	 */
	public Rule getRule(String tokenName) throws APIException
	{
		return getDssDAO().getRule(tokenName);
	}

	/**
	 * @see org.openmrs.module.dss.service.DssService#getPrioritizedRules(java.lang.String)
	 */
	public List<Rule> getPrioritizedRules(String type) throws APIException
	{
		return getDssDAO().getPrioritizedRules(type, 0);
	}
	
	/**
     * @see org.openmrs.module.dss.service.DssService#getPrioritizedRuleEntries(java.lang.String)
     */
	public List<RuleEntry> getPrioritizedRuleEntries(String ruleType) throws APIException {
		return getDssDAO().getPrioritizedRuleEntries(ruleType, 0);
	}
	
	/**
	 * Looks up a rule attribute by name
	 * @see org.openmrs.module.dss.service.DssService#getRuleAttribute(java.lang.String)
	 */
	public RuleAttribute getRuleAttribute(String ruleAttributeName) throws APIException {
   		return getDssDAO().getRuleAttribute(ruleAttributeName);
    }
	
	/**
	 * Looks up a rule attribute by primary key
	 * @see org.openmrs.module.dss.service.DssService#getRuleAttribute(java.lang.Integer)
	 */
	public RuleAttribute getRuleAttribute(Integer ruleAttributeId) throws APIException {
   		return getDssDAO().getRuleAttribute(ruleAttributeId);
    }
	
	/**
	 * returns the first rule attribute value matched by rule id and rule attribute name
	 * @see org.openmrs.module.dss.service.DssService#getRuleAttributeValue(java.lang.Integer, java.lang.String)
	 */
	public RuleAttributeValue getRuleAttributeValue(Integer ruleId, String ruleAttributeName) throws APIException{
		return getDssDAO().getRuleAttributeValue(ruleId, ruleAttributeName);
	}
	
	/**
	 * returns all rule attribute values for a given rule id and rule attribute name
	 * @see org.openmrs.module.dss.service.DssService#getRuleAttributeValues(java.lang.Integer, java.lang.String)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, String ruleAttributeName)
			 throws APIException{
		return getDssDAO().getRuleAttributeValues(ruleId, ruleAttributeName);
	}
	
	/**
	 * Returns list of rule attribute values for a given rule id and rule attribute id
	 * @see org.openmrs.module.dss.service.DssService#getRuleAttributeValues(java.lang.Integer, java.lang.Integer)
	 */
	public List<RuleAttributeValue> getRuleAttributeValues(Integer ruleId, Integer ruleAttributeId)
			 throws APIException{
		return getDssDAO().getRuleAttributeValues(ruleId, ruleAttributeId);
	}
	
	/**
	 * Saves or updates rule attribute value changes to the database
	 * @see org.openmrs.module.dss.service.DssService#saveRuleAttributeValue(
	 * org.openmrs.module.dss.hibernateBeans.RuleAttributeValue)
	 */
	public RuleAttributeValue saveRuleAttributeValue(RuleAttributeValue value) throws APIException {

		return getDssDAO().saveRuleAttributeValue(value);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getPrioritizedRules(java.lang.String, java.lang.Integer)
	 */
    public List<Rule> getPrioritizedRules(String type, Integer startPriority) throws APIException {
	    return getDssDAO().getPrioritizedRules(type, startPriority);
    }
    
    /**
	 * @see org.openmrs.module.dss.service.DssService#getPrioritizedRuleEntries(java.lang.String, java.lang.Integer)
	 */
	public List<RuleEntry> getPrioritizedRuleEntries(String ruleType, Integer startPriority) throws APIException {
		return getDssDAO().getPrioritizedRuleEntries(ruleType, startPriority);
	}
    
	/**
	 * @see org.openmrs.module.dss.service.DssService#getNonPrioritizedRules(java.lang.String)
	 */
	public List<Rule> getNonPrioritizedRules(String type) throws APIException
	{
		return getDssDAO().getNonPrioritizedRules(type);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getNonPrioritizedRuleEntries(java.lang.String)
	 */
	public List<RuleEntry> getNonPrioritizedRuleEntries(String ruleType) throws APIException {
		return getDssDAO().getNonPrioritizedRuleEntries(ruleType);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRules(org.openmrs.module.dss.hibernateBeans.Rule, 
	 * boolean, boolean, java.lang.String)
	 */
	public List<Rule> getRules(Rule rule, boolean ignoreCase, boolean enableLike, String sortColumn) 
			throws APIException
	{
		return getDssDAO().getRules(rule, ignoreCase, enableLike, sortColumn);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRules(java.lang.String)
	 */
	public List<Rule> getRulesByType(String type) throws APIException {
		return getDssDAO().getRulesByType(type);
	}
	
	/**
	 * 
	 * Returns a list of rule attribute values for a given rule attribute id and value
	 * 
	 * @param ruleAttributeId
	 * @param value
	 * @return
	 */
	public List<RuleAttributeValue> getRuleAttributesByValue(Integer ruleAttributeId,String value) throws APIException {
		return getDssDAO().getRuleAttributesByValue(ruleAttributeId, value);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#addRule(java.lang.String, org.openmrs.module.dss.DssRule)
	 */
	public Rule addRule(String classFilename,DssRule rule) throws APIException
	{
		String tokenName = IOUtil.getFilenameWithoutExtension(classFilename);
		Rule databaseRule = getDssDAO().getRule(tokenName);
		
		if(databaseRule != null)
		{
			databaseRule.setLastModified(new java.util.Date());
		}else
		{
			databaseRule = new Rule();
			databaseRule.setCreationTime(new java.util.Date());
		}
		
		databaseRule.setClassFilename(classFilename);
		databaseRule.setAction(rule.getAction());
		databaseRule.setAuthor(rule.getAuthor());
		databaseRule.setCitations(rule.getCitations());
		databaseRule.setData(rule.getData());
		databaseRule.setExplanation(rule.getExplanation());
		databaseRule.setInstitution(rule.getInstitution());
		databaseRule.setKeywords(rule.getKeywords());
		databaseRule.setLinks(rule.getLinks());
		databaseRule.setLogic(rule.getLogic());
		databaseRule.setPurpose(rule.getPurpose());
		databaseRule.setRuleCreationDate(rule.getDate());
		databaseRule.setSpecialist(rule.getSpecialist());
		databaseRule.setTitle(rule.getTitle());
		databaseRule.setVersion(rule.getVersion());
		databaseRule.setTokenName(tokenName);
		databaseRule.setAgeMax(rule.getAgeMax());
		databaseRule.setAgeMin(rule.getAgeMin());
		databaseRule.setAgeMinUnits(rule.getAgeMinUnits());
		databaseRule.setAgeMaxUnits(rule.getAgeMaxUnits());
		
		databaseRule = getDssDAO().addOrUpdateRule(databaseRule);
		updateRuleReferences(rule, databaseRule);
		return databaseRule;
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#saveRuleType(org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleType saveRuleType(RuleType ruleType) throws APIException {
		return getDssDAO().saveRuleType(ruleType);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#retireRuleType(org.openmrs.module.dss.hibernateBeans.RuleType, 
	 * java.lang.String)
	 */
	public void retireRuleType(RuleType ruleType, String reason) throws APIException {
		getDssDAO().saveRuleType(ruleType);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRuleType(java.lang.String)
	 */
	public RuleType getRuleType(String type) throws APIException {
		return getDssDAO().getRuleType(type);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRuleTypes(boolean)
	 */
	public List<RuleType> getRuleTypes(boolean includeRetired) throws APIException {
		return getDssDAO().getRuleTypes(includeRetired);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#saveRuleEntry(org.openmrs.module.dss.hibernateBeans.RuleEntry)
	 */
	public RuleEntry saveRuleEntry(RuleEntry ruleEntry) throws APIException {
		return getDssDAO().saveRuleEntry(ruleEntry);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#retireRuleEntry(org.openmrs.module.dss.hibernateBeans.RuleEntry, 
	 * java.lang.String)
	 */
	public void retireRuleEntry(RuleEntry ruleEntry, String reason) throws APIException {
		Context.getService(DssService.class).saveRuleEntry(ruleEntry);
	}

	/**
	 * @see org.openmrs.module.dss.service.DssService#getRuleEntry(org.openmrs.module.dss.hibernateBeans.Rule, 
	 * org.openmrs.module.dss.hibernateBeans.RuleType)
	 */
	public RuleEntry getRuleEntry(Rule rule, RuleType ruleType) throws APIException {
		return getDssDAO().getRuleEntry(rule, ruleType);
	}
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRuleEntry(java.lang.Integer, java.lang.String)
	 */
	public RuleEntry getRuleEntry(Integer ruleId, String ruleType) throws APIException {
		return getDssDAO().getRuleEntry(ruleId, ruleType);
	}
	
	
	/**
	 * @see org.openmrs.module.dss.service.DssService#getRuleReferences(org.openmrs.module.dss.hibernateBeans.Rule)
	 */
	public List<RuleEntry> getRuleReferences(Rule rule) throws APIException {
		return getDssDAO().getRuleReferences(rule);
	}
	
	/**
	 * Updates any information referenced by rule entries.
	 * 
	 * @param dssRule The rule containing the new priority
	 * @param rule The database rule being referenced.
	 */
	private void updateRuleReferences(DssRule dssRule, Rule rule) {
		// See if there are currently any entries referencing this rule
		List<RuleEntry> ruleEntries = getRuleReferences(rule);
		if (ruleEntries != null && !ruleEntries.isEmpty()) {
			Integer priority = dssRule.getPriority();
			for (RuleEntry ruleEntry : ruleEntries) {
				Integer currentPriority = ruleEntry.getPriority();
				if ((currentPriority == null && priority != null) ||
						(currentPriority != null && priority == null) || 
						(currentPriority != null && !currentPriority.equals(priority))) {
					// Retire the current row
					Context.getService(DssService.class).retireRuleEntry(
						ruleEntry, "Priority change from " + currentPriority + " to " + priority);
					
					// Only create a new row if the rule has a priority < 1000
					if (priority == null || priority.compareTo(RuleEntry.RULE_PRIORITY_RETIRE) < 0) {
						// Create a new rule entry
						RuleEntry newRuleEntry = new RuleEntry();
						newRuleEntry.setPriority(priority);
						newRuleEntry.setRule(ruleEntry.getRule());
						newRuleEntry.setRuleType(ruleEntry.getRuleType());
						Context.getService(DssService.class).saveRuleEntry(newRuleEntry);
					}
				}
			}
		}
	}
}