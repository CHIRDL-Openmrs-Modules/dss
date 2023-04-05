/**
 * 
 */
package org.openmrs.module.dss.util;

import java.util.Calendar;

import org.openmrs.module.chirdlutil.util.Util;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * @author tmdugan
 * 
 */
public class TestDSSUtil extends BaseModuleContextSensitiveTest
{
	/**
	 * 
	 */
	@Test
	public void testConvertUnitsToMetric()
	{
		Assertions.assertEquals(20.0, Util.convertUnitsToMetric(20, null));

		Assertions.assertEquals(50.8, Util.convertUnitsToMetric(20, "in"));

		Assertions.assertEquals(9.071847400000001, Util.convertUnitsToMetric(20, "lb"));

		Assertions.assertEquals(20.0, Util.convertUnitsToMetric(20, "cm"));

	}
	
	@Test
	public void testToProperCase()
	{
		String str = "firstname lastname";
		
		Assertions.assertEquals("Firstname Lastname",Util.toProperCase(str));
		
		str = "lastname";
		
		Assertions.assertEquals("Lastname",Util.toProperCase(str));
		
		str = "";
		
		Assertions.assertEquals("",Util.toProperCase(str));
		
		str = null;
		
		Assertions.assertEquals(null,Util.toProperCase(str));
	}
	
	@Test
	public void testFractionalUnits()
	{
		Calendar today = Calendar.getInstance();
		today.set(2008, Calendar.JUNE,1);
		Calendar birthdate = Calendar.getInstance();
		birthdate.set(2007, Calendar.SEPTEMBER, 9);
		double fractAge = Util.getFractionalAgeInUnits(birthdate.getTime(), 
				today.getTime(), "mo");
		Assertions.assertEquals(fractAge,8.741935483870968);
		
		today = Calendar.getInstance();
		today.set(2008, Calendar.JUNE,27);
		birthdate = Calendar.getInstance();
		birthdate.set(2007, Calendar.SEPTEMBER, 9);
		fractAge = Util.getFractionalAgeInUnits(birthdate.getTime(), 
				today.getTime(), "mo");
		Assertions.assertEquals(fractAge,9.6);
	}
	
	@Test
	public void testRound()
	{
		Assertions.assertEquals(Util.round(25.486546543, 1),25.5);
		Assertions.assertEquals(Util.round(.486546543, 2),.49);
		Assertions.assertEquals(Util.round(.111, 1),.1);
		Assertions.assertEquals(Util.round(.002, 1),0D);
		Assertions.assertEquals(Util.round(.002, 4),0.0020);
	}
}
