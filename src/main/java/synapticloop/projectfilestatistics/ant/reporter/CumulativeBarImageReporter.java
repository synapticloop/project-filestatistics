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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class CumulativeBarImageReporter extends AbstractImageReporter {
	/**
	 * 
	 */
	private static final String CHART_TITLE = "Cumulative Percentages";
	private static final int CHART_PADDING = 25;
	private static final int IMAGE_PADDING = 25;
	
	private static final int AREA_WIDTH = 50;
	private static final int AREA_HEIGHT = 600;
	private static final int AREA_PADDING = 25;
	
	private static final int KEY_WIDTH = 200;
	
	private static final int BORDER_PADDING = 75;
	
	private static final String AVERAGE_TITLE = "All";
	
	private static final double TICK_TITLE_ROTATION = 270;
	@SuppressWarnings("unchecked")
	protected void printToFile() {
		// find out how many we need to render (+1 for the overall statistics)
		
		int numAreas = statisticsBean.getHashMapTotalFileCount().size() + 1;
		
		int imageWidth = numAreas*(AREA_WIDTH + AREA_PADDING) + AREA_PADDING + IMAGE_PADDING*2 + BORDER_PADDING + KEY_WIDTH;
		
		// we need to work out the image height as well
		
		// dummy image for font metrics calculations
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D graphics2d = bufferedImage.createGraphics();
		
		int longestTextTitleHeight = calculateLongestTextHeight(graphics2d);
		
		int imageHeight = IMAGE_PADDING*2 + AREA_HEIGHT + CHART_PADDING + longestTextTitleHeight + CHART_PADDING; 

		// reset the image height and graphics
		bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB_PRE);
		graphics2d = bufferedImage.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, imageWidth, imageHeight);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawTitle(graphics2d, CHART_TITLE, imageWidth, IMAGE_PADDING);
		
		// generate the average images
		
		// now draw the bars for the average over all files
		// code average
		double totalLines = (double)statisticsBean.getTotalLineCount();
		double blankLines = (double)statisticsBean.getTotalLineBlankCount();
		double commentLines = (double)statisticsBean.getTotalLineCommentCount();
		double codeLines = (double)statisticsBean.getTotalLineCodeCount();
		
		// draw the borders for the chart
		
		drawChartBorders(graphics2d, imageWidth);

		int codeHeight = (int)((codeLines/totalLines)*AREA_HEIGHT);
		int commentHeight = (int)((commentLines/totalLines)*AREA_HEIGHT);
		
		drawAverageLine(graphics2d, 
				CODE_DRAW_COLOR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + codeHeight + CHART_PADDING));
		// comment average
		drawAverageLine(graphics2d, 
				COMMENT_DRAW_COLOR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + codeHeight + commentHeight + CHART_PADDING));
		// blank average
		drawAverageLine(graphics2d, 
				BLANK_DRAW_COLOUR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + AREA_HEIGHT + CHART_PADDING));
		
		// draw the overall bar chart
		
		drawCumulativeBarCharts(graphics2d, AVERAGE_TITLE, 0, codeLines, commentLines, blankLines, totalLines, imageWidth, imageHeight);
		
		// now go through and draw the cumulative bar charts

		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();

		// now sort the set
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(set);

		Iterator iter = treeSet.iterator(); 

		// go through each of the file extensions and print out the statistics
		int i = 1;
		while(iter.hasNext()) {
			String key = (String)iter.next();
			double total = (double)statisticsBean.getHashMapTotalLineCount().get(key).intValue();
			double blank = (double)statisticsBean.getHashMapLineBlankCount().get(key).intValue();
			double comment = (double)statisticsBean.getHashMapLineCommentCount().get(key).intValue();
			double code = (double)statisticsBean.getHashMapLineCodeCount().get(key).intValue();

			drawCumulativeBarCharts(graphics2d, "." + key, i*(AREA_WIDTH + AREA_PADDING), code, comment, blank, total, imageWidth, imageHeight);
			i++;
		}

		
		drawGeneratedByMessage(graphics2d, IMAGE_PADDING + BORDER_PADDING, imageHeight-((int)IMAGE_PADDING/2));
		
		// now generate the key
		
		drawKeyElement(graphics2d, "Code %", 
				CODE_DRAW_COLOR, 
				CODE_FILL_COLOR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING);
		drawKeyElement(graphics2d, 
				"Comment %", 
				COMMENT_DRAW_COLOR, 
				COMMENT_FILL_COLOR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING + 40);
		drawKeyElement(graphics2d, 
				"Blank %", 
				BLANK_DRAW_COLOUR, 
				BLANK_FILL_COLOR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING + 80);
		
		drawAverageKeyElement(graphics2d, 
				"Average Code %", 
				CODE_DRAW_COLOR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING + 160);
		
		drawAverageKeyElement(graphics2d, 
				"Average Comment %", 
				COMMENT_DRAW_COLOR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING + 200);

		drawAverageKeyElement(graphics2d, 
				"Average Blank %", 
				BLANK_DRAW_COLOUR, 
				imageWidth-IMAGE_PADDING-KEY_WIDTH + CHART_PADDING, 
				IMAGE_PADDING+CHART_PADDING + 240);

		// dispose of the graphics object
		graphics2d.dispose();
		
		// Write generated image to a file
		String fileOutputLocation = this.outputDirectory + "/" + this.getClass().getCanonicalName() + ".png";

		writePNGToFile(fileOutputLocation, bufferedImage);
	}
	
	private void drawAverageKeyElement(Graphics2D graphics2d, String title, Color drawColour, int x, int y) {
		graphics2d.setStroke(STROKE_DASHED_2);
		graphics2d.setColor(drawColour);
		
		graphics2d.drawLine(x, y, x+ KEY_BOX_WIDTH, y);

		// now draw the title
		graphics2d.setColor(KEY_TITLE_COLOUR);
		graphics2d.setFont(KEY_TITLE_FONT);

		// draw the information at the starting y point with the height of the font 
		graphics2d.drawString(title, x + KEY_BOX_WIDTH*2, y + graphics2d.getFontMetrics().getHeight()/2);
}

	private int calculateLongestTextHeight(Graphics2D graphics2d) {
		Set<String> set = statisticsBean.getHashMapTotalFileCount().keySet();
		graphics2d.setFont(TICK_TITLE_FONT);
		Iterator<String> iterator = set.iterator();
		int maxWidth = 0;
		while (iterator.hasNext()) {
			String title = (String) iterator.next();
			int width = graphics2d.getFontMetrics().stringWidth("." + title);
			if(width > maxWidth) {
				maxWidth = width;
			}
		}
		
		// finally check the AVERAGE_TITLE
		int width = graphics2d.getFontMetrics().stringWidth(AVERAGE_TITLE);
		if(width > maxWidth) {
			maxWidth = width;
		}
		
		return maxWidth;
	}
	
	private void drawCumulativeBarCharts(Graphics2D graphics2d, String title, int offset, double code, double comment, double blank, double total, int imageWidth, int imageHeight) {
		graphics2d.setStroke(STROKE_2);

		int blankHeight = (int)((blank/total)*AREA_HEIGHT);
		int commentHeight = (int)((comment/total)*AREA_HEIGHT);
		int codeHeight = (int)((code/total)*AREA_HEIGHT);

		graphics2d.setColor(BLANK_FILL_COLOR);
		graphics2d.fillRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING, 
				AREA_WIDTH, 
				blankHeight);
		graphics2d.setColor(BLANK_DRAW_COLOUR);
		graphics2d.drawRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING, 
				AREA_WIDTH, 
				blankHeight);

		graphics2d.setColor(COMMENT_FILL_COLOR);
		graphics2d.fillRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING + AREA_HEIGHT - commentHeight - codeHeight, 
				AREA_WIDTH, 
				commentHeight);
		graphics2d.setColor(COMMENT_DRAW_COLOR);
		graphics2d.drawRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING + AREA_HEIGHT - commentHeight - codeHeight, 
				AREA_WIDTH, 
				commentHeight);

		graphics2d.setColor(CODE_FILL_COLOR);
		graphics2d.fillRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING + AREA_HEIGHT - codeHeight, 
				AREA_WIDTH, 
				codeHeight);
		graphics2d.setColor(CODE_DRAW_COLOR);
		graphics2d.drawRect(IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset, 
				IMAGE_PADDING + CHART_PADDING + AREA_HEIGHT - codeHeight, 
				AREA_WIDTH, 
				codeHeight);
		
		graphics2d.setFont(TICK_TITLE_FONT);
		graphics2d.setColor(KEY_TITLE_COLOUR);

		// now draw the title in a separate image
		BufferedImage titleImage = new BufferedImage(graphics2d.getFontMetrics().stringWidth(title),
				graphics2d.getFontMetrics().stringWidth(title), 
				BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D imageGraphics2d = titleImage.createGraphics();
		int titleImageWidth = graphics2d.getFontMetrics().stringWidth(title);

		imageGraphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		imageGraphics2d.transform(AffineTransform.getRotateInstance(Math.toRadians(TICK_TITLE_ROTATION), titleImageWidth/2, titleImageWidth/2));
		imageGraphics2d.setFont(TICK_TITLE_FONT);
		imageGraphics2d.setColor(KEY_TITLE_COLOUR);
		imageGraphics2d.drawString(title, 0, graphics2d.getFontMetrics().getHeight()-3);
		
		// draw the title image to the original image
    graphics2d.drawImage(titleImage, 
    		null, 
    		IMAGE_PADDING + BORDER_PADDING + AREA_PADDING + offset + graphics2d.getFontMetrics().getHeight(),
    		IMAGE_PADDING + CHART_PADDING + AREA_HEIGHT + CHART_PADDING/2);
    imageGraphics2d.dispose();

	}
	
	// draw the chart borders and tick marks
	private void drawChartBorders(Graphics2D graphics2d, int imageWidth) {
		graphics2d.setColor(Color.black);
		graphics2d.setStroke(STROKE_2);
		graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING, 
				IMAGE_PADDING + CHART_PADDING, 
				IMAGE_PADDING + BORDER_PADDING, 
				AREA_HEIGHT + CHART_PADDING + IMAGE_PADDING);
		// now draw the tick marks and labels
		int j = 100;
		for (int i = 0; i <= AREA_HEIGHT; i += ((int)AREA_HEIGHT/10)) {
			graphics2d.setStroke(STROKE_2);
			graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING - 12, 
					IMAGE_PADDING + CHART_PADDING + i, 
					IMAGE_PADDING + BORDER_PADDING, 
					IMAGE_PADDING + CHART_PADDING + i);
			
			// now draw the tick marks
			graphics2d.setFont(TICK_TITLE_FONT);
			String percentageMark = j + "%";
			graphics2d.drawString(percentageMark ,IMAGE_PADDING + BORDER_PADDING - graphics2d.getFontMetrics().stringWidth(percentageMark) - 22 , 
					IMAGE_PADDING + CHART_PADDING + i + graphics2d.getFontMetrics().getHeight()/2 -3);
			j -= 10;
			// draw the final line
			graphics2d.setStroke(STROKE_1);
			graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING, 
					IMAGE_PADDING + CHART_PADDING + i, 
					imageWidth - IMAGE_PADDING - KEY_WIDTH, 
					IMAGE_PADDING + CHART_PADDING + i);
		}
	}
	
	private void drawAverageLine(Graphics2D graphics2d, Color drawColour, int fromX, int toX, int y) {
		graphics2d.setColor(drawColour);
		graphics2d.setStroke(STROKE_DASHED_2);
		graphics2d.drawLine(fromX, y, toX, y);
	}
}
