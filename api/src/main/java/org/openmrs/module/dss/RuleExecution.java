package org.openmrs.module.dss;

/**
 * Bean to store a rule and its execution results.
 * 
 * @author Steve McKee
 *
 */
public class RuleExecution {
	
	private Thread thread;
	private RuleRunnable ruleRunnable;
	
	/**
	 * Constructor method
	 * 
	 * @param rule The rule
	 * @param results The rule execution results
	 */
	public RuleExecution(Thread thread, RuleRunnable ruleRunnable) {
		this.thread = thread;
		this.ruleRunnable = ruleRunnable;
	}
	
	/**
	 * @return the thread
	 */
	public Thread getThread() {
		return this.thread;
	}
	
	/**
	 * @return the ruleRunnable
	 */
	public RuleRunnable getRuleRunnable() {
		return this.ruleRunnable;
	}
}
