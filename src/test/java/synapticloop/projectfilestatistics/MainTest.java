package synapticloop.projectfilestatistics;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
	private Main projectFileStatistics;

	@Before
	public void setup() {
		projectFileStatistics = new Main();
	}

	@Test
	public void testOutput() {
		Main.main(new String[0]);
		Main.main(new String[] {"hello", "baby"});
		Main.main(new String[] {"something"});
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
