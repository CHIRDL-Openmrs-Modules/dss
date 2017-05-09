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
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;

public class clinicFullAddress implements Rule {
	
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
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, java.lang.Integer,
	 *      java.util.Map)
	 */
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		FormInstance formInstance = (FormInstance) parameters.get("formInstance");
		if (formInstance == null) {
			return Result.emptyResult();
		}
		
		Integer locationId = formInstance.getLocationId();
		Location location = Context.getLocationService().getLocation(locationId);
		if (location == null) {
			return Result.emptyResult();
		}
		
		String address = location.getAddress1();
		String city = location.getCityVillage();
		String state = location.getStateProvince();
		String zip = location.getPostalCode();
		
		StringBuffer addressBuffer = new StringBuffer();
		if (address != null) {
			addressBuffer.append(address);
			addressBuffer.append(" ");
		}
		
		if (city != null) {
			addressBuffer.append(city);
			addressBuffer.append(", ");
		}
		
		if (state != null) {
			addressBuffer.append(zip);
		}
		
		return new Result(addressBuffer.toString());
	}
	
}
