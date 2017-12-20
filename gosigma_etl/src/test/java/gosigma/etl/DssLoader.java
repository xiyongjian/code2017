package gosigma.etl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;

public class DssLoader {
	public Logger log = LoggerFactory.getLogger(DssLoader.class);

	public static void main(String[] args) throws JoranException, DataFormatException, EtlException {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");

		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");

		// System.setProperty("javax.net.debug", "all");
		System.setProperty("javax.net.debug", "ssl:handshake:verbose:keymanager:trustmanager");
		System.setProperty("javax.security.debug", "all");

		Utils.initLog();

		DssLoader dl = new DssLoader();
		// dl.checkDssSsl();
		// dl.checkDssSsl2();
		dl.downloadDss();

	}

	public void checkDssSsl() {
		// String url = "https://dss.nyiso.com/dss/index.html#/public/login";
		String url = "https://dss.nyiso.com/dss/login.jsp?user=tangal1&pass=Nexus$2017aT&automated=3";
		log.info("checkDssSsl, url : " + url);
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
				log.info("client class : " + client.getClass().getName());

				HttpClientContext context = HttpClientContext.create();
				// context.setCredentialsProvider(provider);

				HttpGet httpGet = null;
				HttpResponse response = null;
				HttpHost target = null;

				httpGet = new HttpGet(url);
				log.info("Executing request " + httpGet.getRequestLine());
				log.info("----------------------------------------");

				response = client.execute(httpGet, context);
				target = context.getTargetHost();

				log.info("response status code : " + response.getStatusLine().getStatusCode());
				log.info("authCache : "
						+ (context.getAuthCache() != null ? context.getAuthCache().toString()
								: "null"));
				log.info("authState : " + context.getTargetAuthState());
				log.info("credsProvider : " + context.getCredentialsProvider().toString());
				log.info("credsProvider : " + context.getCredentialsProvider().getClass().getName());
				log.info("targetHost : " + context.getTargetHost().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				log.info("response content : " + (s.hasNext() ? s.next() : ""));
				log.info("response : \n" + dump(response));
				log.info("context cookie : " + context.getCookieStore().toString());

				if (response.getStatusLine().getStatusCode() == 401) {
					log.info("\n response 401, try it again with credential");

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
					log.info("response status code : " + response.getStatusLine().getStatusCode());
					log.info("authCache : "
							+ (context.getAuthCache() != null ? context.getAuthCache().toString()
									: "null"));
					log.info("authState : " + context.getTargetAuthState());
					log.info("credsProvider : " + context.getCredentialsProvider().toString());
					log.info("credsProvider : " + context.getCredentialsProvider().getClass().getName());
					log.info("targetHost : " + context.getTargetHost().toString());

					s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					log.info("response content : " + (s.hasNext() ? s.next() : ""));

				}

				url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=CSV";
				log.info("access url : " + url);
				httpGet = new HttpGet(url);
				httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

				log.info("Executing request " + httpGet.getRequestLine());
				log.info("----------------------------------------");

				//				client.addResponseInterceptor(new HttpResponseInterceptor() {
				//
				//					public void process(
				//					        final HttpResponse response,
				//					        final HttpContext context) throws HttpException, IOException {
				//					    HttpEntity entity = response.getEntity();
				//					    if (entity != null) {
				//					        InputStream instream = entity.getContent();
				//					        if (instream instanceof ChunkedInputStream) {
				//					            Header[] footers = ((ChunkedInputStream) instream).getFooters();
				//					        }
				//					    }
				//					}
				//				});
				response = client.execute(httpGet, context);
				// response = client.execute(httpGet);
				//				log.info("response type : " + response.getEntity().getClass().getName());
				//				log.info("encoding elements : " + response.getEntity().getContentEncoding().getElements().toString());
				//				for (HeaderElement e : response.getEntity().getContentEncoding().getElements()) {
				//					log.info("encoding element : " + e.getName() + ", " + e.getValue());
				//				}

				log.info("content length : " + response.getEntity().getContentLength());
				InputStream in = response.getEntity().getContent();
				byte[] data = new byte[in.available()];
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < data.length; ++i) {
					sb.append(String.format("%02X %c | ", data[i], data[i]));
					if ((i % 20) == 0)
						sb.append("\n");
				}
				log.info("Content/binary : " + sb.toString());

				target = context.getTargetHost();
				log.info("response : " + dump(response));

				//				try (ZipInputStream zin = new ZipInputStream(response.getEntity().getContent())) {
				//					ZipEntry ze = null;
				//					while ((ze = zin.getNextEntry()) != null) {
				//						log.info("Unzipping " + ze.getName());
				//						//					        FileOutputStream fout = new FileOutputStream(ze.getName());
				//						//					        for (int c = zin.read(); c != -1; c = zin.read()) {
				//						//					          fout.write(c);
				//						//					        }
				//						//					        fout.close();
				//						
				//						StringBuilder sb = new StringBuilder();
				//						int c;
				//						while ((c = zin.read()) >0) {
				//							sb.append((char)c);
				//						}
				//						log.info("entry content : " + sb.toString());
				//						
				//						zin.closeEntry();
				//					}
				//				}

			}
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	// using DefaultHttpClient instead of HttpClientBuilder's method
	public void checkDssSsl2() throws DataFormatException {
		String url = "https://dss.nyiso.com/dss/login.jsp?user=tangal1&pass=Nexus$2017aT&automated=3";
		log.info("checkDssSsl2, url : " + url);
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
			//			hcb.addInterceptorFirst(new HttpResponseInterceptor() {
			//				@Override
			//				public void process(final HttpResponse response,
			//						final HttpContext context) throws HttpException, IOException {
			//					log.info("Entering....");
			//					HttpEntity entity = response.getEntity();
			//					if (entity != null) {
			//						log.info("entity is not null");
			//						InputStream instream = entity.getContent();
			//						log.info("instream is : " + instream.getClass().getName());
			//						if (instream instanceof ChunkedInputStream) {
			//							log.info("instream is ChunkedInputStream");
			//							Header[] footers = ((ChunkedInputStream) instream).getFooters();
			//						}
			//					}
			//				}
			//			});

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("tangal1",
					"Nexus$2017aT");
			provider.setCredentials(AuthScope.ANY, credentials);

			try (CloseableHttpClient client = hcb./* disableContentCompression(). */build()) {
				log.info("client class : " + client.getClass().getName());

				HttpClientContext context = HttpClientContext.create();
				// context.setCredentialsProvider(provider);

				HttpGet httpGet = null;
				HttpResponse response = null;
				HttpHost target = null;

				httpGet = new HttpGet(url);
				log.info("Executing request " + httpGet.getRequestLine());
				log.info("----------------------------------------");

				response = client.execute(httpGet, context);
				target = context.getTargetHost();

				log.info("response status code : " + response.getStatusLine().getStatusCode());
				log.info("authCache : "
						+ (context.getAuthCache() != null ? context.getAuthCache().toString()
								: "null"));
				log.info("authState : " + context.getTargetAuthState());
				log.info("credsProvider : " + context.getCredentialsProvider().toString());
				log.info("credsProvider : " + context.getCredentialsProvider().getClass().getName());
				log.info("targetHost : " + context.getTargetHost().toString());
				Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
				log.info("response content : " + (s.hasNext() ? s.next() : ""));
				log.info("response : \n" + dump(response));
				log.info("context cookie : " + context.getCookieStore().toString());

				if (response.getStatusLine().getStatusCode() == 401) {
					log.info("\n response 401, try it again with credential");

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
					log.info("response status code : " + response.getStatusLine().getStatusCode());
					log.info("authCache : "
							+ (context.getAuthCache() != null ? context.getAuthCache().toString()
									: "null"));
					log.info("authState : " + context.getTargetAuthState());
					log.info("credsProvider : " + context.getCredentialsProvider().toString());
					log.info("credsProvider : " + context.getCredentialsProvider().getClass().getName());
					log.info("targetHost : " + context.getTargetHost().toString());

					s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					log.info("response content : " + (s.hasNext() ? s.next() : ""));

				}

				url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=CSV";
				// url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=XML";
				{
					// URLConnection connection = new URL(url).openConnection();
				}
				log.info("access url : " + url);
				httpGet = new HttpGet(url);
				// httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

				log.info("Executing request " + httpGet.getRequestLine());
				log.info("----------------------------------------");

				//				client.addResponseInterceptor(new HttpResponseInterceptor() {
				//
				//					public void process(
				//					        final HttpResponse response,
				//					        final HttpContext context) throws HttpException, IOException {
				//					    HttpEntity entity = response.getEntity();
				//					    if (entity != null) {
				//					        InputStream instream = entity.getContent();
				//					        if (instream instanceof ChunkedInputStream) {
				//					            Header[] footers = ((ChunkedInputStream) instream).getFooters();
				//					        }
				//					    }
				//					}
				//				});
				response = client.execute(httpGet, context);
				// response = client.execute(httpGet);
				//				log.info("response type : " + response.getEntity().getClass().getName());
				//				log.info("encoding elements : " + response.getEntity().getContentEncoding().getElements().toString());
				//				for (HeaderElement e : response.getEntity().getContentEncoding().getElements()) {
				//					log.info("encoding element : " + e.getName() + ", " + e.getValue());
				//				}

				// String ba = EntityUtils.toString((response.getEntity()));
				{
					String responseCharset = EntityUtils.getContentCharSet(response.getEntity());
					String contentType = EntityUtils.getContentMimeType(response.getEntity());
					log.info("entity charset : " + responseCharset);
					log.info("entity type : " + contentType);
				}
				if (false) {
					byte[] ba = EntityUtils.toByteArray((response.getEntity()));
					Inflater decompressor = new Inflater();
					decompressor.setInput(ba);
					ByteArrayOutputStream bos = new ByteArrayOutputStream(ba.length);
					byte[] buf = new byte[1024];
					while (!decompressor.finished()) {
						int count = decompressor.inflate(buf);
						bos.write(buf, 0, count);

					}
					bos.close();
					byte[] decompressedData = bos.toByteArray();
				}

				log.info("content length : " + response.getEntity().getContentLength());
				InputStream in = response.getEntity().getContent();
				byte[] data = new byte[in.available()];
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < data.length; ++i) {
					sb.append(String.format("%02X %c | ", data[i], data[i]));
					if ((i % 20) == 0)
						sb.append("\n");
				}
				log.info("Content/binary : " + sb.toString());

				target = context.getTargetHost();
				log.info("response : " + dump(response));

				{
					// GZIPInputStream gin = new GZIPInputStream(response.getEntity().getContent());
					byte[] ba = EntityUtils.toByteArray((response.getEntity()));
					GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(ba));
					//					InputStream decompressor = new InflaterInputStream(gin);
					// InputStream gin = response.getEntity().getContent();
					int c;
					while ((c = gin.read()) != -1) {
						log.info("read char : " + (char) c);
					}
				}

				//				try (ZipInputStream zin = new ZipInputStream(response.getEntity().getContent())) {
				//					ZipEntry ze = null;
				//					while ((ze = zin.getNextEntry()) != null) {
				//						log.info("Unzipping " + ze.getName());
				//						//					        FileOutputStream fout = new FileOutputStream(ze.getName());
				//						//					        for (int c = zin.read(); c != -1; c = zin.read()) {
				//						//					          fout.write(c);
				//						//					        }
				//						//					        fout.close();
				//						
				//						StringBuilder sb = new StringBuilder();
				//						int c;
				//						while ((c = zin.read()) >0) {
				//							sb.append((char)c);
				//						}
				//						log.info("entry content : " + sb.toString());
				//						
				//						zin.closeEntry();
				//					}
				//				}

			}
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listSystemProperties() {
		log.info("Entering....");
		Properties props = System.getProperties();
		Set<Object> keys = new TreeSet(props.keySet());
		for (Object key : keys) {
			Object value = props.get(key);
			log.info("property " + key + " : " + value);
		}
	}

	// using DefaultHttpClient instead of HttpClientBuilder's method
	public void downloadDss() throws EtlException {
		try {
			String url = "https://dss.nyiso.com/dss/login.jsp?user=tangal1&pass=Nexus$2017aT&automated=3";
			log.info("downloadDss, url : " + url);

			try (CloseableHttpClient client = this.createDssClientBuilder().build()) {
				log.info("client class : " + client.getClass().getName());

				HttpGet httpGet = null;
				HttpResponse response = null;
				HttpHost target = null;
				HttpClientContext context = HttpClientContext.create();

				httpGet = new HttpGet(url);
				log.info("Executing request " + httpGet.getRequestLine());
				response = client.execute(httpGet, context);
				log.info("response status code : " + response.getStatusLine().getStatusCode());

				url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=CSV";
				log.info("access url : " + url);
				httpGet = new HttpGet(url);
				response = client.execute(httpGet, context);

				log.info("content length : " + response.getEntity().getContentLength());
				StringBuilder sb = new StringBuilder();
				{
					// GZIPInputStream gin = new GZIPInputStream(response.getEntity().getContent());
					byte[] ba = EntityUtils.toByteArray((response.getEntity()));
					GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(ba));
					int c;
					while ((c = gin.read()) != -1) {
						log.info("read char : " + (char) c);
						sb.append((char)c);
					}
				}
				log.info("unzipped content : " + sb.toString());
			}
		} catch (UnsupportedOperationException | IOException e) {
			throw new EtlException("download dss feed", e);
		}
	}

	public HttpClientBuilder createDssClientBuilder() throws EtlException {
		//			listSystemProperties();
		//			System.setProperty("javax.net.ssl.keyStore", "e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx");
		//			System.setProperty("javax.net.ssl.keyStorePassword", "t$Ft2017tyNexus");
		//			System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		//			listSystemProperties();

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
			// sslCtx.init(keymanagers, new TrustManager[] { tm }, null);
			sslCtx.init(keymanagers, null, null);

			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
					sslCtx);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder
					.<ConnectionSocketFactory>create().register("https", sslConnectionFactory)
					.register("http", new PlainConnectionSocketFactory()).build();
			PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(
					registry);
			HttpClientBuilder hcb = HttpClientBuilder.create();
			hcb.setConnectionManager(pcm);
			return hcb;
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			throw new EtlException("can create DssClientBuilder", e);
		}
	}
}
