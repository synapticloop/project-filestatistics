package synapticloop.projectfilestatistics.util;

/*
 * Copyright (c) 2009-2016 Synapticloop.
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

/**
 * This class holds all of the constants for the project
 * 
 */
public class Constants {
	// the default property file name
	public static final String DEFAULT_PROPERTY_FILE_NAME = "/project-filestatistics.properties";
	// the name of property for the binary file extensions. 
	public static final String PROPERTY_BINARY_FILE_EXTENSION = "binary.file.extension";

	public static final String GENERATED_BY = "Thoughtfully generated by synapticloop project-filestatistics ant plugin.";

	// list of all included plugins
	public static final String DEFAULT_PLUGIN_LIST = "NumberTextReporter," +
			"CumulativeBarTextReporter";

}
