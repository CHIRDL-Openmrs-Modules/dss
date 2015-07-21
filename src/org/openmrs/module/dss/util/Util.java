/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dss.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptDescription;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.module.dss.DssRule;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.service.DssService;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;


/**
 *
 */
public class Util {
	
	private static Log log = LogFactory.getLog(Util.class);
	
	/**
	 * read csv input stream and create RuleAttributeValue objects according to it.
	 * @param input the inputStream including the data source
	 * @return a list of new created RuleAttributeValueDescriptor objects.
	 * @throws Exception 
	 */
	public static List<RuleAttributeValueDescriptor> getRuleAttributeValueDescriptorFromCSV(InputStream input) throws Exception{
		List<RuleAttributeValueDescriptor> ruleAttributeValuedList = null;
		try{
			InputStreamReader inStreamReader = new InputStreamReader(input);
			CSVReader reader = new CSVReader(inStreamReader, ',');
			HeaderColumnNameTranslateMappingStrategy<RuleAttributeValueDescriptor> strat = new HeaderColumnNameTranslateMappingStrategy<RuleAttributeValueDescriptor>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("rule_name", "ruleName");
			map.put("location_name", "locationName");
			map.put("location_tag_name", "locationTagName");
			map.put("attribute_name", "attributeName");
			map.put("attribute_value", "attributeValue");
			strat.setType(RuleAttributeValueDescriptor.class);
			strat.setColumnMapping(map);
			CsvToBean<RuleAttributeValueDescriptor> csv = new CsvToBean<RuleAttributeValueDescriptor>();
			ruleAttributeValuedList = csv.parse(strat, reader);
		}
		catch(Exception e){
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return ruleAttributeValuedList;
	}
	
	/**
	 * Create RuleAttributeValue object from a RuleAttributeValueDescriptor object
	 * @param ruleAttributeValuedList data source
	 * @return a list of new created RuleAttributeValue objects
	 */
	public static List<RuleAttributeValue> getRuleAttributeValues(List<RuleAttributeValueDescriptor> ruleAttributeValuedList) {
		List<RuleAttributeValue> ruleAttributeValueList = new ArrayList<RuleAttributeValue>();
		LocationService locationService = Context.getLocationService();
		DssService dssService = Context.getService(DssService.class);
		if (ruleAttributeValuedList != null) {
			for (RuleAttributeValueDescriptor ruleAttributeValueDescriptor : ruleAttributeValuedList) {
				/*
				 * create RuleAttributeValue object by
				 * RuleAttributeVlaueDescriptor
				 */
				RuleAttributeValue ruleAttributeValue = new RuleAttributeValue();
				Rule rule = dssService.getRule(ruleAttributeValueDescriptor.getRuleName());
				if(rule==null) continue;
				Integer ruleId = rule.getRuleId();
				Location loc = locationService.getLocation(ruleAttributeValueDescriptor.getLocationName());
				if(loc==null) continue;
				Integer locationId = loc.getId();
				LocationTag locTag = locationService.getLocationTagByName(ruleAttributeValueDescriptor.getLocationTagName());
				if(locTag==null) continue;
				Integer locationTagId = locTag.getId();
				RuleAttribute ruleAttribute = dssService.getRuleAttribute(ruleAttributeValueDescriptor.getAttributeName());
				if(ruleAttribute==null) continue;
				Integer ruleAttributeId = ruleAttribute.getRuleAttributeId();
				ruleAttributeValue.setRuleId(ruleId);
				ruleAttributeValue.setRuleAttributeId(ruleAttributeId);
				ruleAttributeValue.setLocationId(locationId);
				ruleAttributeValue.setLocationTagId(locationTagId);
				ruleAttributeValue.setValue(ruleAttributeValueDescriptor.getAttributeValue());
				ruleAttributeValueList.add(ruleAttributeValue);
			}
		}

		return ruleAttributeValueList;
	}

	/**
	 * read the csv file and parse the information of it to a list of RuleAttributeValue objects
	 * @param input an InputStream Object
	 * @return a list of RuleAttributeValue objects
	 * @throws Exception 
	 */
	public static List<RuleAttributeValue> getRuleAttributeValues(InputStream input) throws Exception{
		List<RuleAttributeValue> ruleAttributeValueList = new ArrayList<RuleAttributeValue>();
		List<RuleAttributeValueDescriptor> ruleAttributeValuedList = null;
		LocationService locationService = Context.getLocationService();
		DssService dssService = Context.getService(DssService.class);
		try {
			ruleAttributeValuedList = getRuleAttributeValueDescriptorFromCSV(input);
			
			if (ruleAttributeValuedList != null) {
				for(RuleAttributeValueDescriptor ruleAttributeValued: ruleAttributeValuedList){
					/*create RuleAttributeValue object by RuleAttributeVlaueDescriptor*/
					RuleAttributeValue ruleAttributeValue = new RuleAttributeValue();
					Integer ruleId = dssService.getRule(ruleAttributeValued.getRuleName()).getRuleId();
					Integer locationId = locationService.getLocation(ruleAttributeValued.getLocationName()).getId();
					Integer locationTagId = locationService.getLocationTagByName(ruleAttributeValued.getLocationTagName()).getId();
					Integer ruleAttributeId = dssService.getRuleAttribute(ruleAttributeValued.getAttributeName()).getRuleAttributeId();
					ruleAttributeValue.setRuleId(ruleId);
					ruleAttributeValue.setRuleAttributeId(ruleAttributeId);
					ruleAttributeValue.setLocationId(locationId);
					ruleAttributeValue.setLocationTagId(locationTagId);
					ruleAttributeValue.setValue(ruleAttributeValued.getAttributeValue());
					ruleAttributeValueList.add(ruleAttributeValue);
				}
			}
		}
		catch (Exception e) {
			log.error(e);
			throw e;
		}
		return ruleAttributeValueList;
	}
}
