package com.honvay.hdms.dms.recent.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_recent")
public class Recent {

	private Integer id;

	private Integer documentId;
	
	private Integer userId;

	private Date collectDate;

}