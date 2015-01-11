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
import java.util.Iterator;
import java.util.Set;

public class PieChartImageReporter extends AbstractPieChartImageReporter {
	
	protected void printToFile() {
		// render the overall image
		renderImage(this.outputDirectory + "/" + this.getClass().getCanonicalName() + ".all.files.png", 
				"All Files", 
				statisticsBean.getTotalLineBlankCount(), 
				statisticsBean.getTotalLineCommentCount(), 
				statisticsBean.getTotalLineCodeCount(), 
				statisticsBean.getTotalLineCount());

		// now go through for each file-type and render the individual file.
		Set<String> fileExtensions = statisticsBean.getHashMapTotalFileCount().keySet();
		Iterator<String> fileExtensionsIterator = fileExtensions.iterator();
		while (fileExtensionsIterator.hasNext()) {
			String fileExtension = (String) fileExtensionsIterator.next();
			renderImage(this.outputDirectory + "/" + this.getClass().getCanonicalName() + "." + fileExtension + ".png", 
					"." + fileExtension + " Files", 
					statisticsBean.getHashMapLineBlankCount().get(fileExtension).intValue(), 
					statisticsBean.getHashMapLineCommentCount().get(fileExtension).intValue(), 
					statisticsBean.getHashMapLineCodeCount().get(fileExtension).intValue(), 
					statisticsBean.getHashMapTotalLineCount().get(fileExtension).intValue());
		}
		
	}

	/**
	 * Render the pie chart to the specified location.  This will output a .png
	 * image containing a title, a pie chart and key at the bottom.
	 * 
	 * @param fileOutputLocation the location of the file to write to
	 * @param title the title for the pie chart
	 * @param blank the number of blank lines
	 * @param comment the number of comment lines
	 * @param code the number of code lines
	 * @param total the total number of lines
	 */
	private void renderImage(String fileOutputLocation, String title, int blank, int comment, int code, int total) {

		BufferedImage bufferedImage = new BufferedImage(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D graphics2d = bufferedImage.createGraphics();

		// Draw the statistics
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double blankLines = (double)blank;
		double commentLines = (double)comment;
		double codeLines = (double)code;
		
		int totalLines = total;
		
		int blankSpread = (int)((blankLines/totalLines)*360);
		int commentSpread = (int)((commentLines/totalLines)*360);

		// draw the title
		drawTitle(graphics2d, title, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_PADDING);

		// draw blankLines
		drawArc(graphics2d, BLANK_DRAW_COLOUR, BLANK_FILL_COLOR, 0, blankSpread);
		drawArc(graphics2d, COMMENT_DRAW_COLOR, COMMENT_FILL_COLOR, blankSpread, commentSpread);
		drawArc(graphics2d, CODE_DRAW_COLOR, CODE_FILL_COLOR, blankSpread + commentSpread, 360 - (blankSpread + commentSpread));
		
		// draw the key
		drawKey(graphics2d, BLANK_DRAW_COLOUR, BLANK_FILL_COLOR, "Blank", (int)blankLines, (int)((blankLines/totalLines)*100), (DEFAULT_IMAGE_HEIGHT - DEFAULT_IMAGE_PADDING*2));
		drawKey(graphics2d, COMMENT_DRAW_COLOR, COMMENT_FILL_COLOR, "Comment", (int)commentLines, (int)((commentLines/totalLines)*100), (DEFAULT_IMAGE_HEIGHT - DEFAULT_IMAGE_PADDING*3));
		drawKey(graphics2d, CODE_DRAW_COLOR, CODE_FILL_COLOR, "Code", (int)codeLines, (int)((codeLines/totalLines)*100), (DEFAULT_IMAGE_HEIGHT - DEFAULT_IMAGE_PADDING*4));

		drawGeneratedByMessage(graphics2d, DEFAULT_IMAGE_PADDING, DEFAULT_IMAGE_HEIGHT-((int)DEFAULT_IMAGE_PADDING/2));

		graphics2d.dispose();
		
		// Write generated image to a file
		writePNGToFile(fileOutputLocation, bufferedImage);
	}
	
	/**
	 * Draw a single entry for the key to the pie chart
	 * 
	 * @param graphics2d the graphics context
	 * @param drawColor the colour to draw the square
	 * @param fillColor the colour to fill the square with
	 * @param keyTitle the title for the key
	 * @param number the number of lines
	 * @param percentage the percentage
	 * @param y the y location to draw the element
	 */
	private void drawKey(Graphics2D graphics2d, Color drawColor, Color fillColor, String keyTitle, int number, int percentage, int y) {
		// draw the pretty boxes for the key
		graphics2d.setColor(fillColor);
		graphics2d.fillRect(DEFAULT_IMAGE_PADDING, y, KEY_BOX_HEIGHT, KEY_BOX_WIDTH);
		graphics2d.setStroke(STROKE_2);
		graphics2d.setColor(drawColor);
		graphics2d.drawRect(DEFAULT_IMAGE_PADDING, y, KEY_BOX_HEIGHT, KEY_BOX_WIDTH);

		// now draw the keyTitle
		graphics2d.setColor(KEY_TITLE_COLOUR);
		graphics2d.setFont(KEY_TITLE_FONT);

		// draw the information at the starting y point with the height of the font 
		graphics2d.drawString(keyTitle + ": " + number + " lines", DEFAULT_IMAGE_PADDING *2, y + graphics2d.getFontMetrics().getHeight());
		
	}
	
}
