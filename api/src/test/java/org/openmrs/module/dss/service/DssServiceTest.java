package org.openmrs.module.dss.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutil.util.IOUtil;
import org.openmrs.module.dss.DssRule;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;

public class DssServiceTest extends BaseModuleContextSensitiveTest{
	
	@BeforeEach
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testGetRuleById() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule(3);
		Assertions.assertNotNull(rule);
		Assertions.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", rule.getClassFilename());
		Assertions.assertEquals("Depression Maternal Depression PWS Followup", rule.getTitle());
		Assertions.assertEquals(new Double(1), rule.getVersion());
		Assertions.assertEquals("Indiana University School of Medicine", rule.getInstitution());
		Assertions.assertEquals("The author", rule.getAuthor());
		Assertions.assertEquals("Pediatrics", rule.getSpecialist());
		Assertions.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", rule.getPurpose());
		Assertions.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", rule.getExplanation());
		Assertions.assertEquals("depression, PWS, study, maternal, mom", rule.getKeywords());
		Assertions.assertEquals("read read read read read read read If endif", rule.getData());
		Assertions.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", rule.getLogic());
		Assertions.assertEquals("write write write write write write write", rule.getAction());
		Assertions.assertEquals("MDEPFUPWS", rule.getTokenName());
		Assertions.assertEquals("days", rule.getAgeMinUnits());
		Assertions.assertEquals("months", rule.getAgeMaxUnits());
		Assertions.assertEquals(new Integer(0), rule.getAgeMin());
		Assertions.assertEquals(new Integer(27), rule.getAgeMax());
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assertions.assertEquals("2009-01-07 13:17:52", dt.format(rule.getCreationTime()));
		Assertions.assertEquals("2004-07-28 05:16:59", rule.getRuleCreationDate());
		Assertions.assertEquals("2016-03-22 10:21:18", dt.format(rule.getLastModified()));
	}
	
	@Test
	public void testGetRuleByTokenName() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		Assertions.assertNotNull(rule);
		Assertions.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", rule.getClassFilename());
		Assertions.assertEquals("Depression Maternal Depression PWS Followup", rule.getTitle());
		Assertions.assertEquals(new Double(1), rule.getVersion());
		Assertions.assertEquals("Indiana University School of Medicine", rule.getInstitution());
		Assertions.assertEquals("The author", rule.getAuthor());
		Assertions.assertEquals("Pediatrics", rule.getSpecialist());
		Assertions.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", rule.getPurpose());
		Assertions.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", rule.getExplanation());
		Assertions.assertEquals("depression, PWS, study, maternal, mom", rule.getKeywords());
		Assertions.assertEquals("This is a citation", rule.getCitations());
		Assertions.assertEquals("This is a link", rule.getLinks());
		Assertions.assertEquals("read read read read read read read If endif", rule.getData());
		Assertions.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", rule.getLogic());
		Assertions.assertEquals("write write write write write write write", rule.getAction());
		Assertions.assertEquals("MDEPFUPWS", rule.getTokenName());
		Assertions.assertEquals("days", rule.getAgeMinUnits());
		Assertions.assertEquals("months", rule.getAgeMaxUnits());
		Assertions.assertEquals(new Integer(0), rule.getAgeMin());
		Assertions.assertEquals(new Integer(27), rule.getAgeMax());
		
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assertions.assertEquals("2009-01-07 13:17:52", dt.format(rule.getCreationTime()));
		Assertions.assertEquals("2004-07-28 05:16:59", rule.getRuleCreationDate());
		Assertions.assertEquals("2016-03-22 10:21:18", dt.format(rule.getLastModified()));
	}
	
	@Test
	public void testAddRule() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		DssRule newRule = new TestRule();
		String classFilename = "F:\\TestRule.class";
		dssService.addRule(classFilename, newRule);
		
		String tokenName = IOUtil.getFilenameWithoutExtension(classFilename);
		Rule dbRule = dssService.getRule("TestRule");
		Assertions.assertEquals(tokenName, dbRule.getTokenName());
		Assertions.assertEquals(classFilename, dbRule.getClassFilename());
		Assertions.assertEquals("Title", dbRule.getTitle());
		Assertions.assertEquals(new Double(1), dbRule.getVersion());
		Assertions.assertEquals("Institution", dbRule.getInstitution());
		Assertions.assertEquals("Author", dbRule.getAuthor());
		Assertions.assertEquals("Specialist", dbRule.getSpecialist());
		Assertions.assertEquals("2017-02-07 15:19:54", dbRule.getRuleCreationDate());
		Assertions.assertEquals("Purpose", dbRule.getPurpose());
		Assertions.assertEquals("Explanation", dbRule.getExplanation());
		Assertions.assertEquals("Keywords", dbRule.getKeywords());
		Assertions.assertEquals("Citations", dbRule.getCitations());
		Assertions.assertEquals("Links", dbRule.getLinks());
		Assertions.assertEquals("Data", dbRule.getData());
		Assertions.assertEquals("Logic", dbRule.getLogic());
		Assertions.assertEquals("Action", dbRule.getAction());
		Assertions.assertEquals("months", dbRule.getAgeMinUnits());
		Assertions.assertEquals("years", dbRule.getAgeMaxUnits());
		Assertions.assertEquals(new Integer(3), dbRule.getAgeMin());
		Assertions.assertEquals(new Integer(21), dbRule.getAgeMax());
	}
	
	@Test
	public void testGetRuleType() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		RuleType ruleType = dssService.getRuleType("PWS");
		Assertions.assertNotNull(ruleType);
		Assertions.assertEquals(new Integer(1), ruleType.getRuleTypeId());
		Assertions.assertEquals("PWS", ruleType.getName());
		Assertions.assertEquals(new Integer(1), ruleType.getCreator().getUserId());
		Assertions.assertEquals("2017-07-19 11:32:21", dt.format(ruleType.getDateCreated()));
		Assertions.assertEquals(Boolean.FALSE, ruleType.getRetired());
		Assertions.assertEquals("7cb211ff-6c97-11e7-9be2-0a0027000019", ruleType.getUuid());
	}
	
	@Test
	public void testGetRuleTypes() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<RuleType> ruleTypes = dssService.getRuleTypes(false);
		Assertions.assertNotNull(ruleTypes);
		Assertions.assertTrue(ruleTypes.size() == 3);
		ruleTypes = dssService.getRuleTypes(true);
		Assertions.assertNotNull(ruleTypes);
		Assertions.assertTrue(ruleTypes.size() == 4);
	}
	
	@Test
	public void testGetRuleEntry() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		RuleType ruleType = dssService.getRuleType("PWS");
		RuleEntry ruleEntry = dssService.getRuleEntry(rule, ruleType);
		Assertions.assertNotNull(ruleEntry);
		Assertions.assertEquals(new Integer(1), ruleEntry.getRuleEntryId());
		Assertions.assertEquals(new Integer(1), ruleEntry.getRuleType().getRuleTypeId());
		Assertions.assertEquals(new Integer(3), ruleEntry.getRule().getRuleId());
		Assertions.assertEquals(new Integer(227), ruleEntry.getPriority());
		Assertions.assertEquals(new Integer(1), ruleEntry.getCreator().getUserId());
		Assertions.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntry.getDateCreated()));
		Assertions.assertEquals(Boolean.FALSE, ruleEntry.getRetired());
		Assertions.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntry.getUuid());
	}
	
	@Test
	public void testGetRuleEntryByKeys() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		RuleEntry ruleEntry = dssService.getRuleEntry(new Integer(3), "PWS");
		Assertions.assertNotNull(ruleEntry);
		Assertions.assertEquals(new Integer(1), ruleEntry.getRuleEntryId());
		Assertions.assertEquals(new Integer(1), ruleEntry.getRuleType().getRuleTypeId());
		Assertions.assertEquals(new Integer(3), ruleEntry.getRule().getRuleId());
		Assertions.assertEquals(new Integer(227), ruleEntry.getPriority());
		Assertions.assertEquals(new Integer(1), ruleEntry.getCreator().getUserId());
		Assertions.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntry.getDateCreated()));
		Assertions.assertEquals(Boolean.FALSE, ruleEntry.getRetired());
		Assertions.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntry.getUuid());
	}
	
	@Test
	public void testGetRuleReferences() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		List<RuleEntry> ruleEntries = dssService.getRuleReferences(rule);
		Assertions.assertNotNull(ruleEntries);
		Assertions.assertTrue(ruleEntries.size() > 0);
	}
	
	@Test
	public void testSaveRuleType() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		// Create and save a new rule type
		RuleType ruleType = new RuleType();
		ruleType.setDescription("This is a new rule type");
		ruleType.setName("PWS_Test");
		ruleType = dssService.saveRuleType(ruleType);
		Assertions.assertNotNull(ruleType);
		Assertions.assertNotNull(ruleType.getUuid());
		Assertions.assertNotNull(ruleType.getDateCreated());
		Assertions.assertNotNull(ruleType.getCreator());
		
		// Retire an existing rule type
		ruleType = dssService.getRuleType("PWS_Test");
		Assertions.assertNotNull(ruleType);
		dssService.retireRuleType(ruleType, "Voiding for test.");
	}
	
	@Test
	public void testSaveRuleEntry() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		// Create and save a new rule entry
		RuleType ruleType = dssService.getRuleType("PWS_2");
		Rule rule = dssService.getRule("MDEPFUPWS");
		RuleEntry ruleEntry = new RuleEntry();
		ruleEntry.setRuleType(ruleType);
		ruleEntry.setPriority(new Integer(98));
		ruleEntry.setRule(rule);
		ruleEntry = dssService.saveRuleEntry(ruleEntry);
		Assertions.assertNotNull(ruleEntry);
		
		// Retire an existing rule type
		ruleEntry = dssService.getRuleEntry(rule, ruleType);
		Assertions.assertNotNull(ruleEntry);
		dssService.retireRuleEntry(ruleEntry, "Voiding for test.");
	}
	
	@Test
	public void testGetPrioritizedRuleEntries() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getPrioritizedRuleEntries("PrioritizedTest");
		Assertions.assertTrue(ruleEntries.size() == 3);
		
		// Verify the ordering
		Rule rule1 = ruleEntries.get(0).getRule();
		Assertions.assertEquals(new Integer(5), rule1.getRuleId());
		Rule rule2 = ruleEntries.get(1).getRule();
		Assertions.assertEquals(new Integer(4), rule2.getRuleId());
		Rule rule3 = ruleEntries.get(2).getRule();
		Assertions.assertEquals(new Integer(7), rule3.getRuleId());
	}
	
	@Test
	public void testGetNonPrioritizedRuleEntries() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getNonPrioritizedRuleEntries("PrioritizedTest");
		Assertions.assertTrue(ruleEntries.size() == 1);
		
		// Verify the rule
		RuleEntry ruleEntry = ruleEntries.get(0);
		Rule rule = ruleEntry.getRule();
		Assertions.assertEquals(new Integer(6), rule.getRuleId());
	}
	
	@Test
	public void testGetRuleByType() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<Rule> rules = dssService.getRulesByType("PWS");
		Assertions.assertEquals(1, rules.size());
		
		rules = dssService.getRulesByType("PWS_2");
		Assertions.assertEquals(1, rules.size());
		
		rules = dssService.getRulesByType("PrioritizedTest");
		Assertions.assertEquals(4, rules.size());
	}
	
	@Test
	public void testGetDisassociatedRules() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<Rule> rules = dssService.getDisassociatedRules("PWS");
		Assertions.assertEquals(5, rules.size());
		
		rules = dssService.getDisassociatedRules("PWS_2");
		Assertions.assertEquals(5, rules.size());
		
		rules = dssService.getDisassociatedRules("PrioritizedTest");
		Assertions.assertEquals(2, rules.size());
	}
	
	/**
	 * Test to make sure that all service methods have the Authorized annotation
	 * @throws Exception
	 */
	@Test
	@SkipBaseSetup
	public void checkAuthorizationAnnotations() throws Exception {
		Method[] allMethods = DssService.class.getDeclaredMethods();
		for (Method method : allMethods) {
		    if (Modifier.isPublic(method.getModifiers())) {
		        Authorized authorized = method.getAnnotation(Authorized.class);
		        Assertions.assertNotNull(authorized, "Authorized annotation not found on method " + method.getName());
		    }
		}
	}
	
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
