package com.honvay.hdms.login.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_login_log")
public class LoginLog {

	@TableId
	private Integer id;

	private Date loginDate;

	private Integer userId;

	private String userAgent;

	private String client;

	private String loginIp;

	private String position;
}
