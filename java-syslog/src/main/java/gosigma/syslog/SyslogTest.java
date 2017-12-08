package gosigma.syslog;

import java.awt.*;
import java.util.*;
import java.applet.*;

class SyslogTest {
	static public void main(String argv[]) {
		SyslogApplet applet = new SyslogApplet();

		Frame frame = new SyslogFrame("Syslog Test", applet, 20, 20, 375, 175);
	}
}
