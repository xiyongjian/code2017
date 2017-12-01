package gosigma.etl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedBaseTest {
	static FeedBase _fb = new FeedBase("Template") {

		@Override
		public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
			return null;
		}

		@Override
		public Logger getLogger() {
			log = LoggerFactory.getLogger(IESO_RealtimeConstTotals.class);
			return log;
		}

		{
			this.getLogger();
		}
	};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseCols() {
		String cols = ",,a, b, c , ,d,";
		List<Integer> lst = new ArrayList<>();
		String insCols = _fb.parseCols(cols, lst);

		assertArrayEquals(lst.toArray(), new Integer[] { 2, 3, 4, 6 });
		assertEquals("insert string not equal", insCols, "a,b,c,d");
	}

}
