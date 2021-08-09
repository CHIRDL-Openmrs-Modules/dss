package org.openmrs.module.dss;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.logic.result.Result;
import org.openmrs.module.chirdlutil.threadmgmt.ChirdlRunnable;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.service.DssService;

/**
 * Runnable to execute a rule.
 * 
 * @author Steve McKee
 *
 */
public class RuleRunnable implements ChirdlRunnable {
	
	private Integer patientId;
	private Rule rule;
	private List<Result> results;
	
	public RuleRunnable(Integer patientId, Rule rule) {
		this.patientId = patientId;
		this.rule = rule;
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<Rule> ruleList = new ArrayList<>();
		ruleList.add(this.rule);
		try {
			this.results = Context.getService(DssService.class).runRules(this.patientId, ruleList);
		} finally {
			Context.flushSession();
		}
	}
	
	/**
	 * @see org.openmrs.module.chirdlutil.threadmgmt.ChirdlRunnable#getPriority()
	 */
	@Override
	public int getPriority() {
		return ChirdlRunnable.PRIORITY_ONE;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutil.threadmgmt.ChirdlRunnable#getName()
	 */
	@Override
	public String getName() {
		return "Rule Runnable";
	}
	
	/**
	 * @return the results
	 */
	public List<Result> getResults() {
		return this.results;
	}
	
	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return this.rule;
	}
	
}
