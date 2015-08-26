package org.openmrs.module.dss.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.openmrs.api.context.Context;
import org.openmrs.logic.result.Result;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.service.DssService;

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

/**
 *
 */
public class ParseTreeFile {
	
	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void parseTree(InputStream input, String outputDirectory) {
		
		Node root = new Node();
		buildTree(input, root);
		
		DssService dssService = Context.getService(DssService.class);
		
		HashMap<Integer, Set<String>> ruleLogicMap = new HashMap<Integer, Set<String>>();
		HashMap<Integer, Set<String>> ruleVariableMap = new HashMap<Integer, Set<String>>();
		HashMap<String, Set<String>> leafLogicMap = new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> leafVariableMap = new HashMap<String, Set<String>>();
		HashMap<Integer, Set<String>> ruleAttributeMap = new HashMap<Integer, Set<String>>();
		ArrayList<String> noDataVariables = new ArrayList<String>();
		noDataVariables.add("gender");
		noDataVariables.add("race");
		noDataVariables.add("OBESE_BEFORE_2mo");
		noDataVariables.add("OBESE_BEFORE_6mo");
		noDataVariables.add("OBESE_BEFORE_12mo");
		noDataVariables.add("OBESE_BEFORE_18mo");
		noDataVariables.add("OBESE_BEFORE_24mo");
		noDataVariables.add("OVERWEIGHT_BEFORE_2mo");
		noDataVariables.add("OVERWEIGHT_BEFORE_6mo");
		noDataVariables.add("OVERWEIGHT_BEFORE_12mo");
		noDataVariables.add("OVERWEIGHT_BEFORE_18mo");
		noDataVariables.add("OVERWEIGHT_BEFORE_24mo");
		noDataVariables.add("VERY_TALL_BEFORE_2mo");
		noDataVariables.add("VERY_TALL_BEFORE_6mo");
		noDataVariables.add("VERY_TALL_BEFORE_12mo");
		noDataVariables.add("VERY_TALL_BEFORE_18mo");
		noDataVariables.add("VERY_TALL_BEFORE_24mo");
		noDataVariables.add("TALL_BEFORE_2mo");
		noDataVariables.add("TALL_BEFORE_6mo");
		noDataVariables.add("TALL_BEFORE_12mo");
		noDataVariables.add("TALL_BEFORE_18mo");
		noDataVariables.add("TALL_BEFORE_24mo");
		
		root.traverseBreadthFirst("associated_answer", ruleLogicMap, ruleVariableMap, leafLogicMap, leafVariableMap,
		    ruleAttributeMap);
		
		Integer priority = 1;
		
		//Create the rules for the questions
		for (Integer ruleId : ruleLogicMap.keySet()) {
			Rule rule = setupRule();
			rule.setRuleId(ruleId);
			rule.setPriority(priority);
			rule.setTokenName("obesityScreener" + priority);
			Set<String> ifStatements = ruleLogicMap.get(ruleId);
			Set<String> variables = ruleVariableMap.get(ruleId);
			Set<String> attributes = ruleAttributeMap.get(ruleId);
			String logic = "If (mode = PRODUCE) then \n";
			Rule psfRule = dssService.getRule(ruleId);
			
			logic += "psfResult:= call " + psfRule.getTokenName() + ";\n";
			logic = readVariables(logic, variables, noDataVariables);
			for (String attribute : attributes) {
				logic += "If NOT(" + Node.formatVariableName(attribute) + " = NULL) then conclude false;\n";
			}
			for (String ifStatement : ifStatements) {
				logic += ifStatement + "\n";
			}
			logic += "endif\n";
			rule.setLogic(logic);
			
			String filename = outputDirectory + rule.getTokenName() + ".mlm";
			writeFile(filename, rule);
			
			priority++;
		}
		
		Integer counter = 1;
		
		//Create the scoring rules
		//The scoring rules need to be split into different files because
		//they generate java files that are too large to compile
		final int NUM_IF_STATEMENTS = 40;
		
		//we don't need scoring rules for null
		leafLogicMap.remove("null");
		
		for (String obesityClassifer : leafLogicMap.keySet()) {
			int ifIndex = 0;
			
			Rule rule = setupRule();
			rule.setAction(null);
			rule.setTokenName("obesityScoring" + counter);
			rule.setPurpose("Scoring rule for obesity prediction screener");
			rule.setExplanation("This rule set determines obesity prediction classification.");
			rule.setKeywords("obesity");
			rule.setTitle("Obesity scoring rule");
			
			Set<String> ifStatements = leafLogicMap.get(obesityClassifer);
			Set<String> variables = leafVariableMap.get(obesityClassifer);
			Object[] ifStatementArray = ifStatements.toArray();
			
			//break up the file by number of if statements
			while (ifIndex < ifStatementArray.length) {
				rule.setTokenName("obesityScoring" + counter);
				String logic = "If (mode = CONSUME) then \n";
				logic = readVariables(logic, variables, noDataVariables);
				for (int i = 0; i < NUM_IF_STATEMENTS && ifIndex < ifStatementArray.length; i++, ifIndex++) {
					String ifStatement = (String) ifStatementArray[ifIndex];
					logic += ifStatement + "\n";
				}
				logic += "endif\n";
				rule.setLogic(logic);
				
				String filename = outputDirectory + rule.getTokenName() + ".mlm";
				writeFile(filename, rule);
				counter++;
			}
		}
		
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private static String readVariables(String oldLogic, Set<String> variables, ArrayList<String> noDataVariables) {
		String logic = oldLogic;
		
		logic += "race:= call getRace;\n";
		
		for (String variable : variables) {
			String variableName = Node.formatVariableName(variable);
			if (!variableName.equalsIgnoreCase("race") && !variableName.equalsIgnoreCase("gender")) {
				if (noDataVariables.contains(variableName)) {
					
					logic += variableName + ":= call getObsByConceptAgeThreshold with ";
					
					if (variableName.contains("OBESE")) {
						logic += "\"WTCENTILE\", \"95\"";
					} else if (variableName.contains("OVERWEIGHT")) {
						logic += "\"WTCENTILE\", \"85\"";
					} else if (variableName.contains("VERY_TALL")) {
						logic += "\"HTCENTILE\", \"95\"";
					} else if (variableName.contains("TALL")) {
						logic += "\"HTCENTILE\", \"85\"";
					}
					
					if (variableName.contains("12mo")) {
						logic += ", \"12\", \"" + org.openmrs.module.chirdlutil.util.Util.MONTH_ABBR + "\"";
					} else if (variableName.contains("6mo")) {
						logic += ", \"6\", \"" + org.openmrs.module.chirdlutil.util.Util.MONTH_ABBR + "\"";
					} else if (variableName.contains("2mo")) {
						logic += ", \"2\", \"" + org.openmrs.module.chirdlutil.util.Util.MONTH_ABBR + "\"";
					} else if (variableName.contains("18mo")) {
						logic += ", \"18\", \"" + org.openmrs.module.chirdlutil.util.Util.MONTH_ABBR + "\"";
					} else if (variableName.contains("24mo")) {
						logic += ", \"24\", \"" + org.openmrs.module.chirdlutil.util.Util.MONTH_ABBR + "\"";
					}
					
					logic += ";\n";
					
				} else {
					logic += variableName + ":= call getMostCommonObs with \"" + variableName + "\", \"2\", \""
					        + org.openmrs.module.chirdlutil.util.Util.YEAR_ABBR + "\";\n";
				}
			}
		}
		return logic;
	}
	
	private static void buildTree(InputStream input, Node root) {
		BufferedReader reader = null;
		final String DELIMITER = "|  ";
		int prevLevel = -1;
		Node parentNode = root;
		
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			
			String line = reader.readLine();
			while (line != null) {
				int level = org.apache.commons.lang.StringUtils.countMatches(line, DELIMITER);
				String strippedLine = line.replaceAll(DELIMITER.replaceAll("|", "\\\\|"), "").trim();
				StringTokenizer tokenizer = new StringTokenizer(strippedLine, "=");
				String attribute = null;
				String value = null;
				
				if (tokenizer.hasMoreElements()) {
					attribute = tokenizer.nextToken().trim();
				}
				if (tokenizer.hasMoreElements()) {
					value = tokenizer.nextToken().trim();
				}
				
				if (value.equals("MISSING")) {
					value = "null";
				}
				
				Node node = new Node(attribute, value);
				
				if (level > prevLevel) {
					parentNode.addChild(node);
				} else {
					
					while (level < prevLevel && parentNode != null) {
						parentNode = parentNode.getParent();
						prevLevel--;
					}
					
					if (parentNode != null) {
						parentNode = parentNode.getParent();
						if (parentNode != null) {
							parentNode.addChild(node);
						}
					}
				}
				
				parentNode = node;
				prevLevel = level;
				line = reader.readLine();
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}
			catch (Exception e) {}
		}
	}
	
	private static void writeFile(String filename, Rule rule) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename);
			fileWriter.write(rule.getMLMString());
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static Rule setupRule() {
		Rule rule = new Rule();
		rule.setAgeMax(21);
		rule.setAgeMaxUnits("years");
		rule.setAgeMin(2);
		rule.setAgeMinUnits("years");
		rule.setAuthor("Tammy Dugan");
		rule.setCitations("http://dx.doi.org/10.4338/ACI-2015-03-RA-0036");
		rule.setPurpose("PSF question to collect data for obesity prediction");
		rule.setExplanation("This rule set collects information to predict childhood obesity");
		rule.setVersion(1.0);
		rule.setSpecialist("Pediatrics");
		rule.setKeywords("PSF, obesity");
		rule.setInstitution("Indiana University School of Medicine");
		rule.setTitle("Obesity screening question");
		rule.setAction("write (\"|| psfResult ||\");\n");
		String data = "mode:=read {mode from Parameters};\n";
		rule.setData(data);
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ruleCreationDate = formater.format(new Date());
		rule.setRuleCreationDate(ruleCreationDate);
		return rule;
	}
	
	private void updateParameters(Map<String, Object> parameters, HashMap<String, String> userVarMap,
	                              HashMap<String, Result> resultLookup, String parameterName, String parameterValue) {
		int varLen = 0;
		String variable = null;
		varLen = parameterValue.length();
		Object value = userVarMap.get(parameterValue);
		if (value != null) {
			parameters.put(parameterName, value);
		}
		// It must be a result value or date
		else if (parameterValue.endsWith("_value")) {
			variable = parameterValue.substring(0, varLen - 6); // -6 for _value
			if (resultLookup.get(variable) != null) {
				value = resultLookup.get(variable).toString();
			}
		} else if (parameterValue.endsWith("_date")) {
			variable = parameterValue.substring(0, varLen - 5); // -5 for _date
			if (resultLookup.get(variable) != null) {
				value = resultLookup.get(variable).getResultDate().toString();
			}
		} else if (parameterValue.endsWith("_object")) {
			variable = parameterValue.substring(0, varLen - 7); // -7 for _object
			if (resultLookup.get(variable) != null) {
				value = resultLookup.get(variable);
			}
		} else {
			if (resultLookup.get(parameterValue) != null) {
				value = resultLookup.get(parameterValue).toString();
			}
		}
		if (value != null) {
			parameters.put(parameterName, value);
		} else {
			parameters.put(parameterName, parameterValue);
		}
	}
}
