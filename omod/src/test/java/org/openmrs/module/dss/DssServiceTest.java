package org.openmrs.module.dss;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.dss.service.DssService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;

public class DssServiceTest extends BaseModuleContextSensitiveTest
{
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
		        Assert.assertNotNull("Authorized annotation not found on method " + method.getName(), authorized);
		    }
		}
	}
}
