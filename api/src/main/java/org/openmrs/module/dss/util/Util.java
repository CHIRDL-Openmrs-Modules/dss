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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.service.DssService;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;


/**
 *
 */
public class Util {
	
	private static final Log LOG = LogFactory.getLog(Util.class);
	
	public Util(){
		//empty constructor
	}
	
	/**
	 * read csv input stream and create RuleAttributeValue objects according to it.
	 * @param input the inputStream including the data source
	 * @return a list of new created RuleAttributeValueDescriptor objects.
	 * @throws Exception 
	 */
	public static List<RuleAttributeValueDescriptor> getRuleAttributeValueDescriptorFromCSV(InputStream input) throws Exception{
		List<RuleAttributeValueDescriptor> ruleAttributeValuedList = null;
		InputStreamReader inStreamReader = null;
		CSVReader reader = null;
		try{
			inStreamReader = new InputStreamReader(input);
			reader = new CSVReader(inStreamReader, ',');
			HeaderColumnNameTranslateMappingStrategy<RuleAttributeValueDescriptor> strat = new HeaderColumnNameTranslateMappingStrategy<RuleAttributeValueDescriptor>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("rule_name", "ruleName");
			map.put("attribute_name", "attributeName");
			map.put("attribute_value", "attributeValue");
			strat.setType(RuleAttributeValueDescriptor.class);
			strat.setColumnMapping(map);
			CsvToBean<RuleAttributeValueDescriptor> csv = new CsvToBean<RuleAttributeValueDescriptor>();
			ruleAttributeValuedList = csv.parse(strat, reader);
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
			throw e;
		}finally{
			if(inStreamReader != null){
				inStreamReader.close();
			}
			if(reader != null){
				reader.close();
			}
		}
		return ruleAttributeValuedList;
	}
	
	/**
	 * Create RuleAttributeValue object from a RuleAttributeValueDescriptor object
	 * @param ruleAttributeValuedList data source
	 * @return a list of new created RuleAttributeValue objects
	 */
	public static List<RuleAttributeValue> getRuleAttributeValues(InputStream input) {
		List<RuleAttributeValue> ruleAttributeValueList = new ArrayList<RuleAttributeValue>();
		DssService dssService = Context.getService(DssService.class);
		List<RuleAttributeValueDescriptor> ruleAttributeValueDescriptors = null;
        try {
	        ruleAttributeValueDescriptors = getRuleAttributeValueDescriptorFromCSV(input);
        }catch (Exception e) {
        	LOG.error("Could not parse rule attribute descriptors", e);
        }
		if (ruleAttributeValueDescriptors != null) {
			for (RuleAttributeValueDescriptor ruleAttributeValueDescriptor : ruleAttributeValueDescriptors) {
				/*
				 * create RuleAttributeValue object by
				 * RuleAttributeVlaueDescriptor
				 */
				RuleAttributeValue ruleAttributeValue = new RuleAttributeValue();
				Rule rule = dssService.getRule(ruleAttributeValueDescriptor.getRuleName());
				if(rule==null){
					continue;
				}
				Integer ruleId = rule.getRuleId();
				RuleAttribute ruleAttribute = dssService.getRuleAttribute(ruleAttributeValueDescriptor.getAttributeName());
				if(ruleAttribute==null){
					continue;
				}
				Integer ruleAttributeId = ruleAttribute.getRuleAttributeId();
				ruleAttributeValue.setRuleId(ruleId);
				ruleAttributeValue.setRuleAttributeId(ruleAttributeId);
				ruleAttributeValue.setValue(ruleAttributeValueDescriptor.getAttributeValue());
				ruleAttributeValue.setCreationTime(Calendar.getInstance().getTime());
				ruleAttributeValueList.add(ruleAttributeValue);
			}
		}

		return ruleAttributeValueList;
	}

}
