package org.openmrs.module.dss.hibernateBeans;


/**
 * Holds information to store in the dss_rule_attribute table
 * 
 * @author Tammy Dugan
 */
public class RuleAttribute implements java.io.Serializable
{

	// Fields
	private Integer ruleAttributeId = null;
	private String name = null;
	private String description = null;
	
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
     * @return the name
     */
    public String getName() {
    	return name;
    }
	
    /**
     * @param name the name to set
     */
    public void setName(String name) {
    	this.name = name;
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

	
}