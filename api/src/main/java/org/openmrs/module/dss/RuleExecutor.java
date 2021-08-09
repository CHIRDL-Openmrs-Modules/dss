package org.openmrs.module.dss;

import org.openmrs.api.context.Daemon;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.util.Util;

/**
 * Executor used to execute a rules on separate threads
 * 
 * @author Steve McKee
 */
public class RuleExecutor {
	
	private Integer patientId;
	
	/**
	 * Constructor method
	 * 
	 * @param patientId The identifier of the patient to execute rules against
	 */
	public RuleExecutor(Integer patientId) {
		this.patientId = patientId;
	}
	
	/**
	 * Executes the provided rule and returns a Future.
	 * 
	 * @param rule The rule to execute
	 * @return Future containing a rule result
	 */
	public RuleExecution executeRule(Rule rule) {
		RuleRunnable runnable = new RuleRunnable(this.patientId, rule);
		Thread thread = Daemon.runInDaemonThread(runnable, Util.getDaemonToken());
		return new RuleExecution(thread, runnable);
	}
	
}
