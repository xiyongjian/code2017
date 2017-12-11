package gosigma.etl_log.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecController {
	public static Logger log = LoggerFactory.getLogger(SecController.class);

	// // must set it up for customized user login page
	// @RequestMapping(value = "/login", method = RequestMethod.GET)
	// public String login(Model model) throws Exception {
	// log.info("Entering...");
	// return "login";
	//
	// }

	// programatically clean up security info
	// ref :
	// https://stackoverflow.com/questions/36557294/spring-security-logout-does-not-work-does-not-clear-security-context-and-authe
	//	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	//	public String logoutDo(HttpServletRequest request, HttpServletResponse response) {
	//		log.info("Entering...");
	//		HttpSession session = request.getSession(false);
	//		SecurityContextHolder.clearContext();
	//		session = request.getSession(false);
	//		if (session != null) {
	//			session.invalidate();
	//		}
	//		for (Cookie cookie : request.getCookies()) {
	//			cookie.setMaxAge(0);
	//		}
	//
	//		return "logout";
	//	}

	@RequestMapping(value = { "/sec/user" }, method = RequestMethod.GET)
	public void secUserDo(HttpServletRequest request, HttpServletResponse response) {
	}
}
