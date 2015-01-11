package synapticloop.projectfilestatistics.ant.bean;

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

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import org.mockito.Mock;

import synapticloop.projectfilestatistics.ant.bean.StatisticsBean;

import static org.mockito.Mockito.*;

public class StatisticsBeanTest {
	StatisticsBean statisticsBean;

	@Before
	public void setup() {
		statisticsBean = new StatisticsBean();
	}
	
	@Test
	public void testUpdateCount() {
		statisticsBean.updateCount(".tmp", 100, 100, 100);
		assertEquals(100, statisticsBean.getMaxLineBlankCount());
		assertEquals(100, statisticsBean.getTotalLineBlankCount());
		statisticsBean.updateCount(".tmp", 100, 100, 100);
		statisticsBean.updateCount(".tmp", 200, 200, 200);
		statisticsBean.updateCount(".tmp", 50, 50, 50);
	}

	@Test
	public void testSetterGetter() {
		statisticsBean.setMaxExtensionLength(10);
		assertEquals(10, statisticsBean.getMaxExtensionLength());
		
		statisticsBean.setMaxFileCount(100);
		assertEquals(100, statisticsBean.getMaxFileCount());
		
		statisticsBean.setMaxLineBlankCount(150);
		assertEquals(150, statisticsBean.getMaxLineBlankCount());
		
		statisticsBean.setMaxLineCodeCount(175);
		assertEquals(175, statisticsBean.getMaxLineCodeCount());
		
		statisticsBean.setMaxLineCommentCount(208);
		assertEquals(208, statisticsBean.getMaxLineCommentCount());
		
		statisticsBean.setMaxLineCount(234);
		assertEquals(234, statisticsBean.getMaxLineCount());
		
		statisticsBean.setTotalFileCount(100);
		assertEquals(100, statisticsBean.getTotalFileCount());
		
		statisticsBean.setTotalLineBlankCount(150);
		assertEquals(150, statisticsBean.getTotalLineBlankCount());
		
		statisticsBean.setTotalLineCodeCount(175);
		assertEquals(175, statisticsBean.getTotalLineCodeCount());
		
		statisticsBean.setTotalLineCommentCount(208);
		assertEquals(208, statisticsBean.getTotalLineCommentCount());
		
		statisticsBean.setTotalLineCount(234);
		assertEquals(234, statisticsBean.getTotalLineCount());
		
		assertNotNull(statisticsBean.getHashMapLineBlankCount());
		assertNotNull(statisticsBean.getHashMapLineCodeCount());
		assertNotNull(statisticsBean.getHashMapLineCommentCount());
		assertNotNull(statisticsBean.getHashMapTotalFileCount());
		assertNotNull(statisticsBean.getHashMapTotalLineCount());

		statisticsBean.setHashMapLineBlankCount(new HashMap<String, Integer>());
		assertNotNull(statisticsBean.getHashMapLineBlankCount());
		statisticsBean.setHashMapLineCodeCount(new HashMap<String, Integer>());
		assertNotNull(statisticsBean.getHashMapLineCodeCount());
		statisticsBean.setHashMapLineCommentCount(new HashMap<String, Integer>());
		assertNotNull(statisticsBean.getHashMapLineCommentCount());
		statisticsBean.setHashMapTotalFileCount(new HashMap<String, Integer>());
		assertNotNull(statisticsBean.getHashMapTotalFileCount());
		statisticsBean.setHashMapTotalLineCount(new HashMap<String, Integer>());
		assertNotNull(statisticsBean.getHashMapTotalLineCount());
		
	}
}
