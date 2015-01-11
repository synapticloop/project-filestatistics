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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import synapticloop.projectfilestatistics.util.Constants;

public class AbstractImageReporter extends AbstractReporter {
	// the default image dimensions
	protected static final int DEFAULT_IMAGE_WIDTH = 400;
	protected static final int DEFAULT_IMAGE_HEIGHT = 525;
	protected static final int DEFAULT_IMAGE_PADDING = 25;

	// the colours of the text
	protected static final Color KEY_TITLE_COLOUR = Color.black;
	private static final Color GENERATED_BY_COLOUR = Color.black;
	private static final Color TITLE_COLOUR = Color.black;

	// the code colours
	protected static final Color CODE_FILL_COLOR = new Color(0, 0, 255, 128);
	protected static final Color CODE_DRAW_COLOR = new Color(0, 0, 255, 255);
	
	// the comment colours
	protected static final Color COMMENT_FILL_COLOR = new Color(0, 255, 0, 128);
	protected static final Color COMMENT_DRAW_COLOR = new Color(0, 255, 0, 255);
	
	// the blank colours
	protected static final Color BLANK_FILL_COLOR = new Color(200, 200, 200, 128);
	protected static final Color BLANK_DRAW_COLOUR = new Color(200, 200, 200, 255);

	// the fonts used
	protected static final Font KEY_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
	protected static final Font GENERATED_BY_FONT = new Font("SansSerif", Font.ITALIC, 10);
	protected static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
	protected static final Font TICK_TITLE_FONT = new Font("SansSerif", Font.BOLD, 12);

	protected static final int KEY_BOX_WIDTH = 16;
	protected static final int KEY_BOX_HEIGHT = KEY_BOX_WIDTH;
	
	// some strokes
	final static float dash[] = {5.0f};
	protected static final BasicStroke STROKE_DASHED_2 = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
	protected static final BasicStroke STROKE_2 = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
	protected static final BasicStroke STROKE_1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

	
	protected void printToConsole() {
		// do nothing
	}
	
	/**
	 * Draw the title of the pie chart
	 * 
	 * @param graphics2d The graphics context
	 * @param title the title to draw
	 * @param width the image width
	 * @param y the y dimension
	 */
	protected void drawTitle(Graphics2D graphics2d, String title, int width, int y) {
		graphics2d.setFont(TITLE_FONT);
		graphics2d.setColor(TITLE_COLOUR);
		graphics2d.drawString(title, (width-graphics2d.getFontMetrics().stringWidth(title))/2, y);
	}
	
	protected void drawKeyElement(Graphics2D graphics2d, String title, Color drawColour, Color fillColour, int x, int y) {
		graphics2d.setColor(fillColour);
		graphics2d.fillRect(x, y, KEY_BOX_WIDTH, KEY_BOX_HEIGHT);

		graphics2d.setStroke(STROKE_2);
		graphics2d.setColor(drawColour);
		graphics2d.drawRect(x, y, KEY_BOX_WIDTH, KEY_BOX_HEIGHT);
		
		// now draw the title
		graphics2d.setColor(KEY_TITLE_COLOUR);
		graphics2d.setFont(KEY_TITLE_FONT);

		// draw the information at the starting y point with the height of the font 
		graphics2d.drawString(title, x + KEY_BOX_WIDTH*2, y + graphics2d.getFontMetrics().getHeight());
	}

	
	protected void drawGeneratedByMessage(Graphics2D graphics2d, int x, int y) {
		graphics2d.setColor(GENERATED_BY_COLOUR);
		graphics2d.setFont(GENERATED_BY_FONT);
		graphics2d.drawString(Constants.GENERATED_BY, x, y);
	}

	protected void writePNGToFile(String fileOutputLocation, BufferedImage bufferedImage) {
		try {
			// Save as PNG
			File file = new File(fileOutputLocation);
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException jiioex) {
			System.out.println("FATAL: Could not write image to file " + fileOutputLocation + ", ignoring.");
		}
		
	}

	protected void printToFile() { 
		// do nothing
	}
}
