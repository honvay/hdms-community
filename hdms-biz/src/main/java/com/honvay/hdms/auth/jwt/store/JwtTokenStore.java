package com.honvay.hdms.auth.jwt.store;

import org.springframework.security.core.Authentication;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public interface JwtTokenStore {

	void save(String token, Authentication authentication);

	Authentication get(String token);

	void remove(String token);

}
