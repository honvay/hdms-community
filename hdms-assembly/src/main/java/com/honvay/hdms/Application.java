package com.honvay.hdms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
@EnableCaching
@MapperScan("com.honvay.**.mapper")
@ComponentScan("com.honvay")
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application  {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
