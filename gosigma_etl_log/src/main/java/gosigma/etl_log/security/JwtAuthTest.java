package gosigma.etl_log.security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthTest {
	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) {
		getApiAuth();
	}

	public static void getApiAuth() {
		p("getApiAuth");
		String url = "https://localhost:8443/api/authentication";
		// String urlGet = "https://localhost:8443/beans";
		String urlGet = "http://localhost:8080/beans";

		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin",
					"password");
			provider.setCredentials(AuthScope.ANY, credentials);

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
				// Add AuthCache to the execution context
				HttpClientContext context = HttpClientContext.create();

				p("----------------------------------------");
				p("get : " + urlGet);
				HttpGet hget = new HttpGet(urlGet);
				HttpResponse response = client.execute(hget, context);
				p(dump(response));
				{
					HttpHost target = context.getTargetHost();
					List<URI> redirectLocations = context.getRedirectLocations();
					URI location = URIUtils.resolve(hget.getURI(), target, redirectLocations);
					p("Final HTTP location: " + location.toASCIIString());
				}

				context.setCredentialsProvider(provider);
				// HttpGet http = new HttpGet(url);
				HttpPost http = new HttpPost(url);
				//	if (false) {
				//		http.setEntity(new StringEntity("test post"));
				//		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin",
				//				"password");
				//		http.addHeader(new BasicScheme().authenticate(creds, http, null));
				//	}

				p("----------------------------------------");
				p("Executing request " + http.getRequestLine());

				// http.setHeader("Accept", "application/xml");
				p("before : context's auth cache : " + context.getAuthCache());
				response = client.execute(http, context);

				p(dump(response));
				p("after : context's auth cache : " + context.getAuthCache());
				//				p("cache realm: " + context.getAuthCache().get(context.getTargetHost()).getRealm());
				//				p("cache scheme name: "
				//						+ context.getAuthCache().get(context.getTargetHost()).getSchemeName());
				//				p("cache target host: " + context.getTargetHost().getHostName());
				//				p("cache target host schema: " + context.getTargetHost().getSchemeName());

				{
					HttpHost target = context.getTargetHost();
					List<URI> redirectLocations = context.getRedirectLocations();
					if (redirectLocations != null)
						p("redirection : " + String.join("\n", redirectLocations.stream()
								.map(u -> u.toString()).collect(Collectors.toList())));
					URI location = URIUtils.resolve(http.getURI(), target, redirectLocations);
					p("Final HTTP location: " + location.toASCIIString());
				}

				p("get JWT : " + response.getHeaders("Authorization")[0].getValue());

				// get again with JWT token
				{
					p("----------------------------------------");
					p("use JWT get again : " + urlGet);
					hget = new HttpGet(urlGet);
					hget.addHeader("Authorization",
							response.getHeaders("Authorization")[0].getValue());
					response = client.execute(hget, context);
					p(dump(response));
					//	p("convert content to json");
					//	Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					//	String jsonString = s.hasNext() ? s.next() : "";
					//	p("content : " + jsonString);
					//	if (!jsonString.equals("")) {
					//		ObjectMapper mapper = new ObjectMapper();
					//		Object json = mapper.readValue(jsonString.replaceAll("^.|.$", ""), Object.class);
					//		String indented = mapper.writerWithDefaultPrettyPrinter()
					//				.writeValueAsString(json);
					//		p("json : " + indented);
					//	}
					{
						HttpHost target = context.getTargetHost();
						List<URI> redirectLocations = context.getRedirectLocations();
						URI location = URIUtils.resolve(hget.getURI(), target, redirectLocations);
						p("Final HTTP location: " + location.toASCIIString());
					}

				}
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException
				| UnsupportedOperationException | IOException | URISyntaxException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static String dump(HttpResponse res) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nHttpResponse : " + res.toString());
		sb.append("\nresponse status code : " + res.getStatusLine().getStatusCode());
		for (Header h : res.getAllHeaders())
			sb.append("\nheader " + h.getName() + " : " + h.getValue());
		try {
			Scanner s = new Scanner(res.getEntity().getContent()).useDelimiter("\\A");
			String content = (s.hasNext() ? s.next() : "");
			sb.append("\ncontent : " + content);

			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(content, Object.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			sb.append("\ncontent json : " + indented);
		} catch (IOException e) {
			sb.append("\ncontent : null");
		}

		return sb.toString();
	}
}
