package synapticloop.projectfilestatistics.plugin;

/*
 * Copyright (c) 2011-2016 Synapticloop.
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
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;

import synapticloop.projectfilestatistics.ant.bean.StatisticsBean;
import synapticloop.projectfilestatistics.util.PropertyManager;

@SuppressWarnings("rawtypes")
public class ProjectWalker extends DirectoryWalker {
	private StatisticsBean statisticsBean = new StatisticsBean();

	public ProjectWalker(FileFilter filter, int depthLimit) {
		super(filter, depthLimit);
		// TODO Auto-generated constructor stub
	}

	public ProjectWalker(IOFileFilter directoryFilter, IOFileFilter fileFilter, int depthLimit) {
		super(directoryFilter, fileFilter, depthLimit);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gather statistics on the specified file adding these to the totals.
	 * 
	 * @param file the file to gather statistics on
	 */
	public void recordFileStatistics(File startDirectory) {

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

}
