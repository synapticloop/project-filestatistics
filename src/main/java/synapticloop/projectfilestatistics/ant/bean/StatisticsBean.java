package synapticloop.projectfilestatistics.ant.bean;

/*
 * Copyright (c) 2009-2015 synapticloop.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * The statistics bean collects and stores all of the statistics for the files
 * that were indexed through the process.  The statisticsBean holds the 
 * following statistics:
 * 
 *  <ul>
 *  	<li>Total number of files</li>
 *  	<li>Total number of lines</li>
 *  	<li>Total number of comment lines</li>
 *  	<li>Total number of code lines</li>
 *  	<li>Total number of blank lines</li>
 *  </ul>
 *  
 *  And for each file type, the following statistics:
 *  
 *  <ul>
 *  	<li>The number of files</li>
 *  	<li>The number of lines</li>
 *  	<li>The number of comment lines</li>
 *  	<li>The number of code lines</li>
 *  	<li>The number of blank lines</li>
 *  </ul>
 */

public class StatisticsBean {
	// data structures to hold the statistics
	private HashMap<String, Integer> hashMapTotalFileCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashMapTotalLineCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashMapLineCommentCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashMapLineCodeCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashMapLineBlankCount = new HashMap<String, Integer>();

	// date and time for the current run
	private long runDateTime = 0;

	// statistics gathering for the totals 
	private int totalLineCount = 0;
	private int totalFileCount = 0;
	private int totalLineCommentCount = 0;
	private int totalLineCodeCount = 0;
	private int totalLineBlankCount = 0;

	// statistics for the pretty printer and image based font metrics.
	private int maxExtensionLength = 0;
	private int maxLineCount = 0;
	private int maxFileCount = 0;
	private int maxLineCodeCount = 0;
	private int maxLineCommentCount = 0;
	private int maxLineBlankCount = 0;

	public StatisticsBean() {
		// update the run date time 
		this.runDateTime = System.currentTimeMillis();
	}

	/**
	 * Update the associated line counts for the statistics and the related file
	 * extension.
	 * 
	 * @param fileExtension the file extension to add the count to
	 * @param lineCodeCount the number of lines of code
	 * @param lineCommentCount the number of lines of comments
	 * @param lineBlankCount the number of blank lines
	 */
	public void updateCount(String fileExtension, int lineCodeCount, int lineCommentCount, int lineBlankCount) {

		int lineCount = lineCodeCount + lineCommentCount + lineBlankCount;
		setTotalLineCount(getTotalLineCount() + lineCount);
		setTotalFileCount(getTotalFileCount() + 1);
		setTotalLineCommentCount(getTotalLineCommentCount() + lineCommentCount);
		setTotalLineCodeCount(getTotalLineCodeCount() + lineCodeCount);
		setTotalLineBlankCount(getTotalLineBlankCount() + lineBlankCount);

		// now check for the length of the extension for printing
		if(fileExtension.length() > getMaxExtensionLength()) {
			setMaxExtensionLength(fileExtension.length());
		}

		if(getHashMapTotalFileCount().containsKey(fileExtension)) {
			// update the data structures and the pretty printing statistics
			Integer fileCount = new Integer(getHashMapTotalFileCount().get(fileExtension).intValue() + 1);

			if(fileCount.intValue() > getMaxFileCount()) {
				setMaxFileCount(fileCount.intValue());
			}
			getHashMapTotalFileCount().put(fileExtension, (Integer) fileCount);

			Integer fileLineCount = new Integer(getHashMapTotalLineCount().get(fileExtension).intValue() + lineCount);
			if(fileLineCount.intValue() > getMaxLineCount()) {
				setMaxLineCount(fileLineCount.intValue() + 1);
			}
			getHashMapTotalLineCount().put(fileExtension, fileLineCount);

			Integer commentCount = new Integer(getHashMapLineCommentCount().get(fileExtension).intValue() + lineCommentCount);
			if(commentCount.intValue() > getMaxLineCommentCount()) {
				setMaxLineCommentCount(commentCount.intValue());
			}
			getHashMapLineCommentCount().put(fileExtension, commentCount);

			Integer codeCount = new Integer(getHashMapLineCodeCount().get(fileExtension).intValue() + lineCodeCount);
			if(codeCount.intValue() > getMaxLineCodeCount()) {
				setMaxLineCodeCount(codeCount.intValue());
			}
			getHashMapLineCodeCount().put(fileExtension, codeCount);

			Integer blankCount = new Integer(getHashMapLineBlankCount().get(fileExtension).intValue() + lineBlankCount);
			if(blankCount.intValue() > getMaxLineBlankCount()) {
				setMaxLineBlankCount(blankCount.intValue());
			}
			getHashMapLineBlankCount().put(fileExtension, blankCount);


		} else {
			setMaxFileCount(1);
			getHashMapTotalFileCount().put(fileExtension, new Integer(1));

			setMaxLineCount(lineCount);
			getHashMapTotalLineCount().put(fileExtension, new Integer(lineCount));

			setMaxLineCodeCount(lineCodeCount);
			getHashMapLineCodeCount().put(fileExtension, new Integer(lineCodeCount));

			setMaxLineCommentCount(lineCommentCount);
			getHashMapLineCommentCount().put(fileExtension, new Integer(lineCommentCount));

			setMaxLineBlankCount(lineBlankCount);
			getHashMapLineBlankCount().put(fileExtension, new Integer(lineBlankCount));
		}
		
		
	}

	/**
	 * Set the total line count.
	 * 
	 * @param totalLineCount the totalLineCount to set
	 */
	public void setTotalLineCount(int totalLineCount) {
		this.totalLineCount = totalLineCount;
	}

	/**
	 * Return the total line count.
	 * 
	 * @return the totalLineCount
	 */
	public int getTotalLineCount() {
		return totalLineCount;
	}

	/**
	 * Set the total file count.
	 * 
	 * @param totalFileCount the totalFileCount to set
	 */
	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}

	/**
	 * Return the total file count.
	 * 
	 * @return the totalFileCount
	 */
	public int getTotalFileCount() {
		return totalFileCount;
	}

	/**
	 * Set the total lines of comments.
	 * 
	 * @param totalLineCommentCount the totalLineCommentCount to set
	 */
	public void setTotalLineCommentCount(int totalLineCommentCount) {
		this.totalLineCommentCount = totalLineCommentCount;
	}

	/**
	 * Return the total lines of comments.
	 * 
	 * @return the totalLineCommentCount
	 */
	public int getTotalLineCommentCount() {
		return totalLineCommentCount;
	}

	/**
	 * Set the total lines of code.
	 * 
	 * @param totalLineCodeCount the totalLineCodeCount to set
	 */
	public void setTotalLineCodeCount(int totalLineCodeCount) {
		this.totalLineCodeCount = totalLineCodeCount;
	}

	/**
	 * Return the total lines of code.
	 * 
	 * @return the totalLineCodeCount
	 */
	public int getTotalLineCodeCount() {
		return totalLineCodeCount;
	}

	/**
	 * Set the total blank lines.
	 * 
	 * @param totalLineBlankCount the totalLineBlankCount to set
	 */
	public void setTotalLineBlankCount(int totalLineBlankCount) {
		this.totalLineBlankCount = totalLineBlankCount;
	}

	/**
	 * Return the total blank lines.
	 * 
	 * @return the totalLineBlankCount
	 */
	public int getTotalLineBlankCount() {
		return totalLineBlankCount;
	}

	/**
	 * Set the maximum length of all extensions.
	 *  
	 * @param maxExtensionLength the maxExtensionLength to set
	 */
	public void setMaxExtensionLength(int maxExtensionLength) {
		this.maxExtensionLength = maxExtensionLength;
	}

	/**
	 * Return the maximum length of all extensions.
	 * 
	 * @return the maxExtensionLength
	 */
	public int getMaxExtensionLength() {
		return maxExtensionLength;
	}

	/**
	 * Set the maximum number of lines.
	 * 
	 * @param maxLineCount the maxLineCount to set
	 */
	public void setMaxLineCount(int maxLineCount) {
		this.maxLineCount = maxLineCount;
	}

	/**
	 * Return the maximum number of lines across all files.
	 * 
	 * @return the maxLineCount
	 */
	public int getMaxLineCount() {
		return maxLineCount;
	}

	/**
	 * 
	 * @param maxFileCount the maxFileCount to set
	 */
	public void setMaxFileCount(int maxFileCount) {
		this.maxFileCount = maxFileCount;
	}

	/**
	 * @return the maxFileCount
	 */
	public int getMaxFileCount() {
		return maxFileCount;
	}

	/**
	 * @param maxLineCodeCount the maxLineCodeCount to set
	 */
	public void setMaxLineCodeCount(int maxLineCodeCount) {
		this.maxLineCodeCount = maxLineCodeCount;
	}

	/**
	 * @return the maxLineCodeCount
	 */
	public int getMaxLineCodeCount() {
		return maxLineCodeCount;
	}

	/**
	 * @param maxLineCommentCount the maxLineCommentCount to set
	 */
	public void setMaxLineCommentCount(int maxLineCommentCount) {
		this.maxLineCommentCount = maxLineCommentCount;
	}

	/**
	 * @return the maxLineCommentCount
	 */
	public int getMaxLineCommentCount() {
		return maxLineCommentCount;
	}

	/**
	 * @param maxLineBlankCount the maxLineBlankCount to set
	 */
	public void setMaxLineBlankCount(int maxLineBlankCount) {
		this.maxLineBlankCount = maxLineBlankCount;
	}

	/**
	 * @return the maxLineBlankCount
	 */
	public int getMaxLineBlankCount() {
		return maxLineBlankCount;
	}

	/**
	 * @param hashMapTotalFileCount the hashMapTotalFileCount to set
	 */
	public void setHashMapTotalFileCount(HashMap<String, Integer> hashMapTotalFileCount) {
		this.hashMapTotalFileCount = hashMapTotalFileCount;
	}

	/**
	 * @return the hashMapTotalFileCount
	 */
	public HashMap<String, Integer> getHashMapTotalFileCount() {
		return hashMapTotalFileCount;
	}

	/**
	 * @param hashMapTotalLineCount the hashMapTotalLineCount to set
	 */
	public void setHashMapTotalLineCount(HashMap<String, Integer> hashMapTotalLineCount) {
		this.hashMapTotalLineCount = hashMapTotalLineCount;
	}

	/**
	 * @return the hashMapTotalLineCount
	 */
	public HashMap<String, Integer> getHashMapTotalLineCount() {
		return hashMapTotalLineCount;
	}

	/**
	 * @param hashMapLineCommentCount the hashMapLineCommentCount to set
	 */
	public void setHashMapLineCommentCount(HashMap<String, Integer> hashMapLineCommentCount) {
		this.hashMapLineCommentCount = hashMapLineCommentCount;
	}

	/**
	 * @return the hashMapLineCommentCount
	 */
	public HashMap<String, Integer> getHashMapLineCommentCount() {
		return hashMapLineCommentCount;
	}

	/**
	 * @param hashMapLineCodeCount the hashMapLineCodeCount to set
	 */
	public void setHashMapLineCodeCount(HashMap<String, Integer> hashMapLineCodeCount) {
		this.hashMapLineCodeCount = hashMapLineCodeCount;
	}

	/**
	 * @return the hashMapLineCodeCount
	 */
	public HashMap<String, Integer> getHashMapLineCodeCount() {
		return hashMapLineCodeCount;
	}

	/**
	 * @param hashMapLineBlankCount the hashMapLineBlankCount to set
	 */
	public void setHashMapLineBlankCount(HashMap<String, Integer> hashMapLineBlankCount) {
		this.hashMapLineBlankCount = hashMapLineBlankCount;
	}

	/**
	 * @return the hashMapLineBlankCount
	 */
	public HashMap<String, Integer> getHashMapLineBlankCount() {
		return hashMapLineBlankCount;
	}

	public long getRunDateTime() {
		return runDateTime;
	}

	public void setRunDateTime(long runDateTime) {
		this.runDateTime = runDateTime;
	}

	public void serialise(File fileLocation) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLocation, true));
		Iterator<String> totalFileCountIterator = hashMapTotalFileCount.keySet().iterator();
		while (totalFileCountIterator.hasNext()) {
			String fileExtension = (String) totalFileCountIterator.next();
			String TAB = "\t";
			bufferedWriter.write(runDateTime + TAB + fileExtension);
			bufferedWriter.write(TAB + hashMapTotalFileCount.get(fileExtension));
			bufferedWriter.write(TAB + hashMapTotalLineCount.get(fileExtension));
			bufferedWriter.write(TAB + hashMapLineCodeCount.get(fileExtension));
			bufferedWriter.write(TAB + hashMapLineCommentCount.get(fileExtension));
			bufferedWriter.write(TAB + hashMapLineBlankCount.get(fileExtension));
			bufferedWriter.write("\n");
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	public static Vector<StatisticsBean> deSerialise(File fileLocation) throws IOException {
		Vector<StatisticsBean> statisticsBeans = new Vector<StatisticsBean>();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			long thisRunDateTime = 0;
			StatisticsBean statisticsBean = null;
			boolean shouldStore = false;
			while((line = bufferedReader.readLine()) != null) {
				String[] splitter = line.split("\t");
				long tempRunDateTime = Long.parseLong(splitter[0]);
	
				if(thisRunDateTime != tempRunDateTime) {
					if(thisRunDateTime != 0) {
						shouldStore = true;
					}
					thisRunDateTime = tempRunDateTime;
					if(shouldStore) {
						statisticsBeans.add(statisticsBean);
					}
					statisticsBean = new StatisticsBean();
				}
	
				statisticsBean.setRunDateTime(Long.parseLong(splitter[0]));
				statisticsBean.setTotalFileCount(Integer.parseInt(splitter[2]));
				statisticsBean.setTotalLineCount(Integer.parseInt(splitter[3]));
				statisticsBean.updateCount(splitter[1], Integer.parseInt(splitter[4]), Integer.parseInt(splitter[5]), Integer.parseInt(splitter[6]));
			}
		} catch(IOException ioex) {
			throw(ioex);
		} finally {
			if(null != bufferedReader) {
				bufferedReader.close();
			}
		}
		return(statisticsBeans);
	}
}
