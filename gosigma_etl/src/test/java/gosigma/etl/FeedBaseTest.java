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
	public static Logger log = LoggerFactory.getLogger(FeedBaseTest.class);
	static FeedBase _fb = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_fb = new FeedBase("Template") {
			@Override
			public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
				return null;
			}

			@Override
			public Logger getLogger() {
				return null;
			}
		};
		_fb.log = log;
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
		log.info("Entering...");
		String cols = ",,a, b, c , ,d,";
		List<Integer> lst = new ArrayList<>();
		String insCols = _fb.parseCols(cols, lst);

		assertArrayEquals(lst.toArray(), new Integer[] { 2, 3, 4, 6 });
		assertEquals("insert string not equal", insCols, "a,b,c,d");
		log.info("Leaving...");
	}

	@Test
	public void testParseArgs() {
		String[] args = new String[1];
		args[0] = "-lsdfs";
		try {
			System.out.println("args : " + args);
			System.out.println(args.length);
			_fb.parseArgs(args);
		} catch (EtlException e) {
			_fb.log.error("etl error", e);
			_fb.log.error(_fb.toString());
		}
	}
}
