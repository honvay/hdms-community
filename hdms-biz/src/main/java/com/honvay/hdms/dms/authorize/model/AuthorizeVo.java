package com.honvay.hdms.dms.authorize.model;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/3/5
 **/
@Data
public class AuthorizeVo {

	private Integer id;

	private Integer permissionId;

	private Integer owner;

	private Integer ownerType;

	private String name;

	private String avatar;

	private Boolean inherited;

	private Integer documentId;

	private String permissionName;

	private String path;

}
