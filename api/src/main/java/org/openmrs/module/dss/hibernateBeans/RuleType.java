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

import org.openmrs.BaseOpenmrsMetadata;

/**
 * Bean for the dss_rule_type table
 * 
 * @author Steve McKee
 */
public class RuleType extends BaseOpenmrsMetadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ruleTypeId;
	
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
		return "RuleType [ruleTypeId=" + ruleTypeId + "]";
	}
}
