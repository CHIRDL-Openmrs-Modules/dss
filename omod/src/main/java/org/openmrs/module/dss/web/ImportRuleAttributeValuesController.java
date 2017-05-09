package org.openmrs.module.dss.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.Util;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ImportRuleAttributeValuesController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return "testing";
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object object,
	                                             BindException errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String view = getFormView();
		Map<Integer, Integer> updatedRules = new HashMap<Integer, Integer>(); //map to keep track of whether a rule attribute value has been updated
		
		try {
			// Load the Teleform file.
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile dataFile = multipartRequest.getFile("dataFile");
				if (dataFile != null && !dataFile.isEmpty()) {
					String filename = dataFile.getOriginalFilename();
					int index = filename.lastIndexOf(".");
					if (index < 0) {
						map.put("incorrectExtension", true);
						return new ModelAndView(view, map);
					}
					
					String extension = filename.substring(index + 1, filename.length());
					if (!extension.equalsIgnoreCase("csv")) {
						map.put("incorrectExtension", true);
						return new ModelAndView(view, map);
					}
					DssService dssService = Context.getService(DssService.class);
					
					List<RuleAttributeValue> ruleAttributeValuesFromFile = Util.getRuleAttributeValues(dataFile
					        .getInputStream());
					for (RuleAttributeValue fileRuleAttributeValue : ruleAttributeValuesFromFile) {
						RuleAttribute ruleAttribute = dssService.getRuleAttribute(fileRuleAttributeValue.getRuleAttributeId());
						if (ruleAttribute != null) {
							Integer ruleId = fileRuleAttributeValue.getRuleId();
							List<RuleAttributeValue> databaseRuleAttributeValues = dssService.getRuleAttributeValues(ruleId,
								fileRuleAttributeValue.getRuleAttributeId());
							
							Integer numDatabaseAttributes = databaseRuleAttributeValues.size();
														
							//figure out which copy of the attribute this is
							Integer resultNum = updatedRules.get(ruleId);
							if (resultNum == null) {
								resultNum = 0;
							}
							
							resultNum++;
							
							//If there is a corresponding value for the rule attribute value in the database,
							//update it instead of creating a new row
							if (resultNum <= numDatabaseAttributes) {
								RuleAttributeValue existingAttributeValue = databaseRuleAttributeValues.get(resultNum-1);
								existingAttributeValue.setValue(fileRuleAttributeValue.getValue());
								fileRuleAttributeValue = existingAttributeValue;
							}
							updatedRules.put(fileRuleAttributeValue.getRuleId(), resultNum);
							dssService.saveRuleAttributeValue(fileRuleAttributeValue);
						}
					}
					
				} else {
					map.put("missingFile", true);
					return new ModelAndView(view, map);
				}
			}
		}
		catch (Exception e) {
			log.error("Error while processing uploaded file from request", e);
			map.put("failedFileUpload", true);
			return new ModelAndView(view, map);
		}
		
		map.put("operationType", "Import rule attribute values");
		view = getSuccessView();
		return new ModelAndView(new RedirectView(view), map);
	}
}
