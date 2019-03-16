package com.honvay.hdms.dms.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateRequest extends MountBaseRequest {

	@NotEmpty
	private String name;

	private String contentType;

	@NotNull
	private Long size;

	private Integer parent;

	@NotEmpty
	private String md5;

	private String ext;

	private Integer master;

	private String code;

}
