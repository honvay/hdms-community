package com.honvay.hdms.dms.authorize.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/5
 **/
@Data
public class AuthorizeDto {
	@NotNull
	private Integer documentId;

	private List<Integer> userIds;

	private List<Integer> departmentIds;

	@NotNull
	private Integer permission;
}
