package gosigma.etl;

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
import java.util.zip.GZIPInputStream;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;

public class UtilsHttp {
	public static Logger log = UtilsLog.getLogger(UtilsHttp.class);

	public static CloseableHttpClient createDssHttpClient() throws EtlException {
		try {
			//			listSystemProperties();
			//			System.setProperty("javax.net.ssl.keyStore", "e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx");
			//			System.setProperty("javax.net.ssl.keyStorePassword", "t$Ft2017tyNexus");
			//			System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
			//			listSystemProperties();

			String keyStorePwd = "t$Ft2017tyNexus";
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			String keyStoreFile = "e:\\tmp\\001\\NYSIO\\NexusEnergy Aug 12, 2017.pfx";
			//			InputStream instream = Thread.currentThread().getContextClassLoader()
			//					.getResourceAsStream(keystoreName);
			try (InputStream instream = new FileInputStream(keyStoreFile)) {
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
			return hcb.build();
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			throw new EtlException("create Dss Http Client", e);
		}
	}

	public static void main(String[] args) throws JoranException {
		// TODO Auto-generated method stub
		UtilsLog.resetLogger(true);

		log.info("setup system properties..");
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
		//
		//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
		//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
		//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
		//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
		//
		//		// System.setProperty("javax.net.debug", "all");
		//		System.setProperty("javax.net.debug", "ssl:handshake:verbose:keymanager:trustmanager");
		//		System.setProperty("javax.security.debug", "all");

		testDownload();
	}

	public static void testDownload() {

		try (CloseableHttpClient client = UtilsHttp.createDssHttpClient()) {
			HttpClientContext context = HttpClientContext.create();

			String url = "https://dss.nyiso.com/dss/login.jsp?user=tangal1&pass=Nexus$2017aT&automated=3";
			log.info("downloadDss, url : " + url);

			HttpGet httpGet = new HttpGet(url);
			log.info("Executing request " + httpGet.getRequestLine());
			HttpResponse response = client.execute(httpGet, context);
			log.info("response status code : " + response.getStatusLine().getStatusCode());
			log.info("targetHost : " + context.getTargetHost().toString());

			if (response.getStatusLine().getStatusCode() != 200) {
				log.info("response is not 200, something wrong");
			}

			url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=CSV";
			log.info("access url : " + url);
			httpGet = new HttpGet(url);
			response = client.execute(httpGet, context);
			log.info("response status code : " + response.getStatusLine().getStatusCode());
			log.info("targetHost : " + context.getTargetHost().toString());

			log.info("content length : " + response.getEntity().getContentLength());
			StringBuilder sb = new StringBuilder();
			{
				GZIPInputStream gin = new GZIPInputStream(response.getEntity().getContent());
				int c;
				while ((c = gin.read()) != -1) {
					sb.append((char) c);
				}
			}
			log.info("unzipped content : " + sb.toString());

		} catch (IOException | EtlException e) {
			log.info("", e);
		}
	}

	public static void testDownloadVerbose() {

		try (CloseableHttpClient client = UtilsHttp.createDssHttpClient()) {
			HttpClientContext context = HttpClientContext.create();
			// context.setCredentialsProvider(provider);

			String url = "https://dss.nyiso.com/dss/login.jsp?user=tangal1&pass=Nexus$2017aT&automated=3";
			log.info("downloadDss, url : " + url);

			HttpGet httpGet = new HttpGet(url);
			log.info("Executing request " + httpGet.getRequestLine());
			HttpResponse response = client.execute(httpGet, context);
			HttpHost target = context.getTargetHost();

			log.info("response status code : " + response.getStatusLine().getStatusCode());
			log.info("authCache : "
					+ (context.getAuthCache() != null ? context.getAuthCache().toString()
							: "null"));
			log.info("authState : " + context.getTargetAuthState());
			log.info("credsProvider : " + context.getCredentialsProvider().toString());
			log.info("credsProvider : " + context.getCredentialsProvider().getClass().getName());
			log.info("targetHost : " + context.getTargetHost().toString());

			if (response.getStatusLine().getStatusCode() != 200) {
				log.info("response is not 200, something wrong");
			}

			url = "https://dss.nyiso.com/dss/scripts/dssCADDRetreiveFile.jsp?ID=24904556&fileName=xyj.csv&fileType=CSV";
			log.info("access url : " + url);
			httpGet = new HttpGet(url);
			response = client.execute(httpGet, context);

			log.info("content length : " + response.getEntity().getContentLength());
			StringBuilder sb = new StringBuilder();
			{
				GZIPInputStream gin = new GZIPInputStream(response.getEntity().getContent());
				//				byte[] ba = EntityUtils.toByteArray((response.getEntity()));
				//				GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(ba));
				int c;
				while ((c = gin.read()) != -1) {
					// log.info("read char : " + (char) c);
					sb.append((char) c);
				}
			}
			log.info("unzipped content : " + sb.toString());

		} catch (IOException | EtlException e) {
			log.info("", e);
		}
	}
}
