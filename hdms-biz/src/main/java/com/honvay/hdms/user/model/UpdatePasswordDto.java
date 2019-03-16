package com.honvay.hdms.user.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author LIQIU
 * created on 2019/3/15
 **/
@Data
public class UpdatePasswordDto {

	private Integer id;

	@NotEmpty
	private String oldPassword;

	@NotEmpty
	private String newPassword;

	@NotEmpty
	private String confirmPassword;

}
