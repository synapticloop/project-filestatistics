package synapticloop.projectfilestatistics.reporter;

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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import synapticloop.projectfilestatistics.util.PrintHelper;
import synapticloop.projectfilestatistics.util.PrintfFormat;

public class CumulativeBarTextReporter extends AbstractTextReporter {
	private static final int NUM_CHARS = 80;

	private static final char POINT_CHAR = '+';
	private static final char LINE_CHAR = '-';
	private static final char VERTICAL_LINE_CHAR = '|';

	private static final char CODE_CHAR = '#';
	private static final char COMMENT_CHAR = ':';
	private static final char BLANK_CHAR = ' ';


	@Override
	protected void printToConsole() {
		StringBuilder stringBuilder = new StringBuilder();

		if(statisticsBean.getMaxExtensionLength() > maxExtensionLength) {
			maxExtensionLength = statisticsBean.getMaxExtensionLength();
		}
		stringBuilder.append("\n");
		stringBuilder.append(PrintHelper.underlineText("Line number report (" + this.getClass().getSimpleName() + ")" , '=', false));
		stringBuilder.append("\n");

		stringBuilder.append(printHeading(maxExtensionLength + 2, FILE_TYPE_HEADING));
		stringBuilder.append("\n");
		stringBuilder.append(PrintHelper.underline('-', maxExtensionLength + 2) + generatePercentageSpacer());
		stringBuilder.append("\n");

		// the width of the console is 40 wide - therefore each % point is 2.5%
		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		String key = null;
		Iterator<String> iter = treeSet.iterator(); 
		while(iter.hasNext()) {
			key = (String)iter.next();
			// for the graph and lines
			stringBuilder.append(new PrintfFormat("%" + (maxExtensionLength + 2) + "s  ").sprintf(""));
			stringBuilder.append(generateGraphSpacerHeader(key, POINT_CHAR, LINE_CHAR));
			stringBuilder.append("\n");
			// The file type i.e. the extension
			stringBuilder.append(new PrintfFormat("%" + (maxExtensionLength + 2) + "s  ").sprintf("." + key));
			stringBuilder.append(generateGraphSpacer(key, VERTICAL_LINE_CHAR, CODE_CHAR, COMMENT_CHAR, BLANK_CHAR));
			stringBuilder.append("\n");
		}
		// the last line
		stringBuilder.append(new PrintfFormat("%" + (maxExtensionLength + 2) + "s  ").sprintf(""));
		stringBuilder.append(generateGraphSpacerHeader(key, POINT_CHAR, LINE_CHAR) + "\n");

		stringBuilder.append("\n");

		// print out the key
		stringBuilder.append(PrintHelper.underlineText("Key:", '-', false) + "\n");
		stringBuilder.append("  '" + CODE_CHAR + "' code" + "\n");
		stringBuilder.append("  '" + COMMENT_CHAR + "' comment" + "\n");
		stringBuilder.append("  '" + BLANK_CHAR + "' blank" + "\n");

		System.out.println(stringBuilder.toString());
	}

	private String generateGraphSpacer(String fileExtension, char point, char code, char comment, char blank) {
		int totalLines = statisticsBean.getHashMapTotalLineCount().get(fileExtension).intValue();
		int numCharsForCode = (int)Math.round((statisticsBean.getHashMapLineCodeCount().get(fileExtension).intValue()*(NUM_CHARS *1.0)/totalLines));
		int numCharsForComment = (int)Math.round((statisticsBean.getHashMapLineCommentCount().get(fileExtension).intValue()*(NUM_CHARS *1.0)/totalLines));
		int numCharsForBlank = (int)Math.round((statisticsBean.getHashMapLineBlankCount().get(fileExtension).intValue()*(NUM_CHARS *1.0)/totalLines));

		// we need to check to see whether we are over the total due to rounding errors
		int difference = (numCharsForCode + numCharsForComment + numCharsForBlank) - NUM_CHARS;
		if(difference != 0) {
			// we are over or under - subtract/add it from the largest value
			if(numCharsForBlank >= numCharsForComment && numCharsForBlank >= numCharsForCode) {
				// blank it is
				numCharsForBlank -= difference;
			} else if (numCharsForComment >= numCharsForBlank && numCharsForComment >= numCharsForCode) {
				// comment it is
				numCharsForComment -= difference;
			} else {
				numCharsForCode -= difference;
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(point);
		stringBuilder.append(generateDuplicateCharacters(code, numCharsForCode));
		stringBuilder.append(point);
		stringBuilder.append(generateDuplicateCharacters(comment, numCharsForComment));
		stringBuilder.append(point);
		stringBuilder.append(generateDuplicateCharacters(blank, numCharsForBlank));
		stringBuilder.append(point);
		return (stringBuilder.toString());

	}

	private String generatePercentageSpacer() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("  0");
		boolean hasHalf = NUM_CHARS >= 4;
		boolean hasOneQuarter = NUM_CHARS >= 10;
		boolean hasThreeQuarter = hasOneQuarter;

		int i = 0;
		while(i < NUM_CHARS) {
			if(hasOneQuarter && i > Math.floor((NUM_CHARS/4))-2) {
				stringBuilder.append("25");
				hasOneQuarter = false;
				i++;
			} else if(hasHalf && i > Math.floor((NUM_CHARS/2)) -2) {
				stringBuilder.append("50");
				hasHalf = false;
				i++;
			} else if(hasThreeQuarter && i > Math.floor((NUM_CHARS*3)/4) -1) {
				stringBuilder.append("75");
				hasThreeQuarter = false;
				i++;
			} else {
				stringBuilder.append(" ");
			}
			i++;
		}
		stringBuilder.append("100");
		return (stringBuilder.toString());
	}
	/**
	 * Print the top (or the bottom) of the text graph chart
	 * 
	 * @param fileExtension the file extension to lookup and print 
	 */
	private String generateGraphSpacerHeader(String fileExtension, char point, char line) {
		return(generateGraphSpacer(fileExtension, point, line, line, line));

	}

}
