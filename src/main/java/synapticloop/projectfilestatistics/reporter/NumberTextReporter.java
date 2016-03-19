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

public class NumberTextReporter extends AbstractTextReporter {
	// the following are used for the pretty printing formatter
	private static final String FILE_TYPE_HEADING = "File type";
	private static final String LINE_TOTAL_COUNT_HEADING = "Total(      %)";
	private static final String LINE_COMMENT_COUNT_HEADING = "Comment(      %)";
	private static final String LINE_CODE_COUNT_HEADING = "Code(      %)";
	private static final String LINE_BLANK_COUNT_HEADING = "Blank(      %)";


	/**
	 * Print out the newly gleaned statistics to the console.  Format the
	 * output so that it all aligns nicely. 
	 * 
	 * <em>Note:</em> Yes, it would have been possible to use java.text.* 
	 * formatter classes, except for the overhead that is incurred in setting 
	 * things up.
	 */

	@Override
	protected void printToConsole() {
		if(statisticsBean.getMaxExtensionLength() > maxExtensionLength) {
			maxExtensionLength = statisticsBean.getMaxExtensionLength();
		}

		// work out the line lengths that are required
		int lengthMaxFileCount = (Integer.toString(statisticsBean.getTotalFileCount()).length());
		int lengthMaxLineCount = (Integer.toString(statisticsBean.getTotalLineCount()).length());
		int lengthMaxCommentCount = (Integer.toString(statisticsBean.getTotalLineCommentCount()).length());
		int lengthMaxCodeCount = (Integer.toString(statisticsBean.getTotalLineCodeCount()).length());
		int lengthMaxBlankCount = (Integer.toString(statisticsBean.getTotalLineBlankCount()).length());

		if(LINE_TOTAL_COUNT_HEADING.length() > lengthMaxLineCount - 9) {
			lengthMaxLineCount = LINE_TOTAL_COUNT_HEADING.length();
		}

		if(LINE_CODE_COUNT_HEADING.length() > lengthMaxCodeCount - 9) {
			lengthMaxCodeCount = LINE_CODE_COUNT_HEADING.length();
		}

		if(LINE_COMMENT_COUNT_HEADING.length() > lengthMaxCommentCount - 9) {
			lengthMaxCommentCount = LINE_COMMENT_COUNT_HEADING.length();
		}

		if(LINE_BLANK_COUNT_HEADING.length() > lengthMaxCommentCount - 9) {
			lengthMaxBlankCount = LINE_BLANK_COUNT_HEADING.length();
		}

		System.out.println();
		// print out the heading
		System.out.println(PrintHelper.underlineText("Line number report (" + this.getClass().getSimpleName() + ")" , '=', false));

		// print out the row headings
		System.out.print(printHeading(maxExtensionLength + 2, FILE_TYPE_HEADING));
		System.out.print(printHeading(lengthMaxFileCount + 2, "#"));
		System.out.print(printHeading(lengthMaxCodeCount + 2, LINE_CODE_COUNT_HEADING));
		System.out.print(printHeading(lengthMaxCommentCount + 2, LINE_COMMENT_COUNT_HEADING));
		System.out.print(printHeading(lengthMaxBlankCount + 2, LINE_BLANK_COUNT_HEADING));
		System.out.print(printHeading(lengthMaxLineCount + 2, LINE_TOTAL_COUNT_HEADING));
		System.out.println();

		// set up the underliner
		StringBuilder underliner = new StringBuilder();
		underliner.append(PrintHelper.underline('-', maxExtensionLength + 2) + "  ");
		underliner.append(PrintHelper.underline('-', lengthMaxFileCount + 2) + "  ");
		underliner.append(PrintHelper.underline('-', lengthMaxCodeCount + 2) + "  ");
		underliner.append(PrintHelper.underline('-', lengthMaxCommentCount + 2) + "  ");
		underliner.append(PrintHelper.underline('-', lengthMaxBlankCount + 2) + "  ");
		underliner.append(PrintHelper.underline('-', lengthMaxLineCount + 2) + "  ");
		System.out.println(underliner);


		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		Iterator<String> iter = treeSet.iterator(); 

		// go through each of the file extensions and print out the statistics
		while(iter.hasNext()) {
			String key = (String)iter.next();
			// The file type i.e. the extension
			System.out.print(new PrintfFormat("%" + (maxExtensionLength + 2) + "s  ").sprintf("." + key));

			// the # of files
			System.out.print(new PrintfFormat("%" + (lengthMaxFileCount + 2) + "i  ").sprintf(statisticsBean.getHashMapTotalFileCount().get(key)));

			// the # of lines of code and the percentages
			generateSingleStatistic(lengthMaxCodeCount - 7, statisticsBean.getHashMapLineCodeCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue());


			// the # of lines of comments and the percentages
			generateSingleStatistic(lengthMaxCommentCount -7, statisticsBean.getHashMapLineCommentCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue());

			// the # of blank lines and the percentages
			generateSingleStatistic(lengthMaxBlankCount -7, statisticsBean.getHashMapLineBlankCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue());

			// the total # of lines and the percentages
			generateSingleStatistic(lengthMaxLineCount - 7, statisticsBean.getHashMapTotalLineCount().get(key).intValue(), statisticsBean.getTotalLineCount());
			System.out.println();
		}  

		// more underlining
		System.out.println(underliner);

		// the summary statistics
		// the number of different file types
		System.out.print(new PrintfFormat("%" + (maxExtensionLength - 4) + "i types  ").sprintf(statisticsBean.getHashMapTotalFileCount().keySet().size()));

		// the total number of files
		System.out.print(new PrintfFormat("%" + (lengthMaxFileCount + 2) + "i  ").sprintf(statisticsBean.getTotalFileCount()));

		// the total number of lines of code and the percentages
		generateSingleStatistic(lengthMaxCodeCount -7, statisticsBean.getTotalLineCodeCount(), statisticsBean.getTotalLineCount());
		

		// the total number of lines of comment and the percentages
		generateSingleStatistic(lengthMaxCommentCount -7, statisticsBean.getTotalLineCommentCount(), statisticsBean.getTotalLineCount());

		// the total number of blank lines and the percentages
		generateSingleStatistic(lengthMaxBlankCount -7, statisticsBean.getTotalLineBlankCount(), statisticsBean.getTotalLineCount());

		// the total number of lines and the percentages
		generateSingleStatistic(lengthMaxLineCount - 7, statisticsBean.getTotalLineCount(), statisticsBean.getTotalLineCount());
		
		System.out.println();

		// a double underliner to complete things
		System.out.println(underliner.toString().replace("-", "="));
	}

	/**
	 * Print out a single statistic in the format nnn(%%%.%%)
	 * 
	 * @param numberSpace the amount of space required for printing the number
	 * @param number the actual count
	 * @param totalNumber the total count
	 */
	private void generateSingleStatistic(int numberSpace, int number, int totalNumber) {
		System.out.print(new PrintfFormat("%" + numberSpace + "i").sprintf(number));
		System.out.print(new PrintfFormat("(%6.2f%%)  ").sprintf((number*100.00)/totalNumber));
	}
}
