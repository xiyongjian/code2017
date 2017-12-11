package gosigma.etl_log.security;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import gosigma.etl_log.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
	public static Logger log = LoggerFactory.getLogger(JwtLoginFilter.class);

	public JwtLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("Entering...");
		log.info("Request dump : " + Utils.dump(request));
		//		AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(),
		//				AccountCredentials.class);

		// get user/pass from request and authenticate it
		log.info("authenticationManager as : " + this.getAuthenticationManager().toString());
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				"admin", "password");
		return this.getAuthenticationManager().authenticate(authRequest);
		// return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
		log.info("Entering...");

		//	String token = Jwts.builder()
		//			.setSubject(((User) authResult.getPrincipal()).getUsername())
		//			.setExpiration(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME))
		//			.signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET)
		//			.compact();
		//	response.addHeader(JwtConstants.HEADER_STRING, JwtConstants.TOKEN_PREFIX + token);
		//	// super.successfulAuthentication(request, response, chain, authResult);
		TokenAuthenticationService.addAuthentication(response, authResult.getName());
		response.getWriter().println("{\"authentication\" : \"successful\"}");
	}

}
