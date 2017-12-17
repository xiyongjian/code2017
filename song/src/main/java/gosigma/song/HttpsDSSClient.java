package gosigma.song;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Scanner;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpsDSSClient {
	public static void p(String msg) {
		System.out.println("-- " + msg);
	}

	public static String urlHttp = "http://localhost:8080";
	public static String urlHttps = "https://localhost:8443";
	public static String urlLogin = "login";
	public static String urlLogout = "logout";

	public static void main(String[] args) {
		p("https client, ssl certification, and keep session between http request");

		checkNYSIOssl2(null);
	}


	public static void checkNYSIOssl2(String url) {
		url = "https://dss.nyiso.com/dss/index.html#/public/login";
		p("\n checkNYSIOssl, url : " + url);

		try {
			String keyStorePwd = "t$Ft2017tyNexus";
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			//			InputStream instream = Thread.currentThread().getContextClassLoader()
			//					.getResourceAsStream(keystoreName);
			try (InputStream instream = new FileInputStream(
					"e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx")) {
				clientStore.load(instream, keyStorePwd.toCharArray());
			}

			//Trust everybody
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext sslCtx = SSLContext.getInstance("TLS");
			KeyManagerFactory kmfactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(clientStore, keyStorePwd != null ? keyStorePwd.toCharArray() : null);
			KeyManager[] keymanagers = kmfactory.getKeyManagers();
			sslCtx.init(keymanagers, new TrustManager[] { tm }, null);

			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
					sslCtx);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder
					.<ConnectionSocketFactory>create().register("https", sslConnectionFactory)
					.register("http", new PlainConnectionSocketFactory()).build();
			PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(
					registry);
			HttpClientBuilder hcb = HttpClientBuilder.create();
			hcb.setConnectionManager(pcm);
			CookieStore httpCookieStore = new BasicCookieStore();
			hcb.setDefaultCookieStore(httpCookieStore);

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("tangal1",
					"Nexus$2017aT");
			provider.setCredentials(AuthScope.ANY, credentials);

			try (CloseableHttpClient client = hcb.build()) {

				HttpClientContext context = HttpClientContext.create();
				// context.setCredentialsProvider(provider);

				HttpGet httpGet = new HttpGet(url);
				p("Executing request " + httpGet.getRequestLine());
				p("----------------------------------------");

				HttpResponse response = client.execute(httpGet, context);
				HttpHost target = context.getTargetHost();

				p("response status code : " + response.getStatusLine().getStatusCode());
				p("authCache : "
						+ (context.getAuthCache() != null ? context.getAuthCache().toString()
								: "null"));
				p("authState : " + context.getTargetAuthState());
				p("credsProvider : " + context.getCredentialsProvider().toString());
				p("credsProvider : " + context.getCredentialsProvider().getClass().getName());
				p("targetHost : " + context.getTargetHost().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				p("response content : " + (s.hasNext() ? s.next() : ""));
				p("response : \n" + dump(response));
				
				List<Cookie> cookies = httpCookieStore.getCookies();
				for (Cookie c : cookies) {
					p("cookie, " + c.getName() + " : " + c.getValue());
				}

				if (response.getStatusLine().getStatusCode() != 200) {
					p("response is not 200, something wrong");
				}

			}
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException
				| NoSuchAlgorithmException | CertificateException | UnsupportedOperationException
				| IOException e) {
			e.printStackTrace();
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

		return sb.toString();
	}
}