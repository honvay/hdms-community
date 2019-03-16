/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
