package org.openmrs.module.dss.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.RuleEntry;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.hibernateBeans.dto.RuleEntryDTO;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;

public class RuleEntryDTOTest extends BaseModuleContextSensitiveTest {
	
	@BeforeEach
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
		Assertions.assertNotNull(ruleEntryDTO);
		Assertions.assertEquals(new Integer(1), ruleEntryDTO.getRuleEntryId());
		Assertions.assertEquals(new Integer(1), ruleEntryDTO.getRuleType().getRuleTypeId());
		Assertions.assertEquals(new Integer(3), ruleEntryDTO.getRule().getRuleId());
		Assertions.assertEquals(new Integer(227), ruleEntryDTO.getPriority());
		Assertions.assertEquals(new Integer(1), ruleEntryDTO.getCreator());
		Assertions.assertEquals("2017-07-19 11:34:46", dt.format(ruleEntryDTO.getDateCreated()));
		Assertions.assertEquals(Boolean.FALSE, ruleEntryDTO.getRetired());
		Assertions.assertEquals("d30cb9c7-6c97-11e7-9be2-0a0027000010", ruleEntryDTO.getUuid());
	}
	
	@Test
	public void testMapRuleTypeList() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		List<RuleEntry> ruleEntries = dssService.getPrioritizedRuleEntries("PWS");
		List<RuleEntryDTO> ruleEntryDTOs = RuleEntryDTO.convertFrom(ruleEntries);
		Assertions.assertNotNull(ruleEntryDTOs);
		Assertions.assertTrue(ruleEntryDTOs.size() > 0);
	}
}
