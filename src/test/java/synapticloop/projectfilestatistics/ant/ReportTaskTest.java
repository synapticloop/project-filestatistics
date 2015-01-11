package synapticloop.projectfilestatistics.ant;

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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import org.mockito.Mock;

import synapticloop.projectfilestatistics.ant.ReportTask;

import static org.mockito.Mockito.*;

public class ReportTaskTest {
	ReportTask projectFileStatistics;
	FileSet fileSet;
	Project project;
	DirectoryScanner directoryScanner;

	@Before
	public void setup() {
		projectFileStatistics = new ReportTask();
		fileSet = mock(FileSet.class);
		project = mock(Project.class);
		directoryScanner = mock(DirectoryScanner.class);

		projectFileStatistics.setProject(project);
		when(fileSet.getDirectoryScanner(project)).thenReturn(directoryScanner);
	}

	@Test
	public void testNullFileSets() {
		projectFileStatistics.addFileset(fileSet);
//		projectFileStatistics.execute();
	}

	@Test
	public void testGeneratePluginList() {
		projectFileStatistics.setPluginList("com.example.AverageReporter, does.not.exist");
		projectFileStatistics.execute();
	}

	@Test
	public void testSetterGetter() {
		projectFileStatistics.setDescription("This is a description");
		assertEquals("This is a description", projectFileStatistics.getDescription());
		
		projectFileStatistics.setIgnoreBinary(true);
		
		projectFileStatistics.setOutputDirectory("/tmp");
		projectFileStatistics.setOutputDirectory("/tmp");
		projectFileStatistics.setOutputDirectory("/tmp/something");
		
		projectFileStatistics.setPluginList("plugin.list");
		
		projectFileStatistics.setPropertyFile("some.properties");
	}
}
