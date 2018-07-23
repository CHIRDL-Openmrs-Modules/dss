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
package org.openmrs.module.dss.hibernateBeans.dto;

import java.io.Serializable;

import org.openmrs.module.chirdlutilbackports.dto.BaseOpenmrsMetadataDTO;
import org.openmrs.module.dss.hibernateBeans.RuleType;

/**
 * DTO class for the RuleType class
 * 
 * @author Steve McKee
 */
public class RuleTypeDTO extends BaseOpenmrsMetadataDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ruleTypeId;
	
	/**
	 * Constructor method
	 */
	public RuleTypeDTO() {
	}
	
	/**
	 * Constructor method
	 * 
	 * @param ruleType RuleType used to populate this DTO
	 */
	public RuleTypeDTO(RuleType ruleType) {
		convertFrom(ruleType, this);
	}
	
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
	public Integer getId() {
		return getRuleTypeId();
	}

	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		setRuleTypeId(id);
	}
	
	/**
	 * Populates the RuleTypeDTO from the RuleType.
	 * 
	 * @param source The RuleType object
	 * @param destination The RuleTypeDTO object
	 * @return destination The populated RuleTypeDTO object
	 */
	public static RuleTypeDTO convertFrom(RuleType source, RuleTypeDTO destination) {
		if (source == null) {
			return null;
		} else if (destination == null) {
			destination = new RuleTypeDTO();
		}
		
		BaseOpenmrsMetadataDTO.convertFrom(source, destination);
		destination.setRuleTypeId(source.getRuleTypeId());
		
		return destination;
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
		RuleTypeDTO other = (RuleTypeDTO) obj;
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
		return "RuleTypeDTO [ruleTypeId=" + ruleTypeId + "]";
	}
}
