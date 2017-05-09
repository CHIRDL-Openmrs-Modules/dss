package org.openmrs.module.dss.ruleLibrary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.LogicService;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;
import org.openmrs.module.chirdlutil.util.ChirdlUtilConstants;

public class generateRandom implements Rule {
	
	private LogicService logicService = Context.getLogicService();
	
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
	
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		if (parameters != null) {
			
			String type = (String) parameters.get(ChirdlUtilConstants.PARAMETER_1);
			String minVal = (String) parameters.get(ChirdlUtilConstants.PARAMETER_2);
			String maxVal = (String) parameters.get(ChirdlUtilConstants.PARAMETER_3);
			Integer min = null;
			Integer max = null;
			
			if (type != null && minVal != null && maxVal != null) {
				
				if (type.equalsIgnoreCase("integer")) {
					try {
						min = Integer.parseInt(minVal);
						max = Integer.parseInt(maxVal);
						
						if (min != null && max != null) {
							int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
							return new Result(randomNum);
						}
					}
					catch (NumberFormatException e) {
						
					}
				}
			}
		}
		return Result.emptyResult();
	}
}
