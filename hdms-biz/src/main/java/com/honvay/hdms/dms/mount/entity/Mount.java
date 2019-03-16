package com.honvay.hdms.dms.mount.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
@Data
@TableName("hdms_mount")
public class Mount {

	@TableId
	private Integer id;

	private Integer owner;

	private Integer type;

	private String name;

	private String alias;
}
