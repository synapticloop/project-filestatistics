package synapticloop.projectfilestatistics.util;

/*
 * Copyright (c) 2010 synapticloop.
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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import org.mockito.Mock;

import synapticloop.projectfilestatistics.util.PrintHelper;

import static org.mockito.Mockito.*;

public class PrintHelperTest {
	PrintHelper printHelper;

	@Before
	public void setup() {
		
	}

	@Test
	public void testUnderline() {
		String underline = PrintHelper.underline('-', 10);
		assertEquals("----------", underline);

		underline = PrintHelper.underline('=', 10);
		assertEquals("==========", underline);
	}
	
	@Test
	public void testUnderlineText() {
		String underline = PrintHelper.underlineText("Underline this", '-', true);
		assertEquals("Underline this\n--------- ----", underline);

		underline = PrintHelper.underlineText("Underline this", '-', false);
		assertEquals("Underline this\n--------------", underline);
	}
}
