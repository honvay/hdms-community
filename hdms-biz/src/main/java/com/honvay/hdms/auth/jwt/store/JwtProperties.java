package com.honvay.hdms.auth.jwt.store;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author LIQIU
 * created on 2018/12/26
 **/
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private String signingKey = "hdms";

	private Duration expiration = Duration.ofHours(2);

	private Boolean autoRefresh = true;

	private Duration refreshPoint = Duration.ofMinutes(30);

	private String refreshTokenHeader = "refreshToken";
}
