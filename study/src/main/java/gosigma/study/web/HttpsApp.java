package gosigma.study.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpsApp {
	public static void p(String msg) {
		System.out.println("-- " + msg);
	}

	public static String urlHttp = "http://localhost:8080";
	public static String urlHttps = "https://localhost:8443";
	public static String urlLogin = "login";
	public static String urlLogout = "logout";

	public static void main(String[] args) {
		p("https client, ssl certification, and keep session between http request");
		// httpLiveTest(urlHttp);
		// httpLiveTest(urlHttps);

		// acceptAll_v43(urlHttp);
		// acceptAll_v43(urlHttps);

		// String url = "http://httpbin.org/redirect/3";
		// redirect(url);
		// redirect(urlHttp);
		// redirect(urlHttps);

		// acceptAll(urlHttp);
		// acceptAll(urlHttps);

		// acceptAllWithContext(urlHttp);
		// acceptAllWithContext(urlHttps);

		// acceptAllWithCredential(urlHttp);
		// acceptAllWithCredential(urlHttps);
		// acceptAllWithCredential(urlHttp + "/beans");
		// acceptAllWithCredential(urlHttps + "/beans");

		// acceptAllWithCredentialPreemptive(urlHttp, "https", "localhost", 8443);
		// acceptAllWithCredentialPreemptive(urlHttps, "https", "localhost", 8443);

		// checkAuthInfo(urlHttps);
		// checkNYSIOssl(null);
		checkNYSIOssl2(null);
	}

	public static void httpLiveTest(String url) {
		p("\nhttpLiveTest, url : " + url);
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(url);

			HttpResponse response = httpClient.execute(getMethod);
			p("response status code : " + response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void acceptAll_v43(String url) {
		p("\nacceptAll_v43, url : " + url);
		try {
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 8443, sf));
			ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

			DefaultHttpClient httpClient = new DefaultHttpClient(ccm);
			// enable redirect to https??
			httpClient.setRedirectStrategy(new LaxRedirectStrategy());

			String urlOverHttps = url;
			HttpGet getMethod = new HttpGet(urlOverHttps);

			HttpResponse response = httpClient.execute(getMethod);

			p("response status code : " + response.getStatusLine().getStatusCode());
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException
				| KeyStoreException | IOException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void redirect(String url) {
		p("\nredirect, url : " + url);

		try (CloseableHttpClient httpclient = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy()).build()) {
			HttpClientContext context = HttpClientContext.create();
			HttpGet httpGet = new HttpGet(url);
			p("Executing request " + httpGet.getRequestLine());
			p("----------------------------------------");

			httpclient.execute(httpGet, context);
			HttpHost target = context.getTargetHost();
			List<URI> redirectLocations = context.getRedirectLocations();
			URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
			p("Final HTTP location: " + location.toASCIIString());
		} catch (IOException | URISyntaxException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void acceptAll(String url) {
		p("\nacceptAll, url : " + url);
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
				HttpGet httpGet = new HttpGet(url);
				// httpGet.setHeader("Accept", "application/xml");
				HttpResponse response = client.execute(httpGet);

				p("response status code : " + response.getStatusLine().getStatusCode());
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException
				| IOException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void acceptAllWithContext(String url) {
		p("\nacceptAllWithContext, url : " + url);
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {

				HttpClientContext context = HttpClientContext.create();
				HttpGet httpGet = new HttpGet(url);
				p("Executing request " + httpGet.getRequestLine());
				p("----------------------------------------");

				// httpGet.setHeader("Accept", "application/xml");
				HttpResponse response = client.execute(httpGet, context);
				p("response status code : " + response.getStatusLine().getStatusCode());
				p("response content : " + response.getEntity().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				p("response content : " + (s.hasNext() ? s.next() : ""));

				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				if (redirectLocations != null)
					p("redirection : " + String.join("\n", redirectLocations.stream()
							.map(u -> u.toString()).collect(Collectors.toList())));
				URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
				p("Final HTTP location: " + location.toASCIIString());

			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException
				| URISyntaxException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void acceptAllWithCredential(String url) {
		p("\nacceptAllWithCredential, url : " + url);
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin",
					"password");
			provider.setCredentials(AuthScope.ANY, credentials);

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setDefaultCredentialsProvider(provider)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {

				HttpClientContext context = HttpClientContext.create();
				HttpGet httpGet = new HttpGet(url);
				p("Executing request " + httpGet.getRequestLine());
				p("----------------------------------------");

				// httpGet.setHeader("Accept", "application/xml");
				p("before : context's auth cache : " + context.getAuthCache());
				HttpResponse response = client.execute(httpGet, context);
				p("response status code : " + response.getStatusLine().getStatusCode());
				p("response content : " + response.getEntity().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				p("response content : " + (s.hasNext() ? s.next() : ""));
				p("after : context's auth cache : " + context.getAuthCache());
				p("cache realm: " + context.getAuthCache().get(context.getTargetHost()).getRealm());
				p("cache scheme name: "
						+ context.getAuthCache().get(context.getTargetHost()).getSchemeName());
				p("cache target host: " + context.getTargetHost().getHostName());
				p("cache target host schema: " + context.getTargetHost().getSchemeName());

				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				if (redirectLocations != null)
					p("redirection : " + String.join("\n", redirectLocations.stream()
							.map(u -> u.toString()).collect(Collectors.toList())));
				URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
				p("Final HTTP location: " + location.toASCIIString());

			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException
				| URISyntaxException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void acceptAllWithCredentialPreemptive(String url, String protocol, String host,
			int port) {
		p(String.format("\n acceptAllWithCredentialPreemptive(), %s, %s, %s, %d", url, protocol,
				host, port));
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			HttpHost targetHost = new HttpHost(host, port, protocol);

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin",
					"password");
			// provider.setCredentials(AuthScope.ANY, credentials);
			provider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
					credentials);

			// setup preemptive authentication
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {

				// Add AuthCache to the execution context
				HttpClientContext context = HttpClientContext.create();
				context.setCredentialsProvider(provider);
				context.setAuthCache(authCache);

				HttpGet httpGet = new HttpGet(url);
				p("Executing request " + httpGet.getRequestLine());
				p("----------------------------------------");

				// httpGet.setHeader("Accept", "application/xml");
				p("before : context's auth cache : " + context.getAuthCache());
				HttpResponse response = client.execute(httpGet, context);
				p("response status code : " + response.getStatusLine().getStatusCode());
				p("response content : " + response.getEntity().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				p("response content : " + (s.hasNext() ? s.next() : ""));
				p("after : context's auth cache : " + context.getAuthCache());
				p("cache realm: " + context.getAuthCache().get(context.getTargetHost()).getRealm());
				p("cache scheme name: "
						+ context.getAuthCache().get(context.getTargetHost()).getSchemeName());
				p("cache target host: " + context.getTargetHost().getHostName());
				p("cache target host schema: " + context.getTargetHost().getSchemeName());

				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				if (redirectLocations != null)
					p("redirection : " + String.join("\n", redirectLocations.stream()
							.map(u -> u.toString()).collect(Collectors.toList())));
				URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
				p("Final HTTP location: " + location.toASCIIString());

			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException
				| URISyntaxException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void drafting() {
		try {
			org.apache.http.impl.client.DefaultHttpClient client0 = new org.apache.http.impl.client.DefaultHttpClient();

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://mkyong.com");
			HttpResponse response = client.execute(request);
			// ...
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkAuthInfo(String url) {
		p("\n checkAuthInfo, url : " + url);
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, (certificate, authType) -> true).build();

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
				HttpClientContext context = HttpClientContext.create();
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

				// AuthState authState = (AuthState)
				// context.getAttribute(ClientContext.TARGET_AUTH_STATE);
				// p("authState : " + authState.toString());
				// CredentialsProvider credsProvider = (CredentialsProvider) context
				// .getAttribute(ClientContext.CREDS_PROVIDER);
				// p("credsProvider : " + credsProvider.toString());
				// HttpHost targetHost = (HttpHost)
				// context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				// p("targetHost : " + targetHost.toString());

				if (response.getStatusLine().getStatusCode() == 401) {
					p("\n response 401, try it again with credential");

					// setup preemptive authentication
					// Create AuthCache instance
					AuthCache authCache = new BasicAuthCache();
					// Generate BASIC scheme object and add it to the local auth cache
					BasicScheme basicAuth = new BasicScheme();
					authCache.put(context.getTargetHost(), basicAuth);
					context.setAuthCache(authCache);

					// context.getCredentialsProvider().setCredentials(AuthScope.ANY,
					// new UsernamePasswordCredentials("admin", "password"));
					context.getCredentialsProvider().setCredentials(
							new AuthScope(context.getTargetHost()),
							new UsernamePasswordCredentials("admin", "password"));
					// provider.setCredentials(AuthScope.ANY, credentials);
					response = client.execute(httpGet, context);
					p("response status code : " + response.getStatusLine().getStatusCode());
					p("authCache : "
							+ (context.getAuthCache() != null ? context.getAuthCache().toString()
									: "null"));
					p("authState : " + context.getTargetAuthState());
					p("credsProvider : " + context.getCredentialsProvider().toString());
					p("credsProvider : " + context.getCredentialsProvider().getClass().getName());
					p("targetHost : " + context.getTargetHost().toString());

					s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					p("response content : " + (s.hasNext() ? s.next() : ""));
				}
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException
				| IOException e) {
			p(e.toString());
			e.printStackTrace(System.out);
		}
	}

	public static void checkNYSIOssl(String url) {
		url = "https://dss.nyiso.com/dss/index.html#/public/login";
		// url = "https://www.w3schools.com/html/";	// random testing
		p("\n checkNYSIOssl, url : " + url);

		p("setup certificate and password");
		System.setProperty("javax.net.ssl.keyStore",
				"e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx");
		System.setProperty("javax.net.ssl.keyStorePassword", "t$Ft2017tyNexus");
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

		try {

			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			//			InputStream instream = Thread.currentThread().getContextClassLoader()
			//					.getResourceAsStream(keystoreName);
			try (InputStream instream = new FileInputStream(
					"e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx")) {
				clientStore.load(instream, "t$Ft2017tyNexus".toCharArray());
				// clientStore.load(instream, keyStorePwd.toCharArray());
			}

			// support to use above keystore
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(clientStore, (certificate, authType) -> true).build();
			// .loadTrustMaterial((certificate, authType) -> true).build();
			// .loadTrustMaterial(null, (certificate, authType) -> true).build();

			try (CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
				HttpClientContext context = HttpClientContext.create();
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

				// AuthState authState = (AuthState)
				// context.getAttribute(ClientContext.TARGET_AUTH_STATE);
				// p("authState : " + authState.toString());
				// CredentialsProvider credsProvider = (CredentialsProvider) context
				// .getAttribute(ClientContext.CREDS_PROVIDER);
				// p("credsProvider : " + credsProvider.toString());
				// HttpHost targetHost = (HttpHost)
				// context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				// p("targetHost : " + targetHost.toString());

				if (response.getStatusLine().getStatusCode() == 401) {
					p("\n response 401, try it again with credential");

					// setup preemptive authentication
					// Create AuthCache instance
					AuthCache authCache = new BasicAuthCache();
					// Generate BASIC scheme object and add it to the local auth cache
					BasicScheme basicAuth = new BasicScheme();
					authCache.put(context.getTargetHost(), basicAuth);
					context.setAuthCache(authCache);

					// context.getCredentialsProvider().setCredentials(AuthScope.ANY,
					// new UsernamePasswordCredentials("admin", "password"));
					context.getCredentialsProvider().setCredentials(
							new AuthScope(context.getTargetHost()),
							new UsernamePasswordCredentials("admin", "password"));
					// provider.setCredentials(AuthScope.ANY, credentials);
					response = client.execute(httpGet, context);
					p("response status code : " + response.getStatusLine().getStatusCode());
					p("authCache : "
							+ (context.getAuthCache() != null ? context.getAuthCache().toString()
									: "null"));
					p("authState : " + context.getTargetAuthState());
					p("credsProvider : " + context.getCredentialsProvider().toString());
					p("credsProvider : " + context.getCredentialsProvider().getClass().getName());
					p("targetHost : " + context.getTargetHost().toString());

					s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					p("response content : " + (s.hasNext() ? s.next() : ""));
				}
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException
				| CertificateException | IOException e) {
			e.printStackTrace();
			p(e.toString());
			e.printStackTrace(System.out);
		}
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

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("tangal1",
					"Nexus$2017aT");
			provider.setCredentials(AuthScope.ANY, credentials);

			try (CloseableHttpClient client = hcb.build()) {

				HttpClientContext context = HttpClientContext.create();
				context.setCredentialsProvider(provider);

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

				if (response.getStatusLine().getStatusCode() == 401) {
					p("\n response 401, try it again with credential");

					// setup preemptive authentication
					// Create AuthCache instance
					AuthCache authCache = new BasicAuthCache();
					// Generate BASIC scheme object and add it to the local auth cache
					BasicScheme basicAuth = new BasicScheme();
					authCache.put(context.getTargetHost(), basicAuth);
					context.setAuthCache(authCache);

					// context.getCredentialsProvider().setCredentials(AuthScope.ANY,
					// new UsernamePasswordCredentials("admin", "password"));
					context.getCredentialsProvider().setCredentials(
							new AuthScope(context.getTargetHost()),
							new UsernamePasswordCredentials("admin", "password"));
					// provider.setCredentials(AuthScope.ANY, credentials);
					response = client.execute(httpGet, context);
					p("response status code : " + response.getStatusLine().getStatusCode());
					p("authCache : "
							+ (context.getAuthCache() != null ? context.getAuthCache().toString()
									: "null"));
					p("authState : " + context.getTargetAuthState());
					p("credsProvider : " + context.getCredentialsProvider().toString());
					p("credsProvider : " + context.getCredentialsProvider().getClass().getName());
					p("targetHost : " + context.getTargetHost().toString());

					s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					p("response content : " + (s.hasNext() ? s.next() : ""));

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

		//		try {
		//			Scanner s = new Scanner(res.getEntity().getContent()).useDelimiter("\\A");
		//			String content = (s.hasNext() ? s.next() : "");
		//			sb.append("\ncontent : " + content);
		//
		//			ObjectMapper mapper = new ObjectMapper();
		//			Object json = mapper.readValue(content, Object.class);
		//			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		//			sb.append("\ncontent json : " + indented);
		//		} catch (IOException e) {
		//			sb.append("\ncontent : null");
		//		}

		return sb.toString();
	}
}
