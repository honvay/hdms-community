package com.honvay.hdms.dms.activity.model;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Data
public class AuthorizeActivityDto {

	private Integer owner;

	private Integer ownerType;

	private String ownerName;

	private String ownerAvatar;

	private String currentPermission;

	private String originalPermission;

}
