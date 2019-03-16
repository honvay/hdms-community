package com.honvay.hdms.user.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/16
 **/
@Data
public class UpdateQuotaDto {

	@NotNull
	private Integer id;

	@NotNull
	private Integer quota;

}
