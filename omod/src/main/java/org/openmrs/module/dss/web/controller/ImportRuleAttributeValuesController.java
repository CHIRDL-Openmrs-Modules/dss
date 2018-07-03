package org.openmrs.module.dss.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.RuleAttribute;
import org.openmrs.module.dss.hibernateBeans.RuleAttributeValue;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "module/dss/")
public class ImportRuleAttributeValuesController {
    
    /** Form view */
    private static final String FORM_VIEW = "/module/dss/importRuleAttributeValues";
    
    /** Success view */
    private static final String SUCCESS_VIEW = "dssOperationSuccess.form";
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "importRuleAttributeValues.form", method = RequestMethod.GET)
	protected String initForm() {
		return FORM_VIEW;
	}
	
	@RequestMapping(value = "importRuleAttributeValues.form", method = RequestMethod.POST)
	protected ModelAndView processSubmit(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// Load the Teleform file.
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile dataFile = multipartRequest.getFile("dataFile");
				if (dataFile != null && !dataFile.isEmpty()) {
					String filename = dataFile.getOriginalFilename();
					int index = filename.lastIndexOf('.');
					if (index < 0) {
						map.put("incorrectExtension", Boolean.TRUE);
						return new ModelAndView(FORM_VIEW, map);
					}
					
					String extension = filename.substring(index + 1, filename.length());
					if (!extension.equalsIgnoreCase("csv")) {
						map.put("incorrectExtension", Boolean.TRUE);
						return new ModelAndView(FORM_VIEW, map);
					}
					
					processRuleAttributeValues(dataFile);
				} else {
					map.put("missingFile", Boolean.TRUE);
					return new ModelAndView(FORM_VIEW, map);
				}
			}
		}
		catch (Exception e) {
			log.error("Error while processing uploaded file from request", e);
			map.put("failedFileUpload", Boolean.TRUE);
			return new ModelAndView(FORM_VIEW, map);
		}
		
		map.put("operationType", "Import rule attribute values");
		return new ModelAndView(new RedirectView(SUCCESS_VIEW), map);
	}
	
	/**
	 * Parse out the rule attribute values and save them to the DB.
	 * 
	 * @param dataFile The MultipartFile containing the rule attribute value information.
	 * @throws IOException
	 */
	private void processRuleAttributeValues(MultipartFile dataFile) throws IOException {
	    DssService dssService = Context.getService(DssService.class);
	    //map to keep track of whether a rule attribute value has been updated
	    Map<Integer, Integer> updatedRules = new HashMap<>(); 
        List<RuleAttributeValue> ruleAttributeValuesFromFile = Util.getRuleAttributeValues(dataFile.getInputStream());
        for (RuleAttributeValue fileRuleAttributeValue : ruleAttributeValuesFromFile) {
            RuleAttribute ruleAttribute = dssService.getRuleAttribute(fileRuleAttributeValue.getRuleAttributeId());
            if (ruleAttribute != null) {
                Integer ruleId = fileRuleAttributeValue.getRuleId();
                List<RuleAttributeValue> databaseRuleAttributeValues = dssService.getRuleAttributeValues(ruleId,
                    fileRuleAttributeValue.getRuleAttributeId());
                
                int numDatabaseAttributes = databaseRuleAttributeValues.size();
                                            
                //figure out which copy of the attribute this is
                Integer resultNumInt = updatedRules.get(ruleId);
                int resultNum = 0;
                if (resultNumInt != null) {
                    resultNum = resultNumInt.intValue();
                }
                
                resultNum++;
                
                //If there is a corresponding value for the rule attribute value in the database,
                //update it instead of creating a new row
                if (resultNum <= numDatabaseAttributes) {
                    RuleAttributeValue existingAttributeValue = databaseRuleAttributeValues.get(resultNum-1);
                    existingAttributeValue.setValue(fileRuleAttributeValue.getValue());
                    fileRuleAttributeValue = existingAttributeValue;
                }
                updatedRules.put(fileRuleAttributeValue.getRuleId(), Integer.valueOf(resultNum));
                dssService.saveRuleAttributeValue(fileRuleAttributeValue);
            }
        }
	}
}
