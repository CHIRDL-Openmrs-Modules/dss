package org.openmrs.module.dss.ruleLibrary;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;

public class LocationInfoLookup implements Rule {
	
	/** Logger for this class and subclasses */
	private static final Logger log = LoggerFactory.getLogger(LocationInfoLookup.class);
	
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
		String locationStr = (String) parameters.get("location");
		if (locationStr == null) {
			log.error("No location specified");
			return Result.emptyResult();
		}
		
		String attribute = (String) parameters.get("param1");
		if (attribute == null) {
			log.error("No location attribute specified");
			return Result.emptyResult();
		}
		
		Location location = Context.getLocationService().getLocation(locationStr);
		if (location == null) {
			log.error("No location found with ID {}", locationStr);
			return Result.emptyResult();
		}
		
		if ("name".equalsIgnoreCase(attribute)) {
			return new Result(location.getName());
		} else if ("address1".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress1());
		} else if ("address2".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress2());
		} else if ("city".equalsIgnoreCase(attribute)) {
			return new Result(location.getCityVillage());
		} else if ("state".equalsIgnoreCase(attribute)) {
			return new Result(location.getStateProvince());
		} else if ("postalcode".equalsIgnoreCase(attribute)) {
			return new Result(location.getPostalCode());
		} else if ("country".equalsIgnoreCase(attribute)) {
			return new Result(location.getCountry());
		} else if ("countydistrict".equalsIgnoreCase(attribute)) {
			return new Result(location.getCountyDistrict());
		} else if ("datechanged".equalsIgnoreCase(attribute)) {
			return new Result(location.getDateChanged());
		} else if ("datecreated".equalsIgnoreCase(attribute)) {
			return new Result(location.getDateCreated());
		} else if ("dateretired".equalsIgnoreCase(attribute)) {
			return new Result(location.getDateRetired());
		} else if ("description".equalsIgnoreCase(attribute)) {
			return new Result(location.getDescription());
		} else if ("displaystring".equalsIgnoreCase(attribute)) {
			return new Result(location.getDisplayString());
		} else if ("latitude".equalsIgnoreCase(attribute)) {
			return new Result(location.getLatitude());
		} else if ("longitude".equalsIgnoreCase(attribute)) {
			return new Result(location.getLongitude());
		} else if ("neighborhoodcell".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress3()); // CHICA-1151 getNeighborhoodCell() has been replaced by getAddress3()
		}  else if ("region".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress6()); // CHICA-1151 getRegion() has been replaced by getAddress6()
		} else if ("retired".equalsIgnoreCase(attribute)) {
			return new Result(location.getRetired());
		} else if ("retiredreason".equalsIgnoreCase(attribute)) {
			return new Result(location.getRetireReason());
		} else if ("subregion".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress5()); // CHICA-1151 getSubregion() has been replaced by getAddress5()
		} else if ("townshipdivision".equalsIgnoreCase(attribute)) {
			return new Result(location.getAddress4()); // CHICA-1151 getTownshipDivision() has been replaced by getAddress4()
		} else {
			log.error("{} is not a supported attribute for a Location.", attribute);
			return Result.emptyResult();
		}
	}
}
