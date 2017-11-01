package gosigma.song;

import org.openqa.selenium.WebDriver;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;

// this one have some error, can't solved. use htmlUnit + Jsoup
public class JBrowserDriverExample {
	public static void main(String[] args) {

		try {
		// You can optionally pass a Settings object here,
		// constructed using Settings.Builder
		JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.AMERICA_NEWYORK).build());

		// This will block for the page load and any
		// associated AJAX requests
		//driver.get("http://example.com");
		// driver.get("http://google.com");
		// driver.get("http://www.wenxuecity.com/");
		driver.get("http://localhost:8080/webapp/dynamic.html");

		// You can get status code unlike other Selenium drivers.
		// It blocks for AJAX requests and page loads after clicks
		// and keyboard events.
		System.out.println(driver.getStatusCode());

		// Returns the page source in its current state, including
		// any DOM updates that occurred after page load
		System.out.println(driver.getPageSource());

		// Close the browser. Allows this thread to terminate.
		driver.quit();
		}
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}