package com.honvay.hdms.login.model;

import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/3/13
 **/
@Data
public class LoginLogDto {

	private Integer id;

	private Date loginDate;

	private Integer userId;

	private String userAgent;

	private String client;

	private String loginIp;

	private String position;

	private String username;

	private String name;
}
