package gosigma.etl_log.security;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.debug.DebugFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// must have this annotation for security config
@Configuration
@EnableWebSecurity(debug = true) // list all filters in context
public class SecConfig extends WebSecurityConfigurerAdapter {
	public static Logger log = LoggerFactory.getLogger(SecConfig.class);

	// setup the default user/password/roles for simple setting
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("Entering...");
		// Create a default account
		auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
	}

	// // -------------------------- default login out of box
	// // -----------------------------
	// // redirect to login page /login
	// // to logout, goto /logout
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// log.info("Entering...");
	// http.authorizeRequests().antMatchers("/test/**").permitAll(); // allow all at
	// the front of filters?
	// // http.authorizeRequests().anyRequest().authenticated();
	// super.configure(http);
	//
	// // for /h2-console
	// http.headers().frameOptions().disable();
	// http.csrf().disable();
	//
	// // for logout, not neccessary
	// // http.logout().logoutRequestMatcher(new
	// // AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login").permitAll();
	// }

	// -------------------------- customized login -----------------------------
	// // must setup /login controller and view
	// // redirect to login page /login
	// // to logout, goto /logout
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("Entering...");
		http.httpBasic();

		// for /h2-console
		http.headers().frameOptions().disable();
		http.csrf().disable();

		http.authorizeRequests().antMatchers("/login/**", "/test/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();

		// for customized login page works, may set /login controller and view
		// (login.html with thymeleaf)
		// http.loginPage("/login").permitAll()
		// login/logout setup
		http.formLogin().failureUrl("/login?error").defaultSuccessUrl("/").permitAll();
		//				.and().logout()
		//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		//				// .clearAuthentication(true).invalidateHttpSession(true)	// clean up login info
		//				.logoutSuccessUrl("/login").permitAll();

		//		// uncomment them to enable JWT auth
		//		log.info("setup JwtLoginFilter's authenticationManager as : "
		//				+ authenticationManager().toString());
		//		http.addFilterBefore(new JwtLoginFilter("/api/authentication", authenticationManager()),
		//				UsernamePasswordAuthenticationFilter.class);
		//		http.addFilterBefore(new JwtAuthenticationFilter(),
		//				UsernamePasswordAuthenticationFilter.class);

		{
			log.info("testing, authenticationManager as : "
					+ this.authenticationManager().toString());
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					"admin", "password");
			Authentication auth = this.authenticationManager().authenticate(authRequest);
			log.info("admin/password result : " + auth.toString());
		}
	}

	// @Autowired
	// private FilterChainProxy filterChainProxy;

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void listSecurityFilters() {
		log.info("Entering...");
		// FilterChainProxy filterChainProxy =
		// (FilterChainProxy)applicationContext.getBean("org.springframework.security.filterChainProxy");
		Object o = applicationContext.getBean("springSecurityFilterChain");
		log.info("springSecurityFilterChain : " + o.getClass().getName() + "@" + o.hashCode());
		// DebugFilter df = (DebugFilter)o;

		FilterChainProxy filterChainProxy = null;
		for (Field field : o.getClass().getDeclaredFields()) {
			if (field.getName().equals("fcp")) {
				field.setAccessible(true); // You might want to set modifier to public first.
				try {
					filterChainProxy = (FilterChainProxy) field.get(o);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.info("cant' get proxy", e);
				}
			}
		}

		if (filterChainProxy != null) {
			List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
			for (SecurityFilterChain c : filterChains) {
				log.info("chain : " + c.getClass().getName() + "@" + c.hashCode());
				for (Filter f : c.getFilters()) {
					log.info(" filter : " + f.getClass().getName() + "@" + f.hashCode());
				}
			}
		}
	}

	// @Autowired
	// private AbstractFilterRegistrationBean regBean;
}
