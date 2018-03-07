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
package org.openmrs.module.dss.hibernateBeans;

import java.io.Serializable;

import org.openmrs.module.chirdlutilbackports.BaseChirdlMetadata;


/**
 * Bean representing the dss_rule_entry table
 * 
 * @author Steve McKee
 */
public class RuleEntry extends BaseChirdlMetadata implements Serializable {
	
	public static final int RULE_PRIORITY_RETIRE = 1000;
	
	private static final long serialVersionUID = 1L;
	
	private Integer ruleEntryId;
	private RuleType ruleType;
	private Rule rule;
	private Integer priority;
	
	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	@Override
	public Integer getId() {
		return getRuleEntryId();
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		setRuleEntryId(id);
	}

	/**
	 * @return the ruleEntryId
	 */
	public Integer getRuleEntryId() {
		return ruleEntryId;
	}
	
	/**
	 * @param ruleEntryId the ruleEntryId to set
	 */
	public void setRuleEntryId(Integer ruleEntryId) {
		this.ruleEntryId = ruleEntryId;
	}
	
	/**
	 * @return the ruleType
	 */
	public RuleType getRuleType() {
		return ruleType;
	}
	
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
	
	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
	}
	
	/**
	 * @param rule the rule to set
	 */
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	
	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		result = prime * result + ((ruleEntryId == null) ? 0 : ruleEntryId.hashCode());
		result = prime * result + ((ruleType == null) ? 0 : ruleType.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleEntry other = (RuleEntry) obj;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		if (ruleEntryId == null) {
			if (other.ruleEntryId != null)
				return false;
		} else if (!ruleEntryId.equals(other.ruleEntryId))
			return false;
		if (ruleType == null) {
			if (other.ruleType != null)
				return false;
		} else if (!ruleType.equals(other.ruleType))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleEntry [ruleEntryId=" + ruleEntryId + ", ruleType=" + ruleType + ", rule=" + rule + ", priority="
		        + priority + "]";
	}
}
