package org.openmrs.module.dss.hibernateBeans;

import java.util.Date;

/**
 * Holds information to store in the dss_rule_attribute_value table
 * 
 * @author Tammy Dugan
 */
public class RuleAttributeValue implements java.io.Serializable
{

	// Fields
	private Integer ruleAttributeValueId = null;
	private Integer ruleAttributeId = null;
	private Integer ruleId = null;
	private String value = null;
	private Date creationTime = null;
	private Date lastModified = null;
	
    /**
     * @return the ruleAttributeValueId
     */
    public Integer getRuleAttributeValueId() {
    	return ruleAttributeValueId;
    }
	
    /**
     * @param ruleAttributeValueId the ruleAttributeValueId to set
     */
    public void setRuleAttributeValueId(Integer ruleAttributeValueId) {
    	this.ruleAttributeValueId = ruleAttributeValueId;
    }
	
    /**
     * @return the ruleAttributeId
     */
    public Integer getRuleAttributeId() {
    	return ruleAttributeId;
    }
	
    /**
     * @param ruleAttributeId the ruleAttributeId to set
     */
    public void setRuleAttributeId(Integer ruleAttributeId) {
    	this.ruleAttributeId = ruleAttributeId;
    }
	
    /**
     * @return the ruleId
     */
    public Integer getRuleId() {
    	return ruleId;
    }
	
    /**
     * @param ruleId the ruleId to set
     */
    public void setRuleId(Integer ruleId) {
    	this.ruleId = ruleId;
    }
	
    /**
     * @return the value
     */
    public String getValue() {
    	return value;
    }
	
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
    	this.value = value;
    }
	
    /**
     * @return the creationTime
     */
    public Date getCreationTime() {
    	return creationTime;
    }
	
    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(Date creationTime) {
    	this.creationTime = creationTime;
    }
	
    /**
     * @return the lastModified
     */
    public Date getLastModified() {
    	return lastModified;
    }
	
    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(Date lastModified) {
    	this.lastModified = lastModified;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ruleAttributeId == null) ? 0 : ruleAttributeId.hashCode());
		result = prime * result + ((ruleAttributeValueId == null) ? 0 : ruleAttributeValueId.hashCode());
		result = prime * result + ((ruleId == null) ? 0 : ruleId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obs)
	{
		if (obs == null || !(obs instanceof RuleAttributeValue))
		{
			return false;
		}

		RuleAttributeValue ruleAttributeValue = (RuleAttributeValue) obs;

		if (this.ruleAttributeId.equals(ruleAttributeValue.getRuleAttributeId()))
		{
			if (this.ruleId.equals(ruleAttributeValue.getRuleId()))
			{
				if(this.value.equals(ruleAttributeValue.getValue())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "RuleAttributeValue [ruleAttributeId=" + ruleAttributeId + ", ruleId="+ruleId+ ", ruleAttributeValueId=" + ruleAttributeValueId  + ", value=" + value + ", creationTime="+creationTime+ ", lastModified="+ lastModified+"]";
	}
}