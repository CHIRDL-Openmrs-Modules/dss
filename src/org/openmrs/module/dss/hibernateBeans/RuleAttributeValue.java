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
	private Integer locationId = null;
	private Integer locationTagId = null;
	
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
	
    /**
     * @return the locationId
     */
    public Integer getLocationId() {
    	return locationId;
    }
	
    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(Integer locationId) {
    	this.locationId = locationId;
    }
	
    /**
     * @return the locationTagId
     */
    public Integer getLocationTagId() {
    	return locationTagId;
    }
	
    /**
     * @param locationTagId the locationTagId to set
     */
    public void setLocationTagId(Integer locationTagId) {
    	this.locationTagId = locationTagId;
    }
}