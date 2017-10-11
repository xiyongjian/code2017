package com.danter.google.auth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.locks.Lock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;

/**
 * Servlet implementation class OAuthVerifier
 */
@WebServlet(description = "OAuth verifier for Google, Facebook, Wechat, Microsoft", urlPatterns = { "/OAuthVerifier" })
public class OAuthVerifier extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(OAuthVerifier.class);

	public static String CLIENT_ID = "398944112517-0cstq1vfn609u4tv5vpvqde083i9s8i3.apps.googleusercontent.com";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OAuthVerifier() {
		super();
		// TODO Auto-generated constructor stub
		try {
			FileInputStream serviceAccount;
			// xyj
			String keyFile = System.getProperty("user.home") + "\\git\\code2017\\OAuth2v1\\src\\main\\resources\\fir-52b84-firebase-adminsdk-bwv5g-b8976c35c1.json";
			logger.info("initialization via key file : " + keyFile);
			serviceAccount = new FileInputStream(keyFile);

			// xyj
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					.setDatabaseUrl("https://fir-52b84.firebaseio.com").build();

			FirebaseApp.initializeApp(options);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doGet();, system.out");
		response.getWriter().append("hello, Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doPost()");
		// System.out.println("from : " + request.getParameter("from"));
		// System.out.println("token: " + request.getParameter("token"));

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		System.out.println("body : " + sb.toString());
		JSONObject inJson = new JSONObject(sb.toString());
		String from = inJson.getString("from");
		String token = inJson.getString("token");
		System.out.println("inJson.from : " + inJson.getString("from"));
		System.out.println("inJson.token: " + inJson.getString("token"));

		JSONObject outJson = null;
		if (from.equals("google"))
			outJson = googleVerify(token);
		else if (from.equals("firebase"))
			outJson = firebaseVerify(token);
		else {
			System.out.println("Invalid 'from'.");
			outJson = new JSONObject();
			outJson.put("status", "failed");
			outJson.put("msg", "invalid 'from'");
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(outJson.toString());
		out.flush();

		// TODO Auto-generated method stub
		// doGet(request, response);
	}

	public JSONObject googleVerify(String token) {
		JSONObject outJson = new JSONObject();
		try {
			// refer :
			// https://developers.google.com/identity/sign-in/web/backend-auth
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
					new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();
			System.out.println("public cert url : " + verifier.getPublicKeysManager().getPublicCertsEncodedUrl());

			GoogleIdToken idToken = verifier.verify(token);
			if (idToken != null) {
				System.out.println("valid ID token.");
				Payload payload = idToken.getPayload();

				// Print user identifier
				// String userId = payload.getSubject();
				// System.out.println("User ID: " + userId);
				// outJson.put("userId", userId);

				// Get profile information from payload
				String email = payload.getEmail();
				System.out.println("email: " + email);
				outJson.put("email", email);

				outJson.put("status", "success");
			} else {
				System.out.println("Invalid ID token.");
				outJson.put("status", "failed");
			}
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("verfication exception");
			outJson.put("status", "failed");
			outJson.put("msg", "verification exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("verfication exception");
			outJson.put("status", "failed");
			outJson.put("msg", "verification IO exception");
		}

		return outJson;
	}

	public JSONObject firebaseVerify(String token) {
		System.out.println("firebaseVerify(); " + token);
		JSONObject outJson = new JSONObject();
		try {
			final Object o = new Object();
			System.out.println("firebaseVerify(), start verifyIdToken");
			Task<FirebaseToken> task = FirebaseAuth.getInstance().verifyIdToken(token);
			task.addOnCompleteListener(new OnCompleteListener<FirebaseToken>() {
				@Override
				public void onComplete(Task<FirebaseToken> arg0) {
					// TODO Auto-generated method stub
					synchronized (o) {
						o.notifyAll();
					}
				}
			});
			synchronized (o) {
				int i = 0;
				while (!task.isComplete()) {
					if (++i > 10) {
						logger.info("timeout, 10 sec");
						break;
					}
					o.wait(1000);
				}
			}
			FirebaseToken rsl = task.getResult();
			System.out.println("done, get toke : " + rsl.toString());
			System.out.println("done, get uid : " + rsl.getUid());
			outJson.put("status", "success");
			outJson.put("uid", rsl.getUid());
			outJson.put("email", rsl.getEmail());
			outJson.put("issuer", rsl.getIssuer());
			outJson.put("name", rsl.getName());

		} catch (Exception e) {
			System.out.println("verfication exception");
			e.printStackTrace();
			outJson.put("status", "failed");
			outJson.put("msg", "throw " + e.getClass().getName());
		}

		return outJson;
	}
}
