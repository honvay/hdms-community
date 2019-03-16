/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.jwt.filter;

import com.honvay.hdms.auth.jwt.store.JwtProperties;
import com.honvay.hdms.auth.jwt.store.JwtTokenProvider;
import com.honvay.hdms.auth.jwt.store.JwtTokenStore;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenStore jwtTokenStore;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	JwtProperties properties;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && jwtTokenProvider.validate(jwt)) {

				Claims claims = jwtTokenProvider.parse(jwt);

				Authentication authentication = jwtTokenStore.get(claims.getSubject());

				if (System.currentTimeMillis() - claims.getExpiration().getTime() < properties.getExpiration().toMillis()) {
					response.addHeader(properties.getRefreshTokenHeader(), jwtTokenProvider.generate(authentication));
				}

				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public JwtTokenStore getJwtTokenStore() {
		return jwtTokenStore;
	}

	public void setJwtTokenStore(JwtTokenStore jwtTokenStore) {
		this.jwtTokenStore = jwtTokenStore;
	}
}
