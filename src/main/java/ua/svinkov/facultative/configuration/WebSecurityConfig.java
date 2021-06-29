package ua.svinkov.facultative.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import ua.svinkov.facultative.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserServiceImpl userService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();

		httpSecurity.authorizeRequests().antMatchers("/", "/login", "/reg", "/logout", "/profile").permitAll();

		httpSecurity.authorizeRequests().antMatchers("/student", "/allcourses").access("hasRole('ROLE_STUDENT')");

		httpSecurity.authorizeRequests().antMatchers("/teacher", "/coursestudent", "/coursestudent/**")
				.access("hasRole('ROLE_TEACHER')");

		httpSecurity.authorizeRequests().antMatchers("/admin", "adminbasis", "/addCourse", "/editCourse")
				.access("hasRole('ROLE_ADMIN')");

		httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

		httpSecurity.authorizeRequests().and().formLogin().loginProcessingUrl("/login-processing").loginPage("/login")
				.defaultSuccessUrl("/welcome").failureUrl("/login/error").usernameParameter("login")
				.passwordParameter("password").and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");

		httpSecurity.sessionManagement().sessionAuthenticationStrategy(concurrentSession()).maximumSessions(-1)
				.expiredSessionStrategy(sessionInformationExpiredStrategy());
		// httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	public CompositeSessionAuthenticationStrategy concurrentSession() {

		ConcurrentSessionControlAuthenticationStrategy concurrentAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(
				sessionRegistry());
		List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<SessionAuthenticationStrategy>();
		delegateStrategies.add(concurrentAuthenticationStrategy);
		delegateStrategies.add(new SessionFixationProtectionStrategy());
		delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));

		return new CompositeSessionAuthenticationStrategy(delegateStrategies);
	}

	@Bean
	SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new CustomSessionInformationExpiredStrategy("/login");
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

		private String expiredUrl = "";

		public CustomSessionInformationExpiredStrategy(String expiredUrl) {
			this.expiredUrl = expiredUrl;
		}

		@Override
		public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent)
				throws IOException, ServletException {

			HttpServletRequest request = sessionInformationExpiredEvent.getRequest();
			HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
			request.getSession();// creates a new session
			response.sendRedirect(request.getContextPath() + expiredUrl);
		}

	}
}