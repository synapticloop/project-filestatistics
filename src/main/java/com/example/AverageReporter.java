package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import synapticloop.projectfilestatistics.ant.reporter.AbstractReporter;
import synapticloop.projectfilestatistics.util.PrintHelper;
import synapticloop.projectfilestatistics.util.PrintfFormat;

/**
 * This is a simple example class that reports on the average number of lines 
 * per file.
 */
public class AverageReporter extends AbstractReporter {
	private static final String AVERAGE_LINES_FILE = "Average lines/file:";
	private static final String TOTAL_LINES = "Total # lines:     ";
	private static final String TOTAL_FILES = "Total # files:     ";

	private StringBuilder stringBuilder = null;

	@Override
	protected void printToConsole() {
		System.out.println(getOutput().toString());
	}

	@Override
	protected void printToFile() {
		String fileOutputLocation = outputDirectory + "/" + this.getClass().getName() + ".txt";
		try {
			// Save as PNG
			File file = new File(fileOutputLocation);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(getOutput().toString() + "\n");
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException jiioex) {
			System.out.println("FATAL: Could not write file " + fileOutputLocation + ", ignoring.");
		}
	}
	
	/**
	 * Generate the output for the reporter and buffer it for second usage
	 *  
	 * @return The stringBuilder containing the output.
	 */
	private StringBuilder getOutput() {
		if(stringBuilder == null) {
			int length = new String(statisticsBean.getTotalLineCount() + "").length();
			stringBuilder = new StringBuilder();
			stringBuilder.append(TOTAL_FILES + " " + new PrintfFormat("%" + length + "i").sprintf(statisticsBean.getTotalFileCount()) + "\n");
			stringBuilder.append(TOTAL_LINES + " " + new PrintfFormat("%" + length + "i").sprintf(statisticsBean.getTotalLineCount()) + "\n");
			stringBuilder.append(PrintHelper.underline('-', AVERAGE_LINES_FILE.length()) + " " + PrintHelper.underline('-', length) + "\n");

			int averageLinesFile = 0;
			if(statisticsBean.getTotalFileCount() != 0) {
				averageLinesFile = statisticsBean.getTotalLineCount()/statisticsBean.getTotalFileCount();
			}

			stringBuilder.append(AVERAGE_LINES_FILE + " " + new PrintfFormat("%" + length + "i").sprintf(averageLinesFile) +"\n");
			stringBuilder.append(PrintHelper.underline('=', AVERAGE_LINES_FILE.length()) + " " + PrintHelper.underline('=', length));
		}
		return (stringBuilder);
	}
}
