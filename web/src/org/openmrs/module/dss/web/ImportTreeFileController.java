package org.openmrs.module.dss.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.dss.util.ParseTreeFile;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ImportTreeFileController extends SimpleFormController {
	
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
		MultipartFile dataFile = null;
		String outputDirectoryName = "C:\\Users\\tmdugan\\git\\Obesity_Prediction\\src\\util\\resources\\generated_mlms\\";

		try {
			// Load the file.
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				dataFile = multipartRequest.getFile("dataFile");
				if (dataFile != null && !dataFile.isEmpty()) {
					String filename = dataFile.getOriginalFilename();
					int index = filename.lastIndexOf(".");
					if (index < 0) {
						map.put("incorrectExtension", true);
						return new ModelAndView(view, map);
					}					
				} else {
					map.put("missingFile", true);
					return new ModelAndView(view, map);
				}
			}

			
			if(dataFile != null&&outputDirectoryName != null){
				ParseTreeFile.parseTree(dataFile.getInputStream(), outputDirectoryName);
			}
		}
		catch (Exception e) {
			log.error("Error while processing uploaded file from request", e);
			map.put("failedFileUpload", true);
			return new ModelAndView(view, map);
		}
		
		map.put("operationType", "Import WEKA Tree");
		view = getSuccessView();
		return new ModelAndView(new RedirectView(view), map);
	}
}
