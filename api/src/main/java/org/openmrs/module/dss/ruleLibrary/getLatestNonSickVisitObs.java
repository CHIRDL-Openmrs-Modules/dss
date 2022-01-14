package org.openmrs.module.dss.ruleLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicException;
import org.openmrs.logic.LogicService;
import org.openmrs.logic.Rule;
import org.openmrs.logic.result.Result;
import org.openmrs.logic.result.Result.Datatype;
import org.openmrs.logic.rule.RuleParameterInfo;

public class getLatestNonSickVisitObs implements Rule {
	
	private static final Logger log = LoggerFactory.getLogger(getLatestNonSickVisitObs.class);
	
	private LogicService logicService = Context.getLogicService();
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getParameterList()
	 */
	public Set<RuleParameterInfo> getParameterList() {
		return null;
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDependencies()
	 */
	public String[] getDependencies() {
		return new String[] {};
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getTTL()
	 */
	public int getTTL() {
		return 0; // 60 * 30; // 30 minutes
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.rule.Rule#getDatatype(String)
	 */
	public Datatype getDefaultDatatype() {
		return Datatype.CODED;
	}
	
	/**
	 * *
	 * 
	 * @see org.openmrs.logic.Rule#eval(org.openmrs.logic.LogicContext, java.lang.Integer, java.util.Map)
	 */
	public Result eval(LogicContext context, Integer patientId, Map<String, Object> parameters) throws LogicException {
		if (parameters == null) {
			return Result.emptyResult();
		}
		
		List<Result> results = (List<Result>) parameters.get("param1");
		if (results == null || results.size() == 0) {
			return Result.emptyResult();
		}
		
		Concept sickVisitConcept = Context.getConceptService().getConceptByName("SickVisit");
		Concept visitTypeConcept = Context.getConceptService().getConceptByName("VisitType");
		if (sickVisitConcept == null || visitTypeConcept == null) {
			return results.get(0);
		}
		
		List<Person> personsList = new ArrayList<Person>();
		Patient patient = Context.getPatientService().getPatient(patientId);
		if (patient != null) {
			personsList.add(patient);
		}
		
		List<Concept> conceptList = new ArrayList<Concept>();
		if (visitTypeConcept != null) {
			conceptList.add(visitTypeConcept);
		}
		
		for (Result result : results) {
			Object object = result.getResultObject();
			if (object instanceof Obs) {
				Obs obs = (Obs)object;
				Encounter encounter = obs.getEncounter();
				List<Encounter> encounterList = new ArrayList<Encounter>();
				if (encounter != null) {
					encounterList.add(encounter);
				}
				
				Location location = obs.getLocation();
				List<Location> locationList = new ArrayList<Location>();
				if (location != null) {
					locationList.add(location);
				}
				
				List<Obs> obsSickList = Context.getObsService().getObservations(personsList, encounterList, conceptList, 
					null, null, locationList, null, null, null, null, null, false);
				boolean sickVisit = false;
				for (Obs obsSick : obsSickList) {
					if (obsSick.getValueCoded() == sickVisitConcept) {
						sickVisit = true;
						break;
					}
				}
				
				if (!sickVisit) {
					return result;
				}
			}
		}
		
		return Result.emptyResult();
	}
}
