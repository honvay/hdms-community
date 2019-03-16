package com.honvay.hdms.dms.authorize.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_authorize")
public class Authorize {

	@TableId
	private Integer id;

	/**
	 * 授权主体
	 */
	public Integer documentId;

	public Integer permissionId;

	public Integer ownerType;

	/**
	 * 授权对象
	 */
	public Integer owner;

	public Integer authorizer;

}
