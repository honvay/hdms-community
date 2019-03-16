package com.honvay.hdms.user.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/12
 **/
@Data
public class UserUpdateVo {

	@NotNull
	private Integer id;

	@NotEmpty
	private String username;

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	private String phoneNumber;

	@NotNull
	private Integer departmentId;

	private String departmentName;
}
