package org.openmrs.module.dss.dto;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.dto.RuleDTO;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;

public class RuleDTOTest extends BaseModuleContextSensitiveTest {
	
	@BeforeEach
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testMapRule() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule(3);
		RuleDTO ruleDTO = new RuleDTO(rule);
		
		Assertions.assertNotNull(ruleDTO);
		Assertions.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", ruleDTO.getClassFilename());
		Assertions.assertEquals("Depression Maternal Depression PWS Followup", ruleDTO.getTitle());
		Assertions.assertEquals(new Double(1), ruleDTO.getVersion());
		Assertions.assertEquals("Indiana University School of Medicine", ruleDTO.getInstitution());
		Assertions.assertEquals("The author", ruleDTO.getAuthor());
		Assertions.assertEquals("Pediatrics", ruleDTO.getSpecialist());
		Assertions.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", ruleDTO.getPurpose());
		Assertions.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", ruleDTO.getExplanation());
		Assertions.assertEquals("depression, PWS, study, maternal, mom", ruleDTO.getKeywords());
		Assertions.assertEquals("read read read read read read read If endif", ruleDTO.getData());
		Assertions.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", ruleDTO.getLogic());
		Assertions.assertEquals("write write write write write write write", ruleDTO.getAction());
		Assertions.assertEquals("MDEPFUPWS", ruleDTO.getTokenName());
		Assertions.assertEquals("days", ruleDTO.getAgeMinUnits());
		Assertions.assertEquals("months", ruleDTO.getAgeMaxUnits());
		Assertions.assertEquals(new Integer(0), ruleDTO.getAgeMin());
		Assertions.assertEquals(new Integer(27), ruleDTO.getAgeMax());
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assertions.assertEquals("2009-01-07 13:17:52", dt.format(ruleDTO.getCreationTime()));
		Assertions.assertEquals("2004-07-28 05:16:59", ruleDTO.getRuleCreationDate());
		Assertions.assertEquals("2016-03-22 10:21:18", dt.format(ruleDTO.getLastModified()));
	}
}
