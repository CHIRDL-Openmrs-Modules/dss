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
import java.util.ArrayList;
import java.util.List;

import org.openmrs.module.chirdlutilbackports.dto.BaseChirdlMetadataDTO;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;


/**
 * DTO class for the RuleEntry class
 * 
 * @author Steve McKee
 */
public class RuleEntryDTO extends BaseChirdlMetadataDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer ruleEntryId;
	private RuleTypeDTO ruleType;
	private RuleDTO rule;
	private Integer priority;
	
	/**
	 * Constructor method
	 */
	public RuleEntryDTO() {
	}
	
	/**
	 * Constructor method
	 * 
	 * @param ruleEntry  RuleEntry used to populate this DTO
	 */
	public RuleEntryDTO(RuleEntry ruleEntry) {
		convertFrom(ruleEntry, this);
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public Integer getId() {
		return getRuleEntryId();
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
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
	public RuleTypeDTO getRuleType() {
		return ruleType;
	}
	
	/**
	 * @param ruleType the ruleType to set
	 */
	public void setRuleType(RuleTypeDTO ruleType) {
		this.ruleType = ruleType;
	}
	
	/**
	 * @return the rule
	 */
	public RuleDTO getRule() {
		return rule;
	}
	
	/**
	 * @param rule the rule to set
	 */
	public void setRule(RuleDTO rule) {
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
	 * Populates the RuleEntryDTO from the RuleEntry.
	 * 
	 * @param source The RuleEntry object
	 * @param destination The RuleEntryDTO object
	 * @return destination The populated RuleEntryDTO object
	 */
	public static RuleEntryDTO convertFrom(RuleEntry source, RuleEntryDTO destination) {
		if (source == null) {
			return null;
		} else if (destination == null) {
			destination = new RuleEntryDTO();
		}
		
		BaseChirdlMetadataDTO.convertFrom(source, destination);
		destination.setRule(RuleDTO.convertFrom(source.getRule(), destination.getRule()));
		destination.setRuleType(RuleTypeDTO.convertFrom(source.getRuleType(), destination.getRuleType()));
		destination.setRuleEntryId(source.getRuleEntryId());
		destination.setPriority(source.getPriority());
		
		return destination;
	}
	
	/**
	 * Converts a List of RuleEntry objects to a list of RuleEntryDTO objects.
	 * 
	 * @param source List of RuleEntry objects
	 * @return List of RuleEntryDTO objects
	 */
	public static List<RuleEntryDTO> convertFrom(List<RuleEntry> source) {
		List<RuleEntryDTO> destination = new ArrayList<>();
		if (source == null) {
			return destination;
		}
		
		for (RuleEntry ruleEntry : source) {
			RuleEntryDTO ruleEntryDTO = new RuleEntryDTO(ruleEntry);
			destination.add(ruleEntryDTO);
		}
		
		return destination;
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
		RuleEntryDTO other = (RuleEntryDTO) obj;
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
		return "RuleEntryDTO [ruleEntryId=" + ruleEntryId + ", ruleType=" + ruleType + ", rule=" + rule + ", priority="
		        + priority + "]";
	}
}
