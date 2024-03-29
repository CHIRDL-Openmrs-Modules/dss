package org.openmrs.module.dss;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.chirdlutil.util.Util;

/**
 * Purpose: Checks that module specific global properties have been set 
 *
 * @author Tammy Dugan
 *
 */
public class DssActivator extends BaseModuleActivator {

	private static final Logger log = LoggerFactory.getLogger(DssActivator.class);

	/**
	 * @see org.openmrs.module.BaseModuleActivator#started()
	 */
	public void started() {
		log.info("Starting Dss Module");
		
		//check that all the required global properties are set
		checkGlobalProperties();
	}

	private void checkGlobalProperties()
	{
		try
		{
			AdministrationService adminService = Context.getAdministrationService();
			
			Iterator<GlobalProperty> properties = adminService
					.getAllGlobalProperties().iterator();
			GlobalProperty currProperty = null;
			String currValue = null;
			String currName = null;

			while (properties.hasNext())
			{
				currProperty = properties.next();
				currName = currProperty.getProperty();
				if (currName.startsWith("dss"))
				{
					currValue = currProperty.getPropertyValue();
					if (currValue == null || currValue.length() == 0)
					{
						log.error("You must set a value for global property: {}", currName);
					}
				}
			}
		} catch (Exception e)
		{
			log.error("Error checking global properties for dss module");
			log.error(e.getMessage());
			log.error(Util.getStackTrace(e));

		}
	}
	
	/**
	 * @see org.openmrs.module.BaseModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Shutting down Dss Module");
	}

}
