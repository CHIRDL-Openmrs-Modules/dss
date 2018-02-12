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
import org.openmrs.BaseOpenmrsData;

/**
 * Bean for the dss_rule_type table
 * 
 * @author Steve McKee
 */
public class RuleType extends BaseOpenmrsData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ruleTypeId;
	private String ruleType;
	private String description;
	
	/**
	 * @return the ruleTypeId
	 */
	public Integer getRuleTypeId() {
		return ruleTypeId;
	}
	
	/**
	 * @param ruleTypeId the ruleTypeId to set
	 */
	public void setRuleTypeId(Integer ruleTypeId) {
		this.ruleTypeId = ruleTypeId;
	}

	/**
	 * @return the ruleType
	 */
	public String getRuleType() {
		return ruleType;
	}
	
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	@Override
	public Integer getId() {
		return getRuleTypeId();
	}

	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		setRuleTypeId(id);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((ruleType == null) ? 0 : ruleType.hashCode());
		result = prime * result + ((ruleTypeId == null) ? 0 : ruleTypeId.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		RuleType other = (RuleType) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (ruleType == null) {
			if (other.ruleType != null) {
				return false;
			}
		} else if (!ruleType.equals(other.ruleType)) {
			return false;
		}
		if (ruleTypeId == null) {
			if (other.ruleTypeId != null) {
				return false;
			}
		} else if (!ruleTypeId.equals(other.ruleTypeId))
			return false;
		return true;
	}

	/**
	 * @see org.openmrs.BaseOpenmrsObject#toString()
	 */
	@Override
	public String toString() {
		return "RuleType [ruleTypeId=" + ruleTypeId + ", ruleType=" + ruleType + ", description=" + description + "]";
	}
}
