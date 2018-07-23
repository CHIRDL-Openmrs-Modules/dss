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
 * Controller for manage_rules.form
 * 
 * @author Steve McKee
 */
@Controller
@RequestMapping(value = "module/dss/")
public class ManageRulesController {
	
	/** Form view */
	private static final String FORM_VIEW = "/module/dss/manage_rules";

	@RequestMapping(value = "manage_rules.form", method = RequestMethod.GET)
	protected String initForm(HttpServletRequest request, ModelMap map) throws Exception {
		return FORM_VIEW;
	}
	
	@RequestMapping(value = "manage_rules.form", method = RequestMethod.POST)
	protected ModelAndView processSubmit(HttpServletRequest request, 
										 @RequestParam(value="successViewName", required=true) String successViewName) 
												 throws Exception {
		return new ModelAndView(new RedirectView(successViewName));
	}
}
