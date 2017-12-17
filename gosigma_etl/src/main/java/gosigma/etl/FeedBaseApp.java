package gosigma.etl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedBaseApp extends FeedBase {

	public FeedBaseApp() {
		super("FeedBaseTest");
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		return null;
	}

	@Override
	public Logger getLogger() {
		this.log = LoggerFactory.getLogger(IESO_RealtimeConstTotals.class);
		return this.log;
	}

	public static void main(String[] args) {
		FeedBase fb = new FeedBaseApp();
		fb.getLogger();
		try {
			System.out.println("args : " + args);
			System.out.println(args.length);
			fb.parseArgs(args);
		} catch (EtlException e) {
			fb.log.error("etl error", e);
			fb.log.error(fb.toString());
			System.exit(1); // something wrong, will record in cron log
		}
	}
}

/*
command line options
-c,--cron                     run as cron job
-d,--directory <OUTPUT DIR>   output file location
   --debug                    debug mode
-h,--help                     usage
-i,--input <FEED FILE>        input feed file
-p,--parse                    parse feed file to SQL only
*/
