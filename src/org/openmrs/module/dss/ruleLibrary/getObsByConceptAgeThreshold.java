package org.openmrs.module.dss.ruleLibrary;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicCriteria;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.impl.LogicCriteriaImpl;
import org.openmrs.logic.op.OperandNumeric;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;
import org.openmrs.module.chirdlutil.util.Util;

public class getObsByConceptAgeThreshold implements Rule
{
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getParameterList()
	 */
	public Set<RuleParameterInfo> getParameterList()
	{
		return null;
	}

	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDependencies()
	 */
	public String[] getDependencies()
	{
		return new String[]
		{};
	}

	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getTTL()
	 */
	public int getTTL()
	{
		return 0; // 60 * 30; // 30 minutes
	}

	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDatatype(String)
	 */
	public Datatype getDefaultDatatype()
	{
		return Datatype.CODED;
	}

	/**
	 * Limits results by a given encounter id
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, org.openmrs.Patient, java.util.Map)
	 */
	public Result eval(LogicContext context, Integer patientId,
			Map<String, Object> parameters) throws LogicException
	{
		if(parameters == null)
		{
			return Result.emptyResult();
		}
		
		//=concept_name and age_at_obs < val_in_units and concept_name >=threshold
		String conceptName = (String) parameters.get("param1");
		Integer conceptThreshold =  Integer.parseInt((String) parameters.get("param2"));
		Integer ageThreshold = Integer.parseInt((String) parameters.get("param3"));
		String ageUnits = (String) parameters.get("param4");
		
		PatientService patientService = Context.getPatientService();
		
		Patient patient = patientService.getPatient(patientId);
		Calendar birthdate = Calendar.getInstance();
		birthdate.setTime(patient.getBirthdate());
		
		//figure out the date threshold for the observation
		if(ageUnits.equalsIgnoreCase(Util.MONTH_ABBR)){
			birthdate.add(Calendar.MONTH, ageThreshold);
		}else if(ageUnits.equalsIgnoreCase(Util.DAY_ABBR)){
			birthdate.add(Calendar.DAY_OF_YEAR, ageThreshold);
		}else if(ageUnits.equalsIgnoreCase(Util.WEEK_ABBR)){
			birthdate.add(Calendar.WEEK_OF_YEAR, ageThreshold);
		}else if(ageUnits.equalsIgnoreCase(Util.YEAR_ABBR)){
			birthdate.add(Calendar.YEAR, ageThreshold);
		}
		
		if(conceptName == null)
		{
			return Result.emptyResult();
		}
		Result ruleResult = null;
		
		//limit to just that concept
		LogicCriteria fullCriteria = new LogicCriteriaImpl(conceptName);
		
		//limit to concept greater than or equal to threshold
		LogicCriteria additionalCriteria = new LogicCriteriaImpl(conceptName).gte(new OperandNumeric(conceptThreshold));
		fullCriteria = fullCriteria.and(additionalCriteria);
		
		//limit to obs before a date computed from patient age
		additionalCriteria = new LogicCriteriaImpl(conceptName).before(birthdate.getTime());
		fullCriteria = fullCriteria.and(additionalCriteria);

		ruleResult = context.read(patientId,context.getLogicDataSource("obs"), 
				fullCriteria.last());

		if (ruleResult != null&&ruleResult.size()>0)
		{
			return new Result("YES");
		}else{
			return new Result("NO");
		}
	}
}