package com.honvay.hdms.user.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/12
 **/
@Data
public class UpdateProfileDto {

	private Integer id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	@NotEmpty
	private String mode;

	@NotEmpty
	private String phoneNumber;

	@NotEmpty
	private String sortField;

	@NotNull
	private Boolean sortDesc;

}
