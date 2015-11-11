package org.openmrs.module.dss;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Checks periodically for classes to load dynamically
 * 
 * @author Tammy Dugan
 */
public class ValidateObesityScreenerTask extends AbstractTask {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void execute() {
		Context.openSession();
		try {
			log.info("Starting ValidateObesityScreenerTask");
			EncounterService encounterService = Context.getEncounterService();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_YEAR, 20);
			Date startDate = calendar.getTime();
			calendar.set(Calendar.DAY_OF_YEAR, 26);
			Date endDate = calendar.getTime();
			Collection<Encounter> encounters = encounterService.getEncounters(startDate, endDate);
			int numProcessed = 0;
			log.info("Done pulling " + encounters.size() + " patients");
			//run the scoring rules for all the patients in the database
			//obs will be saved for patients with classifications
			for (Encounter encounter : encounters) {
				runObesityScoringRules(encounter.getPatient(),encounter.getEncounterId());
				numProcessed++;
				log.info("Number patients processed: "+numProcessed+ " of "+encounters.size());
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
			log.error(org.openmrs.module.chirdlutil.util.Util.getStackTrace(e));
		}
		finally {
			Context.closeSession();
		}
	}
	
	protected void runObesityScoringRules(Patient patient,Integer encounterId) {
		DssService dssService = Context.getService(DssService.class);
		
		String baseRuleName = "obesityScoring";
		final int NUM_OBESE_RULES = 36;
		String mode = "PRODUCE";
		
		for (int i = 0; i < NUM_OBESE_RULES; i++) {
			
			try {
				if (patient != null) {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("mode", mode);
					if (encounterId != null) {
		    			parameters.put("encounterId", encounterId);
		    		}
					Rule rule = new Rule();
					String ruleName = baseRuleName + (i + 1);
					rule.setTokenName(ruleName);
					
					List<Rule> rules = dssService.getRules(rule, false, false, null);
					Rule currRule = null;
					if (rules.size() > 0) {
						currRule = rules.get(0);
					}
					
					if (currRule != null && currRule.checkAgeRestrictions(patient)) {
						currRule.setParameters(parameters);
						//will save observations for the classification
						dssService.runRule(patient, currRule);
						
					}
					
				}
				
			}
			catch (Exception e) {
				this.log.error(e.getMessage());
				this.log.error(org.openmrs.module.chirdlutil.util.Util.getStackTrace(e));
			}
		}
	}
	
}
