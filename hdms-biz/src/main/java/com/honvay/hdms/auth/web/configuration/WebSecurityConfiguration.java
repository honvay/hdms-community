package com.honvay.hdms.auth.web.configuration;

import com.honvay.hdms.auth.web.captcha.filter.CaptchaAuthenticationFilter;
import com.honvay.hdms.auth.web.handler.WebAuthenticationFailureHandler;
import com.honvay.hdms.auth.web.handler.WebAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author LIQIU
 */
@EnableWebSecurity
@Configuration
@Order(99)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private SessionRegistry sessionRegistry;

	@Autowired
	private SessionAuthenticationStrategy sessionAuthenticationStrategy;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationFailureHandler failureHandler;

	private CaptchaAuthenticationFilter captchaAuthenticationFilter = new CaptchaAuthenticationFilter();

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new WebAuthenticationSuccessHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/asset/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		captchaAuthenticationFilter.addRequestMatcher(new AntPathRequestMatcher("/login", HttpMethod.POST.name()), this.failureHandler);

		http.setSharedObject(CaptchaAuthenticationFilter.class, captchaAuthenticationFilter);

		http.authorizeRequests()
				.antMatchers("/login", "/logout", "/error").permitAll()
				.antMatchers("/captcha", "/session-invalid").permitAll()
				.and()
				.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureHandler(this.failureHandler)
				.successHandler(this.successHandler())
				//.failureHandler(new WebAuthenticationFailureHandler())
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.invalidateHttpSession(false)
				.and()
				.addFilterBefore(captchaAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
				.sessionManagement()
				.invalidSessionUrl("/session-invalid")
				.maximumSessions(1)
				.expiredUrl("/session-invalid")
				.sessionRegistry(sessionRegistry)
				.and()
				.sessionFixation()
				.migrateSession()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.sessionAuthenticationStrategy(sessionAuthenticationStrategy);
		http.authorizeRequests().antMatchers("/**").authenticated();
		http.getSharedObject(AuthenticationManagerBuilder.class).authenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher))
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

}
