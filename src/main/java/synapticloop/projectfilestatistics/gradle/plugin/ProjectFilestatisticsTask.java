package synapticloop.projectfilestatistics.gradle.plugin;

import java.io.BufferedReader;

/*
 * Copyright (c) 2016 Synapticloop.
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.file.FileTree;
import org.gradle.api.tasks.TaskAction;

import synapticloop.projectfilestatistics.ant.bean.StatisticsBean;
import synapticloop.projectfilestatistics.ant.reporter.CumulativeBarTextReporter;
import synapticloop.projectfilestatistics.gradle.exception.ProjectFilestatisticsException;
import synapticloop.projectfilestatistics.util.Constants;
import synapticloop.projectfilestatistics.util.PropertyManager;

public class ProjectFilestatisticsTask extends DefaultTask {
	private static final String KEY_DIR = "dir";
	private static final String KEY_INCLUDES = "includes";
	private static final String KEY_EXCLUDES = "excludes";

	private StatisticsBean statisticsBean = new StatisticsBean();
	private HashSet<String> binaryFileExtensionSet = new HashSet<String>();

//	private Vector<AbstractReporter> reporters = new Vector<AbstractReporter>();

	private boolean ignoreBinary;
	private List<String> includes;
	private List<String> excludes;
	private FileTree asFileTree;

	@TaskAction
	public void generate() throws ProjectFilestatisticsException {
		Project project = getProject();
		ProjectFilestatisticsPluginExtension extension = project.getExtensions().findByType(ProjectFilestatisticsPluginExtension.class);

		if (extension == null) {
			extension = new ProjectFilestatisticsPluginExtension();
		}

		// load up the properties
		try {
			PropertyManager.getInstance().initialise(Constants.DEFAULT_PROPERTY_FILE_NAME, extension.getPropertyFile());
		} catch(MissingResourceException jumrex) {
			throw new ProjectFilestatisticsException("Cannot load default properties file '" + Constants.DEFAULT_PROPERTY_FILE_NAME + "'.\nexiting...");
		}

		this.ignoreBinary = extension.getIgnoreBinary();
		this.includes = extension.getIncludes();
		this.excludes = extension.getExcludes();

		// check for binary file checking
		if(ignoreBinary) {
			loadBinaryFileExtensions();
		}

		String absoluteProjectPath = project.getProjectDir().getAbsolutePath();

		Map<String, Object> map  = new HashMap<String, Object>();
		map.put(KEY_DIR, absoluteProjectPath);
		map.put(KEY_INCLUDES, includes);
		map.put(KEY_EXCLUDES, excludes);

		ConfigurableFileTree fileTree = project.fileTree(map);
		this.asFileTree = fileTree.getAsFileTree();
		for (File file : fileTree) {
			recordFileStatistics(file);
		}

		// print out the statistics
//		generatePluginList();

		CumulativeBarTextReporter cumulativeBarTextReporter = new CumulativeBarTextReporter();
		cumulativeBarTextReporter.print(extension.getOutputDirectory(), statisticsBean);
	}

//	/**
//	 * Generate the list of the plugins to be executed.  Plugins _MUST_ inherit
//	 * from synapticloop.reporter.AbstractReporter.  The plugin name is 
//	 * instantiated by attempting to load:
//	 * 	this.getClass().getPackage().getName() + ".reporter." + stringPlugin
//	 * if that cannot be found, the class 
//	 * stringPlugin is attempted to be instantiated.
//	 */
//	private void generatePluginList() {
//		findPlugins();
//		//		findHistoricalPlugins();
//	}
//	
//	private void findPlugins() {
//		String[] pluginSplit = pluginList.split(",");
//		Object object = null;
//		for (int i = 0; i < pluginSplit.length; i++) {
//			String pluginName = pluginSplit[i].trim();
//			object = findObject(pluginName);
//
//			// If the object is still null
//			if(null == object) {
//				System.out.println("WARNING: could not find plugin " + pluginName + " class as either '" + this.getClass().getPackage().getName() + ".reporter." + pluginName + "' or '" + pluginName + "'.  Ignoring...\n");
//			} else {
//				if(object instanceof AbstractReporter) {
//					reporters.add((AbstractReporter)object);
//				} else {
//					System.out.println("WARNING: plugin " + pluginName + " is not an instance of " + AbstractReporter.class.getName() + "and will be ignored.");
//				}
//			}
//		}
//	}
//
//	private Object findObject(String pluginName) {
//		Object object = null;
//		// try and find a synapticloop plugin
//		try {
//			object = this.getClass().getClassLoader().loadClass(this.getClass().getPackage().getName() + ".reporter." + pluginName).newInstance();
//		} catch (ClassNotFoundException jlcnfex) { // do nothing 
//		} catch (InstantiationException jliax) { // do nothing
//		} catch (IllegalAccessException jliaex) { // do nothing
//		}
//
//		// try to find other plugin namespace
//		if(null == object) {
//			try {
//				object = this.getClass().getClassLoader().loadClass(pluginName).newInstance();
//			} catch (InstantiationException jliex) { // do nothing
//			} catch (IllegalAccessException jliaex) { // do nothing
//			} catch (ClassNotFoundException jlcnfex) { // do nothing
//			}
//		}
//		return(object);
//	}

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
}
