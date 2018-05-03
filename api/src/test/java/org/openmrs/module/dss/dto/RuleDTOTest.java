package org.openmrs.module.dss.dto;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dss.hibernateBeans.Rule;
import org.openmrs.module.dss.hibernateBeans.dto.RuleDTO;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.module.dss.util.TestUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class RuleDTOTest extends BaseModuleContextSensitiveTest {
	
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet(TestUtil.DBUNIT_SETUP_FILE);
	}
	
	@Test
	public void testMapRule() throws Exception {
		DssService dssService = Context.getService(DssService.class);
		Rule rule = dssService.getRule(3);
		RuleDTO ruleDTO = new RuleDTO(rule);
		
		Assert.assertNotNull(ruleDTO);
		Assert.assertEquals("F:\\chica\\ruleClassDirectory/org/openmrs/module/chica/rule/MDEPFUPWS.class", ruleDTO.getClassFilename());
		Assert.assertEquals("Depression Maternal Depression PWS Followup", ruleDTO.getTitle());
		Assert.assertEquals(new Double(1), ruleDTO.getVersion());
		Assert.assertEquals("Indiana University School of Medicine", ruleDTO.getInstitution());
		Assert.assertEquals("The author", ruleDTO.getAuthor());
		Assert.assertEquals("Pediatrics", ruleDTO.getSpecialist());
		Assert.assertEquals("Prompts MD to follow up suspected maternal depression. Checks if parent has gotten mental health treatment or is no longer depressed.", ruleDTO.getPurpose());
		Assert.assertEquals("This prompt is fired if MD concluded on PWS parent may have depression.", ruleDTO.getExplanation());
		Assert.assertEquals("depression, PWS, study, maternal, mom", ruleDTO.getKeywords());
		Assert.assertEquals("read read read read read read read If endif", ruleDTO.getData());
		Assert.assertEquals("If If conclude If conclude Else conclude endif If If || If || If || If || If || If || If || If || If CALL endif If CALL CALL endif If CALL endif If CALL CALL endif If CALL CALL endif If CALL CALL endif If CALL endif endif", ruleDTO.getLogic());
		Assert.assertEquals("write write write write write write write", ruleDTO.getAction());
		Assert.assertEquals("MDEPFUPWS", ruleDTO.getTokenName());
		Assert.assertEquals("days", ruleDTO.getAgeMinUnits());
		Assert.assertEquals("months", ruleDTO.getAgeMaxUnits());
		Assert.assertEquals(new Integer(0), ruleDTO.getAgeMin());
		Assert.assertEquals(new Integer(27), ruleDTO.getAgeMax());
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals("2009-01-07 13:17:52", dt.format(ruleDTO.getCreationTime()));
		Assert.assertEquals("2004-07-28 05:16:59", ruleDTO.getRuleCreationDate());
		Assert.assertEquals("2016-03-22 10:21:18", dt.format(ruleDTO.getLastModified()));
	}
}
