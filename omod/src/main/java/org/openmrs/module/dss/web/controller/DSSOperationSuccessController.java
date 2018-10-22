package org.openmrs.module.dss.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * @author wang417
 * Controller for dssOperationSuccess.form
 */
@Controller
@RequestMapping(value = "module/dss/dssOperationSuccess.form")
public class DSSOperationSuccessController {

    /** Parameters */
    private static final String PARAMETER_OPERATION_TYPE = "operationType";

    /** Form view */
    private static final String FORM_VIEW = "/module/dss/dssOperationSuccess";
    
    /** Success view */
    private static final String SUCCESS_VIEW = "/admin/index.htm";
    
    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView processSubmit(@RequestParam(value="context", required=true) String context) {
		return new ModelAndView(new RedirectView(context + SUCCESS_VIEW));
	}

	@RequestMapping(method = RequestMethod.GET)
    protected String initForm(HttpServletRequest request, ModelMap map) {
		String operationType = request.getParameter(PARAMETER_OPERATION_TYPE);
		map.put(PARAMETER_OPERATION_TYPE, operationType);
		return FORM_VIEW;
	}
	
}
