package com.honvay.hdms.dms.model;

import lombok.Data;

import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/3/8
 **/
@Data
public class AuthorizedPermission {

	private Integer id;

	private Integer documentId;

	private String path;

	private Integer owner;

	private Integer ownerType;

	private Set<String> permissions;
}
