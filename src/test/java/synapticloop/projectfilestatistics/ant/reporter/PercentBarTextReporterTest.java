package synapticloop.projectfilestatistics.ant.reporter;

/*
 * Copyright (c) 2010-2011 synapticloop.
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

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import org.mockito.Mock;

import synapticloop.projectfilestatistics.bean.StatisticsBean;
import synapticloop.projectfilestatistics.reporter.PercentBarTextReporter;

import static org.mockito.Mockito.*;

public class PercentBarTextReporterTest {
	PercentBarTextReporter percentBarTextReporter;
	StatisticsBean statisticsBean;
	HashMap<String, Integer> mockHashMap;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		percentBarTextReporter = new PercentBarTextReporter();
		statisticsBean = mock(StatisticsBean.class);
		mockHashMap = mock(HashMap.class);
		when(mockHashMap.get(anyString())).thenReturn(new Integer(1));

		when(statisticsBean.getHashMapTotalLineCount()).thenReturn(mockHashMap);
		when(statisticsBean.getHashMapLineCodeCount()).thenReturn(mockHashMap);
		when(statisticsBean.getHashMapLineCommentCount()).thenReturn(mockHashMap);
		when(statisticsBean.getHashMapLineBlankCount()).thenReturn(mockHashMap);
	}

	@Test
	public void testPrint() {
		percentBarTextReporter.print("/tmp/", statisticsBean);
	}
}
