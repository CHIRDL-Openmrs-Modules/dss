package org.openmrs.module.dss.hibernateBeans;

import java.util.Date;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.module.chirdlutil.util.Util;

/**
 * Holds information to store in the dss_rule table
 * 
 * @author Tammy Dugan
 */
public class Rule implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	// Fields
	private Integer ruleId = null;
	private String classFilename = null;
	private Date creationTime = null;
	private String title = null;
	private Double version = null;
	private String institution = null;
	private String author = null;
	private String specialist = null;
	private String ruleCreationDate = null;
	private String purpose = null;
	private String explanation = null;
	private String keywords = null;
	private String citations = null;
	private String links = null;
	private String data = null;
	private String logic = null;
	private String action = null;
	private Date lastModified = null;
	private String result = null; // result of executing the rule
	private String tokenName = null;
	private Map<String, Object> parameters = null; // parameters for rule
													// evaluation
	private String ageMinUnits = null;
	private String ageMaxUnits = null;
	private Integer ageMin = null;
	private Integer ageMax = null;

	/**
	 * @return the ruleId
	 */
	public Integer getRuleId()
	{
		return this.ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(Integer ruleId)
	{
		this.ruleId = ruleId;
	}

	/**
	 * @return the classFilename
	 */
	public String getClassFilename()
	{
		return this.classFilename;
	}

	/**
	 * @param classFilename the classFilename to set
	 */
	public void setClassFilename(String classFilename)
	{
		this.classFilename = classFilename;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime()
	{
		return this.creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the version
	 */
	public Double getVersion()
	{
		return this.version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Double version)
	{
		this.version = version;
	}

	/**
	 * @return the institution
	 */
	public String getInstitution()
	{
		return this.institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(String institution)
	{
		this.institution = institution;
	}

	/**
	 * @return the author
	 */
	public String getAuthor()
	{
		return this.author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * @return the specialist
	 */
	public String getSpecialist()
	{
		return this.specialist;
	}

	/**
	 * @param specialist the specialist to set
	 */
	public void setSpecialist(String specialist)
	{
		this.specialist = specialist;
	}

	/**
	 * @return the ruleCreationDate
	 */
	public String getRuleCreationDate()
	{
		return this.ruleCreationDate;
	}

	/**
	 * @param ruleCreationDate the ruleCreationDate to set
	 */
	public void setRuleCreationDate(String ruleCreationDate)
	{
		this.ruleCreationDate = ruleCreationDate;
	}

	/**
	 * @return the purpose
	 */
	public String getPurpose()
	{
		return this.purpose;
	}

	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose)
	{
		this.purpose = purpose;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation()
	{
		return this.explanation;
	}

	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation)
	{
		this.explanation = explanation;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords()
	{
		return this.keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	/**
	 * @return the citations
	 */
	public String getCitations()
	{
		return this.citations;
	}

	/**
	 * @param citations the citations to set
	 */
	public void setCitations(String citations)
	{
		this.citations = citations;
	}

	/**
	 * @return the links
	 */
	public String getLinks()
	{
		return this.links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(String links)
	{
		this.links = links;
	}

	/**
	 * @return the data
	 */
	public String getData()
	{
		return this.data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * @return the logic
	 */
	public String getLogic()
	{
		return this.logic;
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(String logic)
	{
		this.logic = logic;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return this.action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action)
	{
		this.action = action;
	}

	/**
	 * @return the lastModified
	 */
	public Date getLastModified()
	{
		return this.lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}

	/**
	 * @return the result
	 */
	public String getResult()
	{
		return this.result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result)
	{
		this.result = result;
	}

	/**
	 * @return the tokenName
	 */
	public String getTokenName()
	{
		return this.tokenName;
	}

	/**
	 * @param tokenName the tokenName to set
	 */
	public void setTokenName(String tokenName)
	{
		this.tokenName = tokenName;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters()
	{
		return this.parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters)
	{
		this.parameters = parameters;
	}

	public String getAgeMinUnits()
	{
		return this.ageMinUnits;
	}

	public void setAgeMinUnits(String ageMinUnits)
	{
		this.ageMinUnits = ageMinUnits;
	}

	public String getAgeMaxUnits()
	{
		return this.ageMaxUnits;
	}

	public void setAgeMaxUnits(String ageMaxUnits)
	{
		this.ageMaxUnits = ageMaxUnits;
	}

	public Integer getAgeMin()
	{
		return this.ageMin;
	}

	public void setAgeMin(Integer ageMin)
	{
		this.ageMin = ageMin;
	}

	public Integer getAgeMax()
	{
		return this.ageMax;
	}

	public void setAgeMax(Integer ageMax)
	{
		this.ageMax = ageMax;
	}

	public boolean checkAgeRestrictions(Patient patient)
	{
		String minAgeUnits = null;
		String maxAgeUnits = null;
		minAgeUnits = getAgeMinUnits();
		maxAgeUnits = getAgeMaxUnits();

		boolean minUnitsOk = false;
		if (minAgeUnits != null)
		{
			minUnitsOk = processMinAge(minAgeUnits, patient);
		} else
		{
			minUnitsOk = true;
		}
		boolean maxUnitsOk = false;
		if (maxAgeUnits != null)
		{
			maxUnitsOk = processMaxAge(maxAgeUnits, patient);
		} else
		{
			maxUnitsOk = true;
		}
		
		if(minUnitsOk&&maxUnitsOk){
			return true;
		}
		return false;
	}

	private boolean processMinAge(String units,Patient patient)
	{
		Date today = new Date();
		Date birthdate = patient.getBirthdate();
		int ageInDays = Util.getAgeInUnits(birthdate, today, Util.DAY_ABBR);
		int ageInWeeks = Util.getAgeInUnits(birthdate, today, Util.WEEK_ABBR);
		int ageInMonths = Util.getAgeInUnits(birthdate, today, Util.MONTH_ABBR);
		int ageInYears = Util.getAgeInUnits(birthdate, today, Util.YEAR_ABBR);

		if (units.equalsIgnoreCase("days"))
		{
			if (getAgeMin() <= ageInDays)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("weeks"))
		{
			if (getAgeMin() <= ageInWeeks)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("months"))
		{
			if (getAgeMin() <= ageInMonths)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("years"))
		{
			if (getAgeMin() <= ageInYears)
			{
				return true;
			}
		}

		return false;
	}

	private boolean processMaxAge(String units, Patient patient)
	{
		Date today = new Date();
		Date birthdate = patient.getBirthdate();
		int ageInDays = Util.getAgeInUnits(birthdate, today, Util.DAY_ABBR);
		int ageInWeeks = Util.getAgeInUnits(birthdate, today, Util.WEEK_ABBR);
		int ageInMonths = Util.getAgeInUnits(birthdate, today, Util.MONTH_ABBR);
		int ageInYears = Util.getAgeInUnits(birthdate, today, Util.YEAR_ABBR);

		if (units.equalsIgnoreCase("days"))
		{
			if (getAgeMax() > ageInDays)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("weeks"))
		{
			if (getAgeMax() > ageInWeeks)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("months"))
		{
			if (getAgeMax() > ageInMonths)
			{
				return true;
			}
		} else if (units.equalsIgnoreCase("years"))
		{
			if (getAgeMax() > ageInYears)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((ageMax == null) ? 0 : ageMax.hashCode());
		result = prime * result + ((ageMaxUnits == null) ? 0 : ageMaxUnits.hashCode());
		result = prime * result + ((ageMin == null) ? 0 : ageMin.hashCode());
		result = prime * result + ((ageMinUnits == null) ? 0 : ageMinUnits.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((citations == null) ? 0 : citations.hashCode());
		result = prime * result + ((classFilename == null) ? 0 : classFilename.hashCode());
		result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((explanation == null) ? 0 : explanation.hashCode());
		result = prime * result + ((institution == null) ? 0 : institution.hashCode());
		result = prime * result + ((keywords == null) ? 0 : keywords.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result + ((logic == null) ? 0 : logic.hashCode());
		result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((ruleCreationDate == null) ? 0 : ruleCreationDate.hashCode());
		result = prime * result + ((ruleId == null) ? 0 : ruleId.hashCode());
		result = prime * result + ((specialist == null) ? 0 : specialist.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((tokenName == null) ? 0 : tokenName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (ageMax == null) {
			if (other.ageMax != null)
				return false;
		} else if (!ageMax.equals(other.ageMax))
			return false;
		if (ageMaxUnits == null) {
			if (other.ageMaxUnits != null)
				return false;
		} else if (!ageMaxUnits.equals(other.ageMaxUnits))
			return false;
		if (ageMin == null) {
			if (other.ageMin != null)
				return false;
		} else if (!ageMin.equals(other.ageMin))
			return false;
		if (ageMinUnits == null) {
			if (other.ageMinUnits != null)
				return false;
		} else if (!ageMinUnits.equals(other.ageMinUnits))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (citations == null) {
			if (other.citations != null)
				return false;
		} else if (!citations.equals(other.citations))
			return false;
		if (classFilename == null) {
			if (other.classFilename != null)
				return false;
		} else if (!classFilename.equals(other.classFilename))
			return false;
		if (creationTime == null) {
			if (other.creationTime != null)
				return false;
		} else if (!creationTime.equals(other.creationTime))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (explanation == null) {
			if (other.explanation != null)
				return false;
		} else if (!explanation.equals(other.explanation))
			return false;
		if (institution == null) {
			if (other.institution != null)
				return false;
		} else if (!institution.equals(other.institution))
			return false;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (logic == null) {
			if (other.logic != null)
				return false;
		} else if (!logic.equals(other.logic))
			return false;
		if (purpose == null) {
			if (other.purpose != null)
				return false;
		} else if (!purpose.equals(other.purpose))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (ruleCreationDate == null) {
			if (other.ruleCreationDate != null)
				return false;
		} else if (!ruleCreationDate.equals(other.ruleCreationDate))
			return false;
		if (ruleId == null) {
			if (other.ruleId != null)
				return false;
		} else if (!ruleId.equals(other.ruleId))
			return false;
		if (specialist == null) {
			if (other.specialist != null)
				return false;
		} else if (!specialist.equals(other.specialist))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (tokenName == null) {
			if (other.tokenName != null)
				return false;
		} else if (!tokenName.equals(other.tokenName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule [ruleId=" + ruleId + ", classFilename=" + classFilename + ", creationTime=" + creationTime + ", title="
		        + title + ", version=" + version + ", institution=" + institution + ", author=" + author + ", specialist="
		        + specialist + ", ruleCreationDate=" + ruleCreationDate + ", purpose=" + purpose + ", explanation="
		        + explanation + ", keywords=" + keywords + ", citations=" + citations + ", links=" + links + ", data=" + data
		        + ", logic=" + logic + ", action=" + action + ", lastModified=" + lastModified + ", result=" + result
		        + ", tokenName=" + tokenName + ", ageMinUnits=" + ageMinUnits + ", ageMaxUnits=" + ageMaxUnits + ", ageMin="
		        + ageMin + ", ageMax=" + ageMax + "]";
	}
}