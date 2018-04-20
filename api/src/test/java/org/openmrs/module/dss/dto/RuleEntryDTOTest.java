package org.openmrs.module.dss.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.hibernateBeans.dto.RuleEntryDTO;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class RuleEntryDTOTest extends BaseModuleContextSensitiveTest {
	
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testMapRuleEntry() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule("MDEPFUPWS");
		RuleType ruleType = dssService.getRuleType("PWS");
		RuleEntry ruleEntry = dssService.getRuleEntry(rule, ruleType);
		RuleEntryDTO ruleEntryDTO = new RuleEntryDTO(ruleEntry);
		Assert.assertNotNull(ruleEntryDTO);
		Assert.assertEquals(new Integer(1), ruleEntryDTO.getRuleEntryId());
		Assert.assertEquals(new Integer(1), ruleEntryDTO.getRuleType().getRuleTypeId());
		Assert.assertEquals(new Integer(3), ruleEntryDTO.getRule().getRuleId());
		Assert.assertEquals(new Integer(227), ruleEntryDTO.getPriority());
		Assert.assertEquals(new Integer(1), ruleEntryDTO.getCreator());
		Assert.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntryDTO.getDateCreated()));
		Assert.assertEquals(Boolean.FALSE, ruleEntryDTO.getRetired());
		Assert.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntryDTO.getUuid());
	}
	
	@Test
	public void testMapRuleTypeList() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getPrioritizedRuleEntries("PWS");
		List<RuleEntryDTO> ruleEntryDTOs = RuleEntryDTO.convertFrom(ruleEntries);
		Assert.assertNotNull(ruleEntryDTOs);
		Assert.assertTrue(ruleEntryDTOs.size() > 0);
	}
}
