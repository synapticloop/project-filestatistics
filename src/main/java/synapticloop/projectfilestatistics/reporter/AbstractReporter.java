package synapticloop.projectfilestatistics.reporter;

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

	protected abstract void printToConsole();

	protected abstract void printToFile();
}
