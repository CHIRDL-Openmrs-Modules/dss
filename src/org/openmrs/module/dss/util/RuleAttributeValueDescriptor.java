package org.openmrs.module.dss.util;

/**
 * 
 * @author tmdugan
 * rule attribute value information
 */
public class RuleAttributeValueDescriptor {
	private String ruleName;
	private String locationName;
	private String locationTagName;
	private String attributeName;
	private String attributeValue;
	
	/**
	 * Constructor method
	 * create an empty RuleAttributeValueDescriptor
	 */
	public RuleAttributeValueDescriptor(){
		
	}
	
	/**
	 * Constructor method
	 * create an RuleAttributeValueDescriptor with rule name, location name, location tag name, attribute name, attribute value
	 * @param ruleName rule name
	 * @param locationName location name
	 * @param locationTagName location tag name
	 * @param attributeName attribute name
	 * @param attributeValue attribute value
	 */
	public RuleAttributeValueDescriptor(String ruleName, String locationName, String locationTagName, String attributeName, String attributeValue) {
		this.ruleName = ruleName;
		this.locationName = locationName;
		this.locationTagName = locationTagName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	
	
    /**
     * @return the ruleName
     */
    public String getRuleName() {
    	return ruleName;
    }

	
    /**
     * @param ruleName the ruleName to set
     */
    public void setRuleName(String ruleName) {
    	this.ruleName = ruleName;
    }

	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationTagName() {
		return locationTagName;
	}
	public void setLocationTagName(String locationTagName) {
		this.locationTagName = locationTagName;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((ruleName == null) ? 0 : ruleName.hashCode());
		result = prime * result + ((locationName == null) ? 0 : locationName.hashCode());
		result = prime * result + ((locationTagName == null) ? 0 : locationTagName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleAttributeValueDescriptor other = (RuleAttributeValueDescriptor) obj;
		if (attributeName == null) {
			if (other.ruleName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (attributeValue == null) {
			if (other.attributeValue != null)
				return false;
		} else if (!attributeValue.equals(other.attributeValue))
			return false;
		if (ruleName == null) {
			if (other.ruleName != null)
				return false;
		} else if (!ruleName.equals(other.ruleName))
			return false;
		if (locationName == null) {
			if (other.locationName != null)
				return false;
		} else if (!locationName.equals(other.locationName))
			return false;
		if (locationTagName == null) {
			if (other.locationTagName != null)
				return false;
		} else if (!locationTagName.equals(other.locationTagName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RuleAttributeValueDescriptor [ruleName=" + ruleName + ", locationName=" + locationName + ", locationTagName=" + locationTagName + ", attributeName=" + attributeName + ", attributeValue=" + attributeValue + "]";
	}
	
	

	
	
}
