package gosigma.tomcat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class App {
	public static void main(String[] args) throws IOException {
		post01();
		post02();
	}

	public static void post01() throws IOException {
		String url = "http://localhost:8080/OAuth2v1/PostTesting";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		String body = "hello, world, and ti's body";
		String urlParameters = "name=helloworld&sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Testing");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Referer", "Local Testing");
		// con.setRequestProperty("Content-Length", Integer.toString(body.length()));

		String boundary = "===" + System.currentTimeMillis() + "===";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.write(body.getBytes("UTF8"));
		// wr.writeBytes(body);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	public static void post02() throws IOException {

		try {
			System.out.println("post02");

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://localhost:8080/OAuth2v1/PostTesting?name=hello&num=8888888");

			post.setHeader("Content-type", "application/json");
			post.setHeader("Referer", "Local Testing");

	        String xml = "<xml>xxxx</xml>";
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
	        post.setEntity(entity);

			HttpResponse response = httpClient.execute(post);
			InputStream in = response.getEntity().getContent();
			// System.out.println(getStringFromInputStream(in));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = in.read(buffer, 0, buffer.length)) != -1) {
				System.out.println("read : " + read);
				System.out.println(new String(buffer));
			}		

			System.out.println("post02 done");
			// handle response here...

		} catch (Exception ex) {

			// handle exception here

		} finally {
			// Deprecated
			// httpClient.getConnectionManager().shutdown();
		}
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
}