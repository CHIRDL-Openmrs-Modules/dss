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
import java.util.Date;

import org.openmrs.Auditable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Retireable;
import org.openmrs.User;


/**
 * Bean representing the dss_rule_entry table
 * 
 * @author Steve McKee
 */
public class RuleEntry extends BaseOpenmrsObject implements Auditable, Retireable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer ruleEntryId;
	private RuleType ruleType;
	private Rule rule;
	private Integer priority;
	private Boolean retired = Boolean.FALSE;
	private User retiredBy;
	private Date dateRetired;
	private String retireReason;
	private User creator;
	private Date dateCreated;
	private User changedBy;
	private Date dateChanged;
	
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
	 * @see org.openmrs.Retireable#isRetired()
	 */
	public Boolean isRetired() {
		return retired;
	}

	/**
	 * @see org.openmrs.Retireable#getRetired()
	 */
	public Boolean getRetired() {
		return retired;
	}

	/**
	 * @see org.openmrs.Retireable#setRetired(java.lang.Boolean)
	 */
	public void setRetired(Boolean retired) {
		this.retired = retired;
	}

	/**
	 * @see org.openmrs.Retireable#getRetiredBy()
	 */
	public User getRetiredBy() {
		return retiredBy;
	}

	/**
	 * @see org.openmrs.Retireable#setRetiredBy(org.openmrs.User)
	 */
	public void setRetiredBy(User retiredBy) {
		this.retiredBy = retiredBy;
	}

	/**
	 * @see org.openmrs.Retireable#getDateRetired()
	 */
	public Date getDateRetired() {
		return dateRetired;
	}

	/**
	 * @see org.openmrs.Retireable#setDateRetired(java.util.Date)
	 */
	public void setDateRetired(Date dateRetired) {
		this.dateRetired = dateRetired;
	}
	
	/**
	 * @see org.openmrs.Retireable#getRetireReason()
	 */
	public String getRetireReason() {
		return retireReason;
	}

	/**
	 * @see org.openmrs.Retireable#setRetireReason(java.lang.String)
	 */
	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}

	/**
	 * @see org.openmrs.Auditable#getCreator()
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @see org.openmrs.Auditable#setCreator(org.openmrs.User)
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @see org.openmrs.Auditable#getDateCreated()
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @see org.openmrs.Auditable#setDateCreated(java.util.Date)
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @see org.openmrs.Auditable#getChangedBy()
	 */
	public User getChangedBy() {
		return changedBy;
	}

	/**
	 * @see org.openmrs.Auditable#setChangedBy(org.openmrs.User)
	 */
	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * @see org.openmrs.Auditable#getDateChanged()
	 */
	public Date getDateChanged() {
		return dateChanged;
	}

	/**
	 * @see org.openmrs.Auditable#setDateChanged(java.util.Date)
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((changedBy == null) ? 0 : changedBy.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((dateChanged == null) ? 0 : dateChanged.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateRetired == null) ? 0 : dateRetired.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((retireReason == null) ? 0 : retireReason.hashCode());
		result = prime * result + ((retired == null) ? 0 : retired.hashCode());
		result = prime * result + ((retiredBy == null) ? 0 : retiredBy.hashCode());
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
		if (changedBy == null) {
			if (other.changedBy != null)
				return false;
		} else if (!changedBy.equals(other.changedBy))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (dateChanged == null) {
			if (other.dateChanged != null)
				return false;
		} else if (!dateChanged.equals(other.dateChanged))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateRetired == null) {
			if (other.dateRetired != null)
				return false;
		} else if (!dateRetired.equals(other.dateRetired))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (retireReason == null) {
			if (other.retireReason != null)
				return false;
		} else if (!retireReason.equals(other.retireReason))
			return false;
		if (retired == null) {
			if (other.retired != null)
				return false;
		} else if (!retired.equals(other.retired))
			return false;
		if (retiredBy == null) {
			if (other.retiredBy != null)
				return false;
		} else if (!retiredBy.equals(other.retiredBy))
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
		        + priority + ", retired=" + retired + ", retiredBy=" + retiredBy + ", dateRetired=" + dateRetired
		        + ", retireReason=" + retireReason + ", creator=" + creator + ", dateCreated=" + dateCreated + ", changedBy="
		        + changedBy + ", dateChanged=" + dateChanged + "]";
	}
}
