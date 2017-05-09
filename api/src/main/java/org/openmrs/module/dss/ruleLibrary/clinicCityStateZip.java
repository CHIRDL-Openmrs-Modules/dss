package org.openmrs.module.dss.ruleLibrary;

import java.util.Map;
import java.util.Set;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;

public class clinicCityStateZip implements Rule {
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#getParameterList()
	 */
	public Set<RuleParameterInfo> getParameterList() {
		return null;
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#getDependencies()
	 */
	public String[] getDependencies() {
		return new String[] {};
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#getTTL()
	 */
	public int getTTL() {
		return 0; // 60 * 30; // 30 minutes
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#getDefaultDatatype()
	 */
	public Datatype getDefaultDatatype() {
		return Datatype.CODED;
	}
	
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		Integer locationId = (Integer) parameters.get("locationId");
		if (locationId == null) {
			return Result.emptyResult();
		}
		
		Location location = Context.getLocationService().getLocation(locationId);
		if (location == null) {
			return Result.emptyResult();
		}
		
		String city = location.getCityVillage();
		String state = location.getStateProvince();
		String zip = location.getPostalCode();
		
		if (city != null && state != null && zip != null) {
			return new Result(city + ", " + state + "  " + zip);
		}
		
		return Result.emptyResult();
	}
}
