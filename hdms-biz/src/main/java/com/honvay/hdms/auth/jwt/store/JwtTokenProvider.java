/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.jwt.store;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Date;

/**
 * @author LIQIU
 */
@Slf4j
public class JwtTokenProvider {

	@Autowired
	private JwtProperties properties;

	public String generate(Authentication authentication) {

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + properties.getExpiration().toMillis());

		return Jwts.builder()
				.setSubject(authentication.getName())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, properties.getSigningKey())
				.compact();
	}

	public Claims parse(String token) {
		return Jwts.parser()
				.setSigningKey(properties.getSigningKey())
				.parseClaimsJws(token)
				.getBody();
	}

	public String getUsernameFromToken(String token) {
		return parse(token).getSubject();
	}

	public boolean validate(String authToken) {
		try {
			Jwts.parser().setSigningKey(properties.getSigningKey()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}
}