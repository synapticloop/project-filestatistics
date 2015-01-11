package synapticloop.projectfilestatistics.ant.reporter;

import java.awt.Color;
import java.awt.Graphics2D;

/*
 * Copyright (c) 2009-2011 Synapticloop
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

/**
 * @author osmanj
 *
 */
public class AbstractPieChartImageReporter extends AbstractImageReporter {
	/**
	 * Draw the pie chart arc and fill it with the passed in colour.
	 * 
	 * @param graphics2d The graphics context
	 * @param drawColour the colour to draw the outside of the arc
	 * @param fillColour the colour to fill the arc
	 * @param from the start degree angle 
	 * @param to the end degree angle
	 */
	protected void drawArc(Graphics2D graphics2d, Color drawColour, Color fillColour, int from, int to) {
		graphics2d.setColor(fillColour);
		graphics2d.fillArc(DEFAULT_IMAGE_PADDING, DEFAULT_IMAGE_PADDING*2, DEFAULT_IMAGE_WIDTH-(2*DEFAULT_IMAGE_PADDING), DEFAULT_IMAGE_WIDTH-(2*DEFAULT_IMAGE_PADDING), from, to);
		graphics2d.setColor(drawColour);
		graphics2d.setStroke(STROKE_2);
		graphics2d.drawArc(DEFAULT_IMAGE_PADDING, DEFAULT_IMAGE_PADDING*2, DEFAULT_IMAGE_WIDTH-(2*DEFAULT_IMAGE_PADDING), DEFAULT_IMAGE_WIDTH-(2*DEFAULT_IMAGE_PADDING), from, to);
	}

}
