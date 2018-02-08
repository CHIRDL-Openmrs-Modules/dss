package org.openmrs.module.dss.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutil.util.IOUtil;
import org.openmrs.module.dss.DssRule;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class ServicesTest extends BaseModuleContextSensitiveTest{
	
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testGetRuleById() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule(3);
		Assert.assertNotNull(rule);
		Assert.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", rule.getClassFilename());
		Assert.assertEquals("Depression Maternal Depression PWS Followup", rule.getTitle());
		Assert.assertEquals(new Double(1), rule.getVersion());
		Assert.assertEquals("Indiana University School of Medicine", rule.getInstitution());
		Assert.assertEquals("The author", rule.getAuthor());
		Assert.assertEquals("Pediatrics", rule.getSpecialist());
		Assert.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", rule.getPurpose());
		Assert.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", rule.getExplanation());
		Assert.assertEquals("depression, PWS, study, maternal, mom", rule.getKeywords());
		Assert.assertEquals("read read read read read read read If endif", rule.getData());
		Assert.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", rule.getLogic());
		Assert.assertEquals("write write write write write write write", rule.getAction());
		Assert.assertEquals("MDEPFUPWS", rule.getTokenName());
		Assert.assertEquals("days", rule.getAgeMinUnits());
		Assert.assertEquals("months", rule.getAgeMaxUnits());
		Assert.assertEquals(new Integer(0), rule.getAgeMin());
		Assert.assertEquals(new Integer(27), rule.getAgeMax());
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals("2009-01-07 13:17:52", dt.format(rule.getCreationTime()));
		Assert.assertEquals("2004-07-28 05:16:59", rule.getRuleCreationDate());
		Assert.assertEquals("2016-03-22 10:21:18", dt.format(rule.getLastModified()));
	}
	
	@Test
	public void testGetRuleByTokenName() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		Assert.assertNotNull(rule);
		Assert.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", rule.getClassFilename());
		Assert.assertEquals("Depression Maternal Depression PWS Followup", rule.getTitle());
		Assert.assertEquals(new Double(1), rule.getVersion());
		Assert.assertEquals("Indiana University School of Medicine", rule.getInstitution());
		Assert.assertEquals("The author", rule.getAuthor());
		Assert.assertEquals("Pediatrics", rule.getSpecialist());
		Assert.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", rule.getPurpose());
		Assert.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", rule.getExplanation());
		Assert.assertEquals("depression, PWS, study, maternal, mom", rule.getKeywords());
		Assert.assertEquals("This is a citation", rule.getCitations());
		Assert.assertEquals("This is a link", rule.getLinks());
		Assert.assertEquals("read read read read read read read If endif", rule.getData());
		Assert.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", rule.getLogic());
		Assert.assertEquals("write write write write write write write", rule.getAction());
		Assert.assertEquals("MDEPFUPWS", rule.getTokenName());
		Assert.assertEquals("days", rule.getAgeMinUnits());
		Assert.assertEquals("months", rule.getAgeMaxUnits());
		Assert.assertEquals(new Integer(0), rule.getAgeMin());
		Assert.assertEquals(new Integer(27), rule.getAgeMax());
		
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals("2009-01-07 13:17:52", dt.format(rule.getCreationTime()));
		Assert.assertEquals("2004-07-28 05:16:59", rule.getRuleCreationDate());
		Assert.assertEquals("2016-03-22 10:21:18", dt.format(rule.getLastModified()));
	}
	
	@Test
	public void testAddRule() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		DssRule newRule = new TestRule();
		String classFilename = "F:\\TestRule.class";
		dssService.addRule(classFilename, newRule);
		
		String tokenName = IOUtil.getFilenameWithoutExtension(classFilename);
		Rule dbRule = dssService.getRule("TestRule");
		Assert.assertEquals(tokenName, dbRule.getTokenName());
		Assert.assertEquals(classFilename, dbRule.getClassFilename());
		Assert.assertEquals("Title", dbRule.getTitle());
		Assert.assertEquals(new Double(1), dbRule.getVersion());
		Assert.assertEquals("Institution", dbRule.getInstitution());
		Assert.assertEquals("Author", dbRule.getAuthor());
		Assert.assertEquals("Specialist", dbRule.getSpecialist());
		Assert.assertEquals("2017-02-07 15:19:54", dbRule.getRuleCreationDate());
		Assert.assertEquals("Purpose", dbRule.getPurpose());
		Assert.assertEquals("Explanation", dbRule.getExplanation());
		Assert.assertEquals("Keywords", dbRule.getKeywords());
		Assert.assertEquals("Citations", dbRule.getCitations());
		Assert.assertEquals("Links", dbRule.getLinks());
		Assert.assertEquals("Data", dbRule.getData());
		Assert.assertEquals("Logic", dbRule.getLogic());
		Assert.assertEquals("Action", dbRule.getAction());
		Assert.assertEquals("months", dbRule.getAgeMinUnits());
		Assert.assertEquals("years", dbRule.getAgeMaxUnits());
		Assert.assertEquals(new Integer(3), dbRule.getAgeMin());
		Assert.assertEquals(new Integer(21), dbRule.getAgeMax());
	}
	
	@Test
	public void testGetRuleType() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		RuleType ruleType = dssService.getRuleType("PWS");
		Assert.assertNotNull(ruleType);
		Assert.assertEquals(new Integer(1), ruleType.getRuleTypeId());
		Assert.assertEquals("PWS", ruleType.getRuleType());
		Assert.assertEquals(new Integer(1), ruleType.getCreator().getUserId());
		Assert.assertEquals("2017-07-19 11:32:21", dt.format(ruleType.getDateCreated()));
		Assert.assertEquals(Boolean.FALSE, ruleType.getVoided());
		Assert.assertEquals("7cb211ff-6c97-11e7-9be2-0a0027000019", ruleType.getUuid());
	}
	
	@Test
	public void testGetRuleEntry() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		RuleType ruleType = dssService.getRuleType("PWS");
		RuleEntry ruleEntry = dssService.getRuleEntry(rule, ruleType);
		Assert.assertNotNull(ruleEntry);
		Assert.assertEquals(new Integer(1), ruleEntry.getRuleEntryId());
		Assert.assertEquals(new Integer(1), ruleEntry.getRuleType().getRuleTypeId());
		Assert.assertEquals(new Integer(3), ruleEntry.getRule().getRuleId());
		Assert.assertEquals(new Integer(227), ruleEntry.getPriority());
		Assert.assertEquals(new Integer(1), ruleEntry.getCreator().getUserId());
		Assert.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntry.getDateCreated()));
		Assert.assertEquals(Boolean.FALSE, ruleEntry.getVoided());
		Assert.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntry.getUuid());
	}
	
	@Test
	public void testGetRuleEntryByKeys() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		RuleEntry ruleEntry = dssService.getRuleEntry(new Integer(3), "PWS");
		Assert.assertNotNull(ruleEntry);
		Assert.assertEquals(new Integer(1), ruleEntry.getRuleEntryId());
		Assert.assertEquals(new Integer(1), ruleEntry.getRuleType().getRuleTypeId());
		Assert.assertEquals(new Integer(3), ruleEntry.getRule().getRuleId());
		Assert.assertEquals(new Integer(227), ruleEntry.getPriority());
		Assert.assertEquals(new Integer(1), ruleEntry.getCreator().getUserId());
		Assert.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntry.getDateCreated()));
		Assert.assertEquals(Boolean.FALSE, ruleEntry.getVoided());
		Assert.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntry.getUuid());
	}
	
	@Test
	public void testGetRuleReferences() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		List<RuleEntry> ruleEntries = dssService.getRuleReferences(rule);
		Assert.assertNotNull(ruleEntries);
		Assert.assertTrue(ruleEntries.size() > 0);
	}
	
	@Test
	public void testSaveRuleType() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		// Create and save a new rule type
		RuleType ruleType = new RuleType();
		ruleType.setCreator(Context.getUserService().getUser(new Integer(1)));
		ruleType.setDateCreated(new Date());
		ruleType.setDescription("This is a new rule type");
		ruleType.setRuleType("PWS_Test");
		ruleType.setUuid(UUID.randomUUID().toString());
		ruleType = dssService.saveRuleType(ruleType);
		Assert.assertNotNull(ruleType);
		
		// Void an existing rule type
		ruleType = dssService.getRuleType("PWS_Test");
		Assert.assertNotNull(ruleType);
		ruleType.setVoided(Boolean.TRUE);
		ruleType.setVoidedBy(Context.getUserService().getUser(new Integer(1)));
		ruleType.setVoidReason("Voiding for test.");
		ruleType.setDateVoided(new Date());
		dssService.saveRuleType(ruleType);
	}
	
	@Test
	public void testSaveRuleEntry() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		// Create and save a new rule entry
		RuleType ruleType = dssService.getRuleType("PWS_2");
		Rule rule = dssService.getRule("MDEPFUPWS");
		RuleEntry ruleEntry = new RuleEntry();
		ruleEntry.setCreator(Context.getUserService().getUser(new Integer(1)));
		ruleEntry.setDateCreated(new Date());
		ruleEntry.setRuleType(ruleType);
		ruleEntry.setUuid(UUID.randomUUID().toString());
		ruleEntry.setPriority(new Integer(98));
		ruleEntry.setRule(rule);
		ruleEntry = dssService.saveRuleEntry(ruleEntry);
		Assert.assertNotNull(ruleEntry);
		
		// Void an existing rule type
		ruleEntry = dssService.getRuleEntry(rule, ruleType);
		Assert.assertNotNull(ruleEntry);
		ruleEntry.setVoided(Boolean.TRUE);
		ruleEntry.setVoidedBy(Context.getUserService().getUser(new Integer(1)));
		ruleEntry.setVoidReason("Voiding for test.");
		ruleEntry.setDateVoided(new Date());
		dssService.saveRuleEntry(ruleEntry);
	}
	
	@Test
	public void testGetPrioritizedRules() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<Rule> rules = dssService.getPrioritizedRules("PrioritizedTest");
		Assert.assertTrue(rules.size() == 3);
		
		// Verify the ordering
		Rule rule1 = rules.get(0);
		Assert.assertEquals(new Integer(5), rule1.getRuleId());
		Rule rule2 = rules.get(1);
		Assert.assertEquals(new Integer(4), rule2.getRuleId());
		Rule rule3 = rules.get(2);
		Assert.assertEquals(new Integer(7), rule3.getRuleId());
	}
	
	@Test
	public void testGetNonPrioritizedRules() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<Rule> rules = dssService.getNonPrioritizedRules("PrioritizedTest");
		Assert.assertTrue(rules.size() == 1);
		
		// Verify the rule
		Rule rule1 = rules.get(0);
		Assert.assertEquals(new Integer(6), rule1.getRuleId());
	}
	
//	@Test
//	public void testVoidingByPriority() throws Exception {
//		DssService dssService = Context.getService(DssService.class);
//		DssRule dssRule = new TestRule();
//		dssRule.set
//		Rule rule = dssService.getRule("MDEPFUPWS");
//		rule.
//	}
	
	private class TestRule implements DssRule {
		
		@Override
		public String getTitle() {
			return "Title";
		}

		@Override
		public Double getVersion() {
			return new Double(1);
		}

		@Override
		public String getInstitution() {
			return "Institution";
		}

		@Override
		public String getAuthor() {
			return "Author";
		}

		@Override
		public String getSpecialist() {
			return "Specialist";
		}

		@Override
		public String getDate() {
			return "2017-02-07 15:19:54";
		}

		@Override
		public String getPurpose() {
			return "Purpose";
		}

		@Override
		public String getExplanation() {
			return "Explanation";
		}

		@Override
		public String getKeywords() {
			return "Keywords";
		}

		@Override
		public String getCitations() {
			return "Citations";
		}

		@Override
		public String getLinks() {
			return "Links";
		}

		@Override
		public String getData() {
			return "Data";
		}

		@Override
		public String getLogic() {
			return "Logic";
		}

		@Override
		public String getAction() {
			return "Action";
		}

		@Override
		public Integer getAgeMin() {
			return new Integer(3);
		}

		@Override
		public Integer getAgeMax() {
			return new Integer(21);
		}

		@Override
		public String getAgeMinUnits() {
			return "months";
		}

		@Override
		public String getAgeMaxUnits() {
			return "years";
		}

		@Override
		public Integer getPriority() {
			return null;
		}

		@Override
		public String getType() {
			return null;
		}
		
	}
}