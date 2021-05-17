package org.openmrs.module.dss.ruleLibrary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;
import org.openmrs.module.chirdlutil.util.ResultDateComparator;

/**
 * Calculates a person's age in years based from their date of birth to the index date
 */
public class getReverseNumericResultElement implements Rule {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see org.openmrs.logic.Rule#getParameterList()
	 */
	@Override
	public Set<RuleParameterInfo> getParameterList() {
		return null;
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getDependencies()
	 */
	@Override
	public String[] getDependencies() {
		return new String[] {};
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getTTL()
	 */
	@Override
	public int getTTL() {
		return 0;
	}
	
	/**
	 * @see org.openmrs.logic.Rule#getDefaultDatatype()
	 */
	@Override
	public Datatype getDefaultDatatype() {
		return Datatype.NUMERIC;
	}
	
	/**
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, java.lang.Integer, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		Integer index = null;
		List<Result> results = null;
		Result distinctResult = null;
		
		if (parameters != null) {
			Object param1Obj = parameters.get("param1");
			if (param1Obj instanceof String) {
				try {
					index = Integer.valueOf((String) param1Obj);
				} catch (NumberFormatException e) {
					this.log.error("Error parsing value " + param1Obj + " into an integer", e);
					return Result.emptyResult();
				}
			}
			
			Object paramResults = parameters.get("param2");
			if (paramResults instanceof List<?>) {
				results = (List<Result>)paramResults;
			} else {
				return Result.emptyResult();
			}
		}
		
		if (index != null && index.intValue() < results.toArray().length) {
			// Sort the results by date
			Collections.sort(results, new ResultDateComparator());
			// Reverse the list
			Collections.reverse(results);
			distinctResult = (Result) results.toArray()[index.intValue()];
		}
		
		if (distinctResult == null) {
			return Result.emptyResult();
		}
		
		if (distinctResult.toString() == null) {
			distinctResult.setValueText(String.valueOf(distinctResult.toNumber()));
		}
		
		return distinctResult;
	}
	
}
