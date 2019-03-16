package com.honvay.hdms.dms.token;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author LIQIU
 * created on 2019/3/15
 **/
@Component
public class AccessTokenStore {

	private Cache<String, String> cache;

	public AccessTokenStore() {
		// 通过CacheBuilder构建一个缓存实例
		cache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				.concurrencyLevel(10)
				.build();
	}

	public String put(String code) {
		String token = UUID.randomUUID().toString();
		cache.put(token, code);
		return token;
	}

	public String get(String token) {
		String code = cache.getIfPresent(token);
		this.cache.invalidate(token);
		return code;
	}
}
