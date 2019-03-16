package com.honvay.hdms.dms.model.request;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@Data
public class UpdateTagRequest {

	@NotNull
	private Integer id;

	@NotEmpty
	private String tags;

	private AuthenticatedUser user;

}
