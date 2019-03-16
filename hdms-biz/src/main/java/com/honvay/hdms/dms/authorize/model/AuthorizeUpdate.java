package com.honvay.hdms.dms.authorize.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/6
 **/
@Data
public class AuthorizeUpdate {

	@NotNull
	private Integer id;

	@NotNull
	private Integer permissionId;

}
