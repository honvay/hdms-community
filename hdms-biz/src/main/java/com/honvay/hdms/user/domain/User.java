package com.honvay.hdms.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_user")
public class User implements Serializable {

	public static final String ROLE_USER = "USER";
	public static final String ROLE_SYS_ADMIN = "SYS_ADMIN";
	public static final String ROLE_DOC_ADMIN = "DOC_ADMIN";

	public static final String STATUS_DISABLED = "1";
	public static final String STATUS_ACTIVE = "2";
	public static final String STATUS_LOCKED = "3";

	public static final String MODE_LIST = "list";
	public static final String MODE_BLOCK = "block";


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	private String name;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private Integer status;

	private Date lastLoginDate;

	private String lastLoginIp;

	private String description;

	private String avatar;

	private Integer quota;

	private Integer departmentId;

	private String role;

	private String phoneNumber;

	private String fullPinyin;

	private String shortPinyin;

	private Boolean changePassword;

	private Integer loginFailCount;

	private String mode;

	private String sortField;

	private Boolean sortDesc;

	private Integer mountId;

}
