package com.honvay.hdms.auth.jwt.store;

import org.springframework.security.core.Authentication;

import java.util.HashMap;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
public class DefaultJwtTokenStore implements JwtTokenStore {

	private HashMap<String, Authentication> repository = new HashMap<>();

	@Override
	public void save(String token, Authentication authentication) {
		repository.put(token, authentication);
	}

	@Override
	public Authentication get(String token) {
		return repository.get(token);
	}

	@Override
	public void remove(String token) {
		repository.remove(token);
	}
}
