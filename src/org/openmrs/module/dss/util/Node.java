package org.openmrs.module.dss.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
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
public class Node {
	
	private String name = null;
	
	private String value = null;
	
	private Node parent = null;
	
	private List<Node> children = null;
	
	public Node() {
		this.children = new ArrayList<Node>();
	}
	
	public Node(String name, String value) {
		this();
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @return the name
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the value
	 */
	public String value() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Integer size() {
		return this.children.size();
	}
	
	/**
	 * @return the children
	 */
	public List<Node> children() {
		return children;
	}
	
	public void addChild(Node node) {
		this.children.add(node);
		node.setParent(this);
	}
	
	@Override
	public String toString() {
		
		return toString(-1);
	}
	
	private String toString(int level) {
		
		StringBuffer text = new StringBuffer();
		Integer size = this.size();
		text.append("\n");
		for (int i = 0; i < level; i++) {
			text.append("|  ");
		}
		if (name() != null) {
			text.append(name() + " = " + value());
		}
		for (int j = 0; j < size; j++) {
			text.append(this.children.get(j).toString(level + 1));
		}
		
		return text.toString();
	}
	
	public void traverseBreadthFirst(String ruleAttributeName, HashMap<Integer, Set<String>> ruleLogicMap,
	                                 HashMap<Integer, Set<String>> ruleVariableMap) {
		
		//A rule will only be created if the given attribute has a mapping in the rule attribute value table (ie it has an associated PSF question)
		//The maps gather if statements and variables that will be contained in the same rule
		
		String ruleAttributeLookupValue = name() + " = " + value();
		
		if (ruleAttributeLookupValue.contains(":")) {
			ruleAttributeLookupValue = ruleAttributeLookupValue.substring(0, ruleAttributeLookupValue.indexOf(":"));
		}
		DssService dssService = Context.getService(DssService.class);
		RuleAttribute ruleAttribute = dssService.getRuleAttribute(ruleAttributeName);
		Integer ruleAttributeId = ruleAttribute.getRuleAttributeId();
		List<RuleAttributeValue> ruleAttributeValues = dssService.getRuleAttributesByValue(ruleAttributeId,
		    ruleAttributeLookupValue);
		
		if (ruleAttributeValues != null && ruleAttributeValues.size() > 0) {
			for (RuleAttributeValue ruleAttributeValue : ruleAttributeValues) {
				Integer ruleId = ruleAttributeValue.getRuleId();
				Set<String> ifStatements = ruleLogicMap.get(ruleId);
				Set<String> variables = ruleVariableMap.get(ruleId);
				if (ifStatements == null) {
					ifStatements = new HashSet<String>();
				}
				if (variables == null) {
					variables = new HashSet<String>();
				}
				ifStatements.add(buildIfStatement(variables));
				ruleLogicMap.put(ruleId, ifStatements);
				ruleVariableMap.put(ruleId, variables);
			}
		}
		Integer size = size();
		
		for (int j = 0; j < size; j++) {
			this.children.get(j).traverseBreadthFirst(ruleAttributeName, ruleLogicMap, ruleVariableMap);
		}
		
	}

	private String buildIfStatement(Set<String> variables) {
		Node currParent = this.parent;
		StringBuffer buffer = new StringBuffer();
		
		while (currParent != null&&currParent.name() != null) {
			if(buffer.length()>0){
				buffer.append(" AND ");
			}else{
				buffer.append("If");
			}
			variables.add(currParent.name());
			buffer.append("("+currParent.name()+" = "+currParent.value()+")");
			currParent = currParent.getParent();
		}
		if(buffer.length()>0){
			buffer.append(" then conclude true;");
		}
		return buffer.toString();
	}
}
