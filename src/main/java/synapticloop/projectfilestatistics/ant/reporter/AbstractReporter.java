package synapticloop.projectfilestatistics.ant.reporter;

/*
 * Copyright (c) 2009-2011 synapticloop.
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

import synapticloop.projectfilestatistics.ant.bean.StatisticsBean;

public abstract class AbstractReporter {
	protected String outputDirectory = null;
	protected StatisticsBean statisticsBean = null;

	public void print(String outputDirectory, StatisticsBean statisticsBean) {

		this.outputDirectory = outputDirectory;
		this.statisticsBean = statisticsBean;
		// first check to see whether we have an output-directory
		if(null != outputDirectory) {
			printToFile();
		}
		printToConsole();
	}

	protected abstract void printToConsole();
	
	protected abstract void printToFile();
}
