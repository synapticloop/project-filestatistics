package synapticloop.projectfilestatistics.util;

/*
 * Copyright (c) 2009-2011 Synapticloop.
 * All rights reserved.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENCE shipped with 
 * this source code or binaries.
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * As the name would suggest, this class manages properties.  The manager may
 * get properties from two locations:
 * <ol>
 * 	<li>The default properties - which are the defaults and must exist</li>
 * 	<li>The over-ride properties - which allow over-riding of the default properties</li>
 * </ol>
 * 
 */
public class PropertyManager {
	private final static PropertyManager propertyManager = new PropertyManager();
	// the global properties
	private static Properties properties = null;
	// whether the property manager is initialised
	private static boolean isInitialised = false;
	
	/**
	 * Private constructor to thwart instantiation
	 */
	private PropertyManager() {
		
	}
	
	public static PropertyManager getInstance() {
		return(propertyManager);
	}

	/**
	 * return a property from the default properties file.
	 * 
	 * @param key the key to lookup
	 * @return the property from the key - null if not found.
	 */
	public String getProperty(String key) {
		if(!isInitialised) {
			initialise(null, null);
		}

		if(null != properties) {
			return(properties.getProperty(key));
		}
		return(null);
	}

	/**
	 * Initialise the properties that are to be used - the default properties
	 * must exist.
	 * 
	 * @param defaultProperties the default property file location
	 * @throws MissingResourceException if the default properties cannot be found
	 */
	public void initialise(String defaultProperties) throws MissingResourceException {
		initialise(defaultProperties, null);
	}

	/**
	 * Initialise the properties that are to be used - the default properties
	 * must exist.  The over-ride properties allow the user to have custom
	 * properties which are read after the defaults.  If the over-ride 
	 * properties cannot be found an error is logged to the console.
	 * 
	 * @param defaultProperties the default property file location
	 * @param overrideProperties the over ride property file location
	 * @throws MissingResourceException if the default properties cannot be found
	 */
	public void initialise(String defaultProperties, String overrideProperties) throws MissingResourceException {
		try {
			properties = new Properties();
			properties.load(PropertyManager.class.getResourceAsStream(defaultProperties));
		} catch(FileNotFoundException jifnfex) {
			// if we cannot find the properties - cannot continue - die horribly
			throw new MissingResourceException("Cannot find the default properties file located at " + defaultProperties + " - exception was:\n" + jifnfex.getMessage(), PropertyManager.class.getName(), defaultProperties);
		} catch(IOException jiioex) {
			// if we cannot find the properties - cannot continue - die horribly
			throw new MissingResourceException("Cannot find the default properties file located at " + defaultProperties + " - exception was:\n" + jiioex.getMessage(), PropertyManager.class.getName(), defaultProperties);
		} catch(java.lang.Exception jlex) {
			// if we cannot find the properties - cannot continue - die horribly
			throw new MissingResourceException("Generic Exception on default property file located at " + defaultProperties, PropertyManager.class.getName(), defaultProperties);
		}

		// now for the over-ride properties file
		if(null != overrideProperties) {
			try {
				FileInputStream fileInputStream = new FileInputStream(new File(overrideProperties));
				properties.load(fileInputStream);
			} catch (FileNotFoundException jifnfex) {
				System.out.println("WARNING: cannot find properties file " + overrideProperties);
			} catch (IOException jiioex) {
				System.out.println("WARNING: cannot load properties file " + overrideProperties);
			}
		} 

		isInitialised = true;
		
	}
}
