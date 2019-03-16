package com.honvay.hdms.auth.jwt.configuration;

import com.honvay.hdms.auth.jwt.filter.JwtAuthenticationFilter;
import com.honvay.hdms.auth.jwt.store.JwtProperties;
import com.honvay.hdms.auth.web.handler.*;
import com.honvay.hdms.auth.jwt.entry.JwtAuthenticationEntryPoint;
import com.honvay.hdms.auth.jwt.handler.JwtAuthenticationFailureHandler;
import com.honvay.hdms.auth.jwt.handler.JwtAuthenticationSuccessHandler;
import com.honvay.hdms.auth.jwt.store.DefaultJwtTokenStore;
import com.honvay.hdms.auth.jwt.store.JwtTokenProvider;
import com.honvay.hdms.auth.jwt.store.JwtTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LIQIU
 */
@EnableConfigurationProperties(JwtProperties.class)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Bean
	public JwtTokenStore jwtTokenStore() {
		return new DefaultJwtTokenStore();
	}

	@Bean
	public JwtAuthenticationFailureHandler failureHandler() {
		return new JwtAuthenticationFailureHandler();
	}

	@Bean
	public JwtAuthenticationSuccessHandler successHandler() {
		return new JwtAuthenticationSuccessHandler(jwtTokenProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("111111"));
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(10L);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
				.antMatchers("/login", "/logout", "/error").permitAll()
				.and()
				.formLogin()
				.loginProcessingUrl("/login")
				.failureHandler(this.failureHandler())
				.successHandler(this.successHandler())
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler(new JwtLogoutSuccessHandler())
				.and()
				.exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterAfter(this.jwtAuthenticationFilter, SecurityContextPersistenceFilter.class);
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
