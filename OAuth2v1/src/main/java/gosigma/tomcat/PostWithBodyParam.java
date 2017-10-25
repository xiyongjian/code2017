package gosigma.tomcat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
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

import com.google.api.client.util.IOUtils;

public class PostWithBodyParam {
	public static void main(String[] args) throws IOException {
		post01();
		post02();
		post03();

		post01a();
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

	public static void post01a() throws IOException {
		System.out.println("\n post01a(), another method try to post with body");
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

        con.setChunkedStreamingMode(0);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        con.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(urlParameters.getBytes());
		wr.write(urlParameters.getBytes());

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
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			String theString = baos.toString();
			System.out.println("response : " + theString);
			
//			byte[] buffer = new byte[1024];
//			int read = 0;
//			while ((read = in.read(buffer, 0, buffer.length)) != -1) {
//				System.out.println("read : " + read);
//				System.out.println(new String(buffer));
//			}		

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

	public static void post03() throws IOException {

		try {
			System.out.println("post03");

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://localhost:8080/OAuth2v1/PostTesting?name=hello&num=8888888");
			// HttpPost httpPost = new HttpPost("http://localhost:8080/OAuth2v1/PostTesting");
		 
		    String json = "{\"id\":1,\"name\":\"John\"}";
		    StringEntity entity = new StringEntity(json);
		    httpPost.setEntity(entity);
		    httpPost.setHeader("Accept", "application/json");
		    httpPost.setHeader("Content-type", "application/json");
		    
		    System.out.println("httpPost toString(): " + httpPost.toString());
		 
			HttpResponse response = httpClient.execute(httpPost);
			InputStream in = response.getEntity().getContent();
			// System.out.println(getStringFromInputStream(in));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			String theString = baos.toString();
			System.out.println("response : " + theString);
			
//			byte[] buffer = new byte[1024];
//			int read = 0;
//			while ((read = in.read(buffer, 0, buffer.length)) != -1) {
//				System.out.println("read : " + read);
//				System.out.println(new String(buffer));
//			}		

			System.out.println("post03 done");
			// handle response here...

		} catch (Exception ex) {

			// handle exception here

		} finally {
			// Deprecated
			// httpClient.getConnectionManager().shutdown();
		}
	}
}
