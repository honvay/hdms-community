package com.honvay.hdms.dms.document.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
@MapperScan("com.honvay.**.mapper")
@ComponentScan("com.honvay")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
