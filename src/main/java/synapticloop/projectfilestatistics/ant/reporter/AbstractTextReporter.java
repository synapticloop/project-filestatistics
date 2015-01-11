package synapticloop.projectfilestatistics.ant.reporter;

import synapticloop.projectfilestatistics.util.PrintfFormat;

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

public class AbstractTextReporter extends AbstractReporter {
	protected static final String FILE_TYPE_HEADING = "File type";

	// values for totals
	protected int maxExtensionLength = FILE_TYPE_HEADING.length();

	protected String printHeading(int length, String heading) {
		return(new PrintfFormat("%" + length + "s  ").sprintf(heading));
	}

	protected String generateDuplicateCharacters(char character, int numTimes) {
		StringBuffer stringBuffer = new StringBuffer();
	
		while(numTimes > 0) {
			stringBuffer.append(character);
			numTimes--;
		}
		
		return (stringBuffer.toString());
	}

	protected void printToConsole() {
		// do nothing
	}

	protected void printToFile() {
		// do nothing
	}
}
