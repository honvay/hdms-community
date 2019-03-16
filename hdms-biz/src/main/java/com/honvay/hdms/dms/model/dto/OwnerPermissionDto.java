package com.honvay.hdms.dms.model.dto;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/2/27
 **/
@Data
public class OwnerPermissionDto {

	private Integer id;

	private Integer documentId;

	private String path;

	private Integer owner;

	private Integer ownerType;

	private String permissions;
}
