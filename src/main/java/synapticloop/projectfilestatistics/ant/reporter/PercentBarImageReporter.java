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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PercentBarImageReporter extends AbstractImageReporter {
	private static final int CHART_TOP_PADDING = 25;
	private static final int IMAGE_PADDING = 25;
	
	private static final int AREA_WIDTH = 50;
	private static final int AREA_HEIGHT = 600;
	private static final int AREA_PADDING = 25;
	
	private static final int BORDER_PADDING = 75;
	
	protected void printToFile() {
		// find out how many we need to render (+1 for the overall statistics)
		
		int numAreas = statisticsBean.getHashMapTotalFileCount().size() + 1;
		
		int imageWidth = numAreas*(AREA_WIDTH + AREA_PADDING) + AREA_PADDING + IMAGE_PADDING*2 + BORDER_PADDING;
		
		// we need to work out the image height as well
		
		int longestTextTitleHeight = 100;
		
		int imageHeight = IMAGE_PADDING*2 + AREA_HEIGHT + CHART_TOP_PADDING + longestTextTitleHeight; 

		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D graphics2d = bufferedImage.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, imageWidth, imageHeight);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawTitle(graphics2d, "All Files", imageWidth, IMAGE_PADDING);
		
		// now draw the bars for the average over all files
		// code average
		double totalLines = (double)statisticsBean.getTotalLineCount();
		double commentLines = (double)statisticsBean.getTotalLineCommentCount();
		double codeLines = (double)statisticsBean.getTotalLineCodeCount();
		
		// draw the borders for the chart
		
		drawChartBorders(graphics2d, imageWidth);

		int codeHeight = (int)((codeLines/totalLines)*AREA_HEIGHT);
		int commentHeight = (int)((commentLines/totalLines)*AREA_HEIGHT);
		
		drawAverageLine(graphics2d, 
				CODE_DRAW_COLOR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + codeHeight));
		// comment average
		drawAverageLine(graphics2d, 
				COMMENT_DRAW_COLOR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + codeHeight + commentHeight));
		// blank average
		drawAverageLine(graphics2d, 
				BLANK_DRAW_COLOUR, 
				IMAGE_PADDING + BORDER_PADDING + 3, 
				imageWidth-IMAGE_PADDING, 
				imageHeight - (IMAGE_PADDING + longestTextTitleHeight + AREA_HEIGHT));
		
		
		drawGeneratedByMessage(graphics2d, IMAGE_PADDING + BORDER_PADDING, imageHeight-((int)IMAGE_PADDING/2));
		
		// now generate the key

		graphics2d.dispose();
		
		// Write generated image to a file
		String fileOutputLocation = this.outputDirectory + "/" + this.getClass().getCanonicalName() + ".png";
		try {
			// Save as PNG
			File file = new File(fileOutputLocation);
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException jiioex) {
			System.out.println("FATAL: Could not write image to file " + fileOutputLocation + ", ignoring.");
		}

	}

	/**
	 * Draw the chart borders and tick marks.
	 * 
	 * @param graphics2d the graphics to draw to
	 * @param imageWidth the width of the image
	 */
	private void drawChartBorders(Graphics2D graphics2d, int imageWidth) {
		graphics2d.setColor(Color.black);
		graphics2d.setStroke(STROKE_2);
		graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING, 
				IMAGE_PADDING + CHART_TOP_PADDING, 
				IMAGE_PADDING + BORDER_PADDING, 
				AREA_HEIGHT + CHART_TOP_PADDING + IMAGE_PADDING);
		// now draw the tick marks and labels
		int j = 100;
		for (int i = 0; i <= AREA_HEIGHT; i += ((int)AREA_HEIGHT/10)) {
			graphics2d.setStroke(STROKE_2);
			graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING - 12, 
					IMAGE_PADDING + CHART_TOP_PADDING + i, 
					IMAGE_PADDING + BORDER_PADDING, 
					IMAGE_PADDING + CHART_TOP_PADDING + i);
			
			// now draw the tick marks
			graphics2d.setFont(KEY_TITLE_FONT);
			String percentageMark = j + "%";
			graphics2d.drawString(percentageMark ,IMAGE_PADDING + BORDER_PADDING - graphics2d.getFontMetrics().stringWidth(percentageMark) - 22 , 
					IMAGE_PADDING + CHART_TOP_PADDING + i + graphics2d.getFontMetrics().getHeight()/2 -3);
			j -= 10;
			// draw the final line
			graphics2d.setStroke(STROKE_1);
			graphics2d.drawLine(IMAGE_PADDING + BORDER_PADDING, 
					IMAGE_PADDING + CHART_TOP_PADDING + i, 
					imageWidth - IMAGE_PADDING, 
					IMAGE_PADDING + CHART_TOP_PADDING + i);
		}
	}

	/**
	 * Draw an average line on the chart
	 *  
	 * @param graphics2d the graphics to draw to
	 * @param drawColour the colour to draw the line in
	 * @param fromX the x co-ordinate to draw from
	 * @param toX the x co-ordinate to draw to
	 * @param y the y co-ordinate to draw
	 */
	private void drawAverageLine(Graphics2D graphics2d, Color drawColour, int fromX, int toX, int y) {
		graphics2d.setColor(drawColour);
		graphics2d.setStroke(STROKE_DASHED_2);
		graphics2d.drawLine(fromX, y, toX, y);
	}
}
