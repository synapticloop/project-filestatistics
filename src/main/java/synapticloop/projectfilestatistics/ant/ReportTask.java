package synapticloop.projectfilestatistics.ant;

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
 * 
 * This work is based on the filestatistics ant plugin from NetReturn Consulting
 * http://www.netreturnconsulting.com.au/ and extended to include abstract 
 * printing architecture and removal of history statistics.
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Vector;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import synapticloop.projectfilestatistics.ant.bean.StatisticsBean;
import synapticloop.projectfilestatistics.ant.reporter.AbstractReporter;
import synapticloop.projectfilestatistics.util.Constants;
import synapticloop.projectfilestatistics.util.PropertyManager;

/**
 * This class collects statistics on files within the specified fileset.  It
 * records file types, number of files and number of lines (both code and
 * comments) and stores them in the StatisticsBean.
 * 
 * Three attributes are allowed for the element as follows:
 * <dl>
 *   <dt><strong>ignoreBinary</strong> - boolean - optional - default false</dt>
 *   <dd>Any extension which is considered 'binary' from the list of extensions
 *       will be silently ignored</dd>
 *   <dt><strong>propertyFile</strong> - String - optional</dt>
 *   <dd>The propertyFile location will be loaded and will over-ride any of the
 *       properties set in the default property file name</dd>
 * </dl>
 * 
 */

public class ReportTask extends Task {
	// custom property file name
	private String propertyFileName = null;
	// whether to ignore binary files
	private boolean ignoreBinary = false;
	// and the associated binary file extensions cache
	private HashSet<String> binaryFileExtensionSet = new HashSet<String>();
	// files to be checked 
	protected Vector<FileSet> filesets = new Vector<FileSet>();
	// the output directory
	private String outputDirectory = null;
	// where to serialise the statistics for

	// list of plugins to use
	private String pluginList = Constants.DEFAULT_PLUGIN_LIST;

	private StatisticsBean statisticsBean = new StatisticsBean();

	Vector<AbstractReporter> reporters = new Vector<AbstractReporter>();


	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() {
		// load up the properties
		try {
			PropertyManager.getInstance().initialise(Constants.DEFAULT_PROPERTY_FILE_NAME, propertyFileName);
		} catch(MissingResourceException jumrex) {
			getProject().log("Cannot load default properties file '" + Constants.DEFAULT_PROPERTY_FILE_NAME + "'.\nexiting...", Project.MSG_ERR);
			return;
		}

		// check for binary file checking
		if(ignoreBinary) {
			loadBinaryFileExtensions();
		}

		// go through each of the files and record the statistics
		for(int i = 0; i < filesets.size(); i++) {
			FileSet fileset = (FileSet) filesets.elementAt(i);
			DirectoryScanner directoryScanner = fileset.getDirectoryScanner(getProject());
			String[] files = directoryScanner.getIncludedFiles();
			File dirBase = fileset.getDir(getProject());

			for(int j = 0; j < files.length; j++){
				File file = new File(dirBase, files[j]);
				if(file.isFile() && file.canRead()) {
					recordFileStatistics(file);
				}
			}
		}

		// print out the statistics
		generatePluginList();

		// now go through the plugins and execute them
		Iterator<AbstractReporter> reporterIterator = reporters.iterator();
		while (reporterIterator.hasNext()) {
			AbstractReporter abstractReporter = (AbstractReporter) reporterIterator.next();
			abstractReporter.print(outputDirectory, statisticsBean);
		}
	}

	/**
	 * Generate the list of the plugins to be executed.  Plugins _MUST_ inherit
	 * from synapticloop.reporter.AbstractReporter.  The plugin name is 
	 * instantiated by attempting to load:
	 * 	this.getClass().getPackage().getName() + ".reporter." + stringPlugin
	 * if that cannot be found, the class 
	 * stringPlugin is attempted to be instantiated.
	 */
	private void generatePluginList() {
		findPlugins();
		//		findHistoricalPlugins();
	}

	private void findPlugins() {
		String[] pluginSplit = pluginList.split(",");
		Object object = null;
		for (int i = 0; i < pluginSplit.length; i++) {
			String pluginName = pluginSplit[i].trim();
			object = findObject(pluginName);

			// If the object is still null
			if(null == object) {
				System.out.println("WARNING: could not find plugin " + pluginName + " class as either '" + this.getClass().getPackage().getName() + ".reporter." + pluginName + "' or '" + pluginName + "'.  Ignoring...\n");
			} else {
				if(object instanceof AbstractReporter) {
					reporters.add((AbstractReporter)object);
				} else {
					System.out.println("WARNING: plugin " + pluginName + " is not an instance of " + AbstractReporter.class.getName() + "and will be ignored.");
				}
			}
		}
	}

	private Object findObject(String pluginName) {
		Object object = null;
		// try and find a synapticloop plugin
		try {
			object = this.getClass().getClassLoader().loadClass(this.getClass().getPackage().getName() + ".reporter." + pluginName).newInstance();
		} catch (ClassNotFoundException jlcnfex) { // do nothing 
		} catch (InstantiationException jliax) { // do nothing
		} catch (IllegalAccessException jliaex) { // do nothing
		}

		// try to find other plugin namespace
		if(null == object) {
			try {
				object = this.getClass().getClassLoader().loadClass(pluginName).newInstance();
			} catch (InstantiationException jliex) { // do nothing
			} catch (IllegalAccessException jliaex) { // do nothing
			} catch (ClassNotFoundException jlcnfex) { // do nothing
			}
		}
		return(object);
	}

	/**
	 * Set the name of the custom property file to be loaded
	 * 
	 * @param propertyFileName the location of the property file
	 */

	public void setPropertyFile(String propertyFileName) { this.propertyFileName = propertyFileName; }


	/**
	 * Whether to ignore binary files as they don't have a concept of lines, 
	 * code and comments.
	 * 
	 * @param ignoreBinary whether to ignore binary files (default false)
	 */

	public void setIgnoreBinary(boolean ignoreBinary) { this.ignoreBinary = ignoreBinary; }

	/**
	 * The fileset to be used for this task
	 *  
	 * @param fileset the passed in fileset
	 */

	public void addFileset(FileSet fileset) { filesets.addElement(fileset); }

	/**
	 * Retrieve the file extension from a file name.  If the file has no
	 * extension, return an empty string.
	 *  
	 * @param fileName
	 * @return the file extension
	 */

	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf('.');
		if(index == -1) {
			return "";
		} else {
			String extension = fileName.substring(index + 1);
			return(extension);
		}
	}

	/**
	 * populate the list of binary file extensions for checking against files
	 * to determine whether to skip them.  Each element in the comma separated 
	 * list is trimmed and set to lowercase before being stored in the hashset.
	 */
	private void loadBinaryFileExtensions() {
		String binaryFileExtensions = PropertyManager.getInstance().getProperty(Constants.PROPERTY_BINARY_FILE_EXTENSION);
		if(null != binaryFileExtensions) {
			String[] binaryFiles = binaryFileExtensions.split("\\,");
			for(int i = 0; i < binaryFiles.length; i++) {
				binaryFileExtensionSet.add(binaryFiles[i].trim().toLowerCase());
			}
		}
	}

	/**
	 * Gather statistics on the specified file adding these to the totals.
	 * 
	 * @param file the file to gather statistics on
	 */

	private void recordFileStatistics(File file) {
		String fileExtension = getFileExtension(file.getName());

		// now to check whether this is a binary file
		if(ignoreBinary && binaryFileExtensionSet.contains(fileExtension.toLowerCase())) {
			return;
		}

		String singleLineComment = PropertyManager.getInstance().getProperty(fileExtension + ".comment.single");
		String multiLineCommentStart = PropertyManager.getInstance().getProperty(fileExtension + ".comment.multi.start");
		String multiLineCommentEnd = PropertyManager.getInstance().getProperty(fileExtension + ".comment.multi.end");

		boolean hasSingleLineComment = (null != singleLineComment);
		boolean hasMultiLineCommentStart = (null != multiLineCommentStart);
		boolean hasMultiLineCommentEnd = (null != multiLineCommentEnd);

		int lineCount = 0;
		int lineCodeCount = 0;
		int lineCommentCount = 0;
		int lineBlankCount = 0;

		boolean isInComment = false;

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			String line;
			do {
				boolean thisLine = false;
				line = bufferedReader.readLine();
				if (line != null) {
					lineCount++;
					lineCodeCount++;
					line = line.trim();

					// check to make sure that we aren't in a comment
					if(!isInComment && hasSingleLineComment && line.startsWith(singleLineComment)) {
						lineCommentCount++;
						lineCodeCount--;
					} else if(line.length() == 0) {
						lineCodeCount--;
						lineBlankCount++;
					} else {
						// at this point, this is definitely not a single line comment
						// and is not a blank line
						if(hasMultiLineCommentStart) {
							if(line.startsWith(multiLineCommentStart)) {
								lineCommentCount++;
								lineCodeCount--;
								isInComment = true;
								thisLine = true;
							}

							if(line.contains(multiLineCommentStart)) {
								isInComment = true;
								thisLine = true;
							}
						}

						// there may be a case where the multi-line comment 
						// starts and ends on the same line

						if(hasMultiLineCommentEnd && isInComment) {
							if(line.contains(multiLineCommentEnd)) {
								isInComment = false;
								if(!thisLine) {
									lineCommentCount++;
									lineCodeCount--;
								}
							}
						}
					}
				}
			} while (line != null);

			statisticsBean.updateCount(fileExtension, lineCodeCount, lineCommentCount, lineBlankCount);

		} catch (FileNotFoundException jifnfex) {
			// this shouldn't happen as the fileset is passed through via ant
			// which has already checked - do nothing yet.
		} catch (IOException jiioex) {
			// do nothing as to keep things quiet
		} finally {
			if(null != fileReader) {
				try {
					fileReader.close();
				} catch (IOException jiioex) {
					fileReader = null;
				}
			}

			if(null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException jiioex) {
					bufferedReader = null;
				}
			}
		}
	}

	/**
	 * @param pluginList the pluginList to set
	 */
	public void setPluginList(String pluginList) { this.pluginList = pluginList; }

	/**
	 * Set the directory for the output of the generated files.
	 * 
	 * @param outputDirectory the outputDirectory to set
	 */
	public void setOutputDirectory(String outputDirectory) {
		// need to make sure that the directory exists - if not create it - if not
		// possible to create - fail
		File file = new File(outputDirectory);
		if(!(file.exists() && file.isDirectory())) {
			// 
		}
		if(!file.exists() || !file.isDirectory()) {
			// try and create
			if(!file.mkdirs()) {
				System.out.println("WARNING: Directory cannot be created");
			}
			this.outputDirectory = null;
		} else {
			this.outputDirectory = outputDirectory;
		}
	}
}