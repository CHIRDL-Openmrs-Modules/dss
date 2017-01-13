package org.openmrs.module.dss.ruleLibrary;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicCriteria;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.impl.LogicCriteriaImpl;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;
import org.openmrs.module.dss.service.DssService;

public class bpPercentage implements Rule {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getParameterList()
	 */
	public Set<RuleParameterInfo> getParameterList() {
		return null;
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDependencies()
	 */
	public String[] getDependencies() {
		return new String[] {};
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getTTL()
	 */
	public int getTTL() {
		return 0; // 60 * 30; // 30 minutes
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDatatype(String)
	 */
	public Datatype getDefaultDatatype() {
		return Datatype.CODED;
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, java.lang.Integer, java.util.Map)
	 */
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		Result diasResult = null;
		Result systResult = null;
		
		String conceptName = "SYSTOLIC_BP";
		Integer encounterId = (Integer) parameters.get("encounterId");
		
		LogicCriteria conceptCriteria = new LogicCriteriaImpl(conceptName);
		
		LogicCriteria fullCriteria = null;
		LogicCriteria encounterCriteria = null;
		
		if (encounterId != null) {
			encounterCriteria = new LogicCriteriaImpl("encounterId").equalTo(encounterId.intValue());
			
			fullCriteria = conceptCriteria.and(encounterCriteria);
		} else {
			fullCriteria = conceptCriteria;
		}
		systResult = context.read(patientId, context.getLogicDataSource("obs"), fullCriteria.last());
		
		conceptName = "DIASTOLIC_BP";
		encounterId = (Integer) parameters.get("encounterId");
		
		conceptCriteria = new LogicCriteriaImpl(conceptName);
		
		fullCriteria = null;
		encounterCriteria = null;
		
		if (encounterId != null) {
			encounterCriteria = new LogicCriteriaImpl("encounterId").equalTo(encounterId.intValue());
			
			fullCriteria = conceptCriteria.and(encounterCriteria);
		} else {
			fullCriteria = conceptCriteria;
		}
		diasResult = context.read(patientId, context.getLogicDataSource("obs"), fullCriteria.last());
		
		if (diasResult != null && systResult != null) {
			Double diasBPNum = diasResult.toNumber();
			Double systBPNum = systResult.toNumber();
			
			if (diasBPNum != null && systBPNum != null) {
				// Get the percentages
				DssService dssService = Context.getService(DssService.class);
				org.openmrs.module.dss.hibernateBeans.Rule rule = new org.openmrs.module.dss.hibernateBeans.Rule();
				Patient patient = Context.getPatientService().getPatient(patientId);
				Map<String, Object> map = new HashMap<String, Object>();
				
				// Get the systolic percentage
				map.put("param1", "systolic");
				map.put("param2", systResult.toString());
				map.put("encounterId", encounterId);
				rule.setTokenName("LookupPatientBPcentile");
				rule.setParameters(map);
				Result systPercentResult = dssService.runRule(patient, rule);
				
				// Get the diastolic percentage
				map.put("param1", "diastolic");
				map.put("param2", diasResult.toString());
				Result diasPercentResult = dssService.runRule(patient, rule);
				
				// Create the string
				if (systPercentResult != null && diasPercentResult != null && systPercentResult.toNumber() != null && 
						diasPercentResult.toNumber() != null) {
					Integer systPercent = null;
					Integer diasPercent = null;
					try {
						systPercent = systPercentResult.toNumber().intValue();
						diasPercent = diasPercentResult.toNumber().intValue();
						String systPercentStr = String.valueOf(systPercent);
						String diasPercentStr = String.valueOf(diasPercent);
						if (systPercentResult.toNumber() > 99) {
							systPercentStr = ">99";
						}
						
						if (diasPercentResult.toNumber() > 99) {
							diasPercentStr = ">99";
						}
						
						return new Result(systPercentStr + "%/" + diasPercentStr + "%");
					}
					catch (NumberFormatException e) {
						log.error("Error parsing systolic and/or diastolic results into an Integer", e);
					}
				}
			}
		}
		
		return Result.emptyResult();
	}
}
