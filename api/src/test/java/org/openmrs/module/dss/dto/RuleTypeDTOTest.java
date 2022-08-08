package org.openmrs.module.dss.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.RuleType;
import org.openmrs.module.dss.hibernateBeans.dto.RuleTypeDTO;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;

public class RuleTypeDTOTest extends BaseModuleContextSensitiveTest {
	
	@BeforeEach
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testMapRuleType() throws Exception {
		DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DssService dssService = Context.getService(DssService.class);
		RuleType ruleType = dssService.getRuleType("PWS");
		RuleTypeDTO ruleTypeDTO = new RuleTypeDTO(ruleType);
		Assertions.assertNotNull(ruleTypeDTO);
		Assertions.assertEquals(new Integer(1), ruleTypeDTO.getRuleTypeId());
		Assertions.assertEquals("PWS", ruleTypeDTO.getName());
		Assertions.assertEquals(new Integer(1), ruleTypeDTO.getCreator());
		Assertions.assertEquals("2017-07-19 11:32:21", dt.format(ruleTypeDTO.getDateCreated()));
		Assertions.assertEquals(Boolean.FALSE, ruleTypeDTO.getRetired());
		Assertions.assertEquals("7cb211ff-6c97-11e7-9be2-0a0027000019", ruleTypeDTO.getUuid());
	}
}
