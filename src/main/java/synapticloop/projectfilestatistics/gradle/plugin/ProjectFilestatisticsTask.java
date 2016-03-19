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
import org.gradle.api.tasks.TaskAction;

import synapticloop.projectfilestatistics.gradle.exception.ProjectFilestatisticsException;
import synapticloop.projectfilestatistics.gradle.plugin.bean.StatisticsBean;
import synapticloop.projectfilestatistics.reporter.CumulativeBarTextReporter;
import synapticloop.projectfilestatistics.reporter.NumberTextReporter;
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
		if(includes.isEmpty()) {
			this.includes.add("src/main/**/*.*");
		}

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
		for (File file : fileTree) {
			recordFileStatistics(file);
		}

		CumulativeBarTextReporter cumulativeBarTextReporter = new CumulativeBarTextReporter();
		cumulativeBarTextReporter.print(extension.getOutputDirectory(), statisticsBean);

		NumberTextReporter numberTextReporter = new NumberTextReporter();
		numberTextReporter.print(extension.getOutputDirectory(), statisticsBean);
	}

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
