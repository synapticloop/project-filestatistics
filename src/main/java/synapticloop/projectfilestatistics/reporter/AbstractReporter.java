package synapticloop.projectfilestatistics.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import synapticloop.projectfilestatistics.bean.StatisticsBean;

public abstract class AbstractReporter {
	protected String outputDirectory = null;
	protected StatisticsBean statisticsBean = null;

	/**
	 * Print the results to either the file system or the console
	 * 
	 * @param outputDirectory the output directory to output the results to - if null, will output to the console
	 * @param statisticsBean the statistics for the project
	 */
	public void print(String outputDirectory, StatisticsBean statisticsBean) {

		this.outputDirectory = outputDirectory;
		this.statisticsBean = statisticsBean;
		// first check to see whether we have an output-directory
		if(null != outputDirectory) {
			printToFile();
		}
		printToConsole();
	}
	/**
	 * Print out the newly gleaned statistics to the console.  Format the
	 * output so that it all aligns nicely. 
	 * 
	 * <em>Note:</em> Yes, it would have been possible to use java.text.* 
	 * formatter classes, except for the overhead that is incurred in setting 
	 * things up.
	 */
	private void printToConsole() {
		System.out.println(generateOutput());
	}

	private void printToFile() {
		File file = new File(outputDirectory);
		file.mkdirs();

		try {
			FileUtils.writeStringToFile(new File(file, this.getClass().getSimpleName() + ".txt"), generateOutput());
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	protected abstract String generateOutput();
}
