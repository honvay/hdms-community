package com.honvay.hdms.dms.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("hdms_permission")
public class Permission {

	private Integer id;

	private String name;

	private String description;

	private String permissions;
}
