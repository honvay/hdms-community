package com.honvay.hdms.dms.model.request;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/7
 **/
@Data
public class UpdateDescRequest {

	@NotNull
	private Integer id;

	private String description;

	private AuthenticatedUser user;




}
