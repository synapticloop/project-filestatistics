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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import synapticloop.projectfilestatistics.util.Constants;
import synapticloop.projectfilestatistics.util.PrintfFormat;

public class OverviewHtmlReporter extends AbstractHtmlReporter {

	@Override
	protected void printToConsole() {
		// we do not print to console for this one
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void printToFile() {
		StringBuffer stringBuffer = new StringBuffer();
		generateHeader(stringBuffer);
		
		stringBuffer.append("<body>");
		stringBuffer.append("<div class=\"wrapper\">");

		generateOverviewTable(stringBuffer);
		
		generateOverviewBarChart(stringBuffer);

		stringBuffer.append("<h1>Individual Pie Charts</h1>");
		
		double blankLines = (double)statisticsBean.getTotalLineBlankCount();
		double commentLines = (double)statisticsBean.getTotalLineCommentCount();
		double codeLines = (double)statisticsBean.getTotalLineCodeCount();

		stringBuffer.append(getImageHtmlTag("All Files", codeLines, commentLines, blankLines));

		Set<String> set;
		TreeSet<String> treeSet;
		Iterator iter;

		// now draw the HTML for each of the images
		set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		iter = treeSet.iterator(); 

		// go through each of the file extensions and print out the statistics
		int i = 1;
		while(iter.hasNext()) {
			String key = (String)iter.next();
			double blank = (double)statisticsBean.getHashMapLineBlankCount().get(key).intValue();
			double comment = (double)statisticsBean.getHashMapLineCommentCount().get(key).intValue();
			double code = (double)statisticsBean.getHashMapLineCodeCount().get(key).intValue();

			stringBuffer.append(getImageHtmlTag("." + key + " Files", code, comment, blank));
			i++;
		}

		generateFooter(stringBuffer);
		
		// Write generated image to a file
		String fileOutputLocation = this.outputDirectory + "/" + this.getClass().getCanonicalName() + ".html";
		try {
			// Save as PNG
			File file = new File(fileOutputLocation);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException jiioex) {
			System.out.println("FATAL: Could not write html to file " + fileOutputLocation + ", ignoring.");
		}
	}

	/**
	 * Generate the footer for the HTML page.
	 * 
	 * @param stringBuffer the string buffer to append the footer to.
	 */
	private void generateFooter(StringBuffer stringBuffer) {
		stringBuffer.append("<p>" + Constants.GENERATED_BY + "</p>");

		stringBuffer.append("<p><strong>About this page</strong></p>");
		stringBuffer.append("<p>");
		stringBuffer.append("	All charts were generated with the Google Chart APIs - see <a href=\"http://code.google.com/apis/chart/\">http://code.google.com/apis/chart/</a>");
		stringBuffer.append("</p>");
		stringBuffer.append("<p>");
		stringBuffer.append("	For terms of service - see <a href=\"http://code.google.com/apis/chart/terms.html\">http://code.google.com/apis/chart/terms.html</a>");
		stringBuffer.append("</p>");

		stringBuffer.append("</div>");
		stringBuffer.append("</body>");
		stringBuffer.append("</html>");
	}

	/**
	 * Generate the Google API chart for the overview chart for all files
	 * 
	 * @param stringBuffer the string buffer to append the API call to.
	 */
	@SuppressWarnings("unchecked")
	private void generateOverviewBarChart(StringBuffer stringBuffer) {
		stringBuffer.append("<img src=\"http://chart.apis.google.com/chart" +
				"?cht=bhs" +
				"&chco=0000FFFF,00FF00FF,C8C8C8FF" +
				"&chd=t:");
		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		Iterator iter = treeSet.iterator(); 
		StringBuffer codeStringBuffer = new StringBuffer();
		StringBuffer commentStringBuffer = new StringBuffer();
		StringBuffer blankStringBuffer = new StringBuffer();
		Vector<String> labels = new Vector<String>();
		
		boolean isFirst = true;
		// go through each of the file extensions and print out the statistics
		while(iter.hasNext()) {
			String key = (String)iter.next();
			if(!isFirst) {
				codeStringBuffer.append(',');
				commentStringBuffer.append(',');
				blankStringBuffer.append(',');
			} else {
				isFirst = false;
			}
			
			int codeLines = (int)(statisticsBean.getHashMapLineCodeCount().get(key).intValue()*100.00)/statisticsBean.getHashMapTotalLineCount().get(key).intValue();
			codeStringBuffer.append(new PrintfFormat("%i").sprintf(codeLines));
			int commentLines = (int)(statisticsBean.getHashMapLineCommentCount().get(key).intValue()*100.00)/statisticsBean.getHashMapTotalLineCount().get(key).intValue();
			commentStringBuffer.append(new PrintfFormat("%i").sprintf(commentLines));

			// make sure that the blank lines make up to 100% - rounding can get it subtle-y wrong
			int blankLines = (int)(100 - (codeLines + commentLines));
			blankStringBuffer.append(new PrintfFormat("%i").sprintf(blankLines));
			labels.add("|" + "." + key);
		}
		stringBuffer.append(codeStringBuffer + "|");
		stringBuffer.append(commentStringBuffer + "|");
		stringBuffer.append(blankStringBuffer);
		// 27 * numFiles + 76 + 27
		// each bar is 27 pixels high plus 76 for the key and bottom axis plus
		// another 27 for the title.
		
		stringBuffer.append("&chs=450x" + ((statisticsBean.getHashMapTotalLineCount().entrySet().size() + 1)* 27 + 76) +
				"&chdlp=bv" +
				"&chdl=code|comment|blank" +
				"&chtt=Cumulative+Percentages" +
				"&chxt=x,y" +
				"&chxl=1:");
		
		// for some unknown reason, the labels are in reverse!
		Collections.reverse(labels);
		
		for (Iterator<String> iterator = labels.iterator(); iterator.hasNext();) {
			stringBuffer.append(iterator.next());
		}

		// end the image tag
		stringBuffer.append("\"/>");
	}
	
	/**
	 * Generate the overview table
	 * 
	 * @param stringBuffer the string buffer to append the table to.
	 */
	@SuppressWarnings("unchecked")
	private void generateOverviewTable(StringBuffer stringBuffer) {
		stringBuffer.append("<h1>Overview</h1>");
		
		stringBuffer.append("<table cellpadding=\"0\" cellspacing=\"0\">");
		stringBuffer.append("<thead>");
		stringBuffer.append("  <tr>");
		stringBuffer.append("    <th>File Type</th>");
		stringBuffer.append("    <th>Number</th>");
		stringBuffer.append("    <th>Code (%)</th>");
		stringBuffer.append("    <th>Comment (%)</th>");
		stringBuffer.append("    <th>Blank (%)</th>");
		stringBuffer.append("    <th>Total (%)</th>");
		stringBuffer.append("  </tr>");
		stringBuffer.append("</thead>");
		stringBuffer.append("<tbody>");

		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		Iterator iter = treeSet.iterator(); 

		// go through each of the file extensions and print out the statistics
		while(iter.hasNext()) {
			String key = (String)iter.next();
			// The file type i.e. the extension
			stringBuffer.append("<tr>");
			stringBuffer.append("<td>." + key + "</td>");

			// the # of files
			stringBuffer.append("<td>" + statisticsBean.getHashMapTotalFileCount().get(key) + "</td>");

			// the # of lines of code and the percentages
			stringBuffer.append("<td class=\"code\">" + generateSingleStatistic(statisticsBean.getHashMapLineCodeCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue()) + "</td>");


			// the # of lines of comments and the percentages
			stringBuffer.append("<td class=\"comment\">" + generateSingleStatistic(statisticsBean.getHashMapLineCommentCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue()) + "</td>");

			// the # of blank lines and the percentages
			stringBuffer.append("<td class=\"blank\">" + generateSingleStatistic(statisticsBean.getHashMapLineBlankCount().get(key).intValue(), statisticsBean.getHashMapTotalLineCount().get(key).intValue()) + "</td>");

			// the total # of lines and the percentages
			stringBuffer.append("<td>" + generateSingleStatistic(statisticsBean.getHashMapTotalLineCount().get(key).intValue(), statisticsBean.getTotalLineCount()) + "</td>");
		}  

		stringBuffer.append("</tbody>");
		stringBuffer.append("<tfoot>");
		stringBuffer.append("  <tr>");

		// now for the totals
		// the summary statistics
		// the number of different file types
		stringBuffer.append("<td>" + statisticsBean.getHashMapTotalFileCount().keySet().size() + " Types</td>");

		// the total number of files
		stringBuffer.append("<td>" + statisticsBean.getTotalFileCount() + "</td>");

		// the total number of lines of code and the percentages
		stringBuffer.append("<td class=\"codetotal\">" + generateSingleStatistic(statisticsBean.getTotalLineCodeCount(), statisticsBean.getTotalLineCount()) + "</td>");
		

		// the total number of lines of comment and the percentages
		stringBuffer.append("<td class=\"commenttotal\">" + generateSingleStatistic(statisticsBean.getTotalLineCommentCount(), statisticsBean.getTotalLineCount()) + "</td>");

		// the total number of blank lines and the percentages
		stringBuffer.append("<td class=\"blanktotal\">" + generateSingleStatistic(statisticsBean.getTotalLineBlankCount(), statisticsBean.getTotalLineCount()) + "</td>");

		// the total number of lines and the percentages
		stringBuffer.append("<td>" + generateSingleStatistic(statisticsBean.getTotalLineCount(), statisticsBean.getTotalLineCount()) + "</td>");
		
		
		stringBuffer.append("  </tr>");
		stringBuffer.append("</tfoot>");
		stringBuffer.append("</table>");
	}

	/**
	 * Generate the html header with CSS includes.
	 * 
	 * @param stringBuffer the string buffer to append to.
	 */
	private void generateHeader(StringBuffer stringBuffer) {
		stringBuffer.append("<html><head>");
		stringBuffer.append("<title>HTML Based Overview</title>");
		stringBuffer.append("<style type=\"text/css\">");
		stringBuffer.append("	html {font-family: sans-serif; text-align: center; }");
		stringBuffer.append("	img {border: 1px solid black; margin:5px; padding:5px;}");
		stringBuffer.append("	.wrapper { width:700px; margin:0 auto;}");
		stringBuffer.append("	p { font-style:italic;}");
		stringBuffer.append("	table { width:100%; border:1px solid black; border-collapse:collapse;}");
		stringBuffer.append("	td { text-align:right; padding:3px; border-right:1px solid black;}");
		stringBuffer.append("	tfoot {font-weight:bold;}");
		stringBuffer.append("	th {border-bottom:1px solid black;}");
		stringBuffer.append("	tfoot td {border-top:1px solid black;}");
		stringBuffer.append("	.code { background-color:#88F;}");
		stringBuffer.append("	.comment { background-color:#8F8;}");
		stringBuffer.append("	.blank { background-color:#C8C8C8;}");
		stringBuffer.append("	.codetotal { background-color:#00F;}");
		stringBuffer.append("	.commenttotal { background-color:#0F0;}");
		stringBuffer.append("	.blanktotal { background-color:#404040;}");
		stringBuffer.append("</style>");
		stringBuffer.append("</head>");
	}

	/**
	 * Print out a single statistic in the format nnn(%%%.%%)
	 * 
	 * @param numberSpace the amount of space required for printing the number
	 * @param number the actual count
	 * @param totalNumber the total count
	 */
	private String generateSingleStatistic(int number, int totalNumber) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(new PrintfFormat("%i").sprintf(number));
		stringBuffer.append(new PrintfFormat("(%6.2f%%)").sprintf((number*100.00)/totalNumber));
		return (stringBuffer.toString());
	}

	/**
	 * Generate the 'img' tag for the Google chart API call 
	 * 
	 * @param title The title for the chart
	 * @param code the number of code lines
	 * @param comment the number of comment lines
	 * @param blank the number of blank lines
	 * 
	 * @return a string buffer containing the Google chart API 'img' tag
	 */
	private StringBuffer getImageHtmlTag(String title, double code, double comment, double blank) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<h2>" + title + "</h2>");
		stringBuffer.append("<img src=\"http://chart.apis.google.com/chart" +
				"?cht=p3" +
				"&chd=t:" + (int)code + "," + (int)comment + "," + (int)blank +
				"&chco=0000FFFF,00FF00FF,C8C8C8FF" +
				"&chs=300x300" +
				"&chdlp=bv" +
				"&chdl=code|comment|blank" +
				"&chtt=" + title.replace(' ', '+') + "\">\n");
			
		return (stringBuffer);
	}
}
