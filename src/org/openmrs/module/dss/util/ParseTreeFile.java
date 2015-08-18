package org.openmrs.module.dss.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import org.openmrs.api.context.Context;
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
	public static void parseTree() {
		
		String treeFile = "C:\\Users\\tmdugan\\git\\Obesity_Prediction\\src\\util\\resources\\ID3_small_model.txt";
		final String DELIMITER = "|  ";
		Node root = new Node();
		Tree tree = new Tree(root);
		BufferedReader reader = null;
		int prevLevel = -1;
		Node parentNode = root;
		DssService dssService = Context.getService(DssService.class);
		try {
			reader = new BufferedReader(new FileReader(treeFile));
			
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
		
		HashMap<Integer, Set<String>> ruleLogicMap = new HashMap<Integer, Set<String>>();
		HashMap<Integer, Set<String>> ruleVariableMap = new HashMap<Integer, Set<String>>();
		
		tree.getRoot().traverseBreadthFirst("associated_answer", ruleLogicMap, ruleVariableMap);
		Integer priority = 1;
		
		for (Integer ruleId : ruleLogicMap.keySet()) {
			Rule rule = setupRule();
			rule.setRuleId(ruleId);
			rule.setPriority(priority);
			rule.setTokenName("obesityScreener" + priority);
			Set<String> ifStatements = ruleLogicMap.get(ruleId);
			Set<String> variables = ruleVariableMap.get(ruleId);
			String logic = "If (mode = PRODUCE) then \n";
			Rule psfRule = dssService.getRule(ruleId);
			logic += "psfResult:= call " + psfRule.getTokenName() + ";\n";

			for (String ifStatement : ifStatements) {
				logic += ifStatement + "\n";
			}
			logic += "endif\n";
			rule.setLogic(logic);
			
			String data = "mode:=read {mode from Parameters};\n";
			
			for (String variable : variables) {
				//not sure if this is quite accurate
				//model used most common attribute but this is pulling most recent
				data += variable + " := read Last {" + variable + " from CHICA};\n";
			}
			rule.setData(data);
			String outputDirectory = "C:\\Users\\tmdugan\\git\\Obesity_Prediction\\src\\util\\resources\\";
			String filename = outputDirectory + rule.getTokenName() + ".mlm";
			writeFile(filename, rule);
			
			priority++;
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
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ruleCreationDate = formater.format(new Date());
		rule.setRuleCreationDate(ruleCreationDate);
		return rule;
	}
	
}
