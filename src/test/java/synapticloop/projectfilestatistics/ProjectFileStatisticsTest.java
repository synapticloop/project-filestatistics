package synapticloop.projectfilestatistics;

import org.junit.Before;
import org.junit.Test;

public class ProjectFileStatisticsTest {
	private ProjectFileStatistics projectFileStatistics;

	@Before
	public void setup() {
		projectFileStatistics = new ProjectFileStatistics();
	}

	@Test
	public void testOutput() {
		ProjectFileStatistics.main(new String[0]);
		ProjectFileStatistics.main(new String[] {"hello", "baby"});
		ProjectFileStatistics.main(new String[] {"something"});
	}

//	@Test
//	public void testUpperOutput() throws IOException {
//		ProjectFileStatistics.main(new String[] {"EXAMPLE"});
//	}
//
//	@Test
//	public void testLowerOutput() throws IOException {
//		ProjectFileStatistics.main(new String[] {"example"});
//	}
}
