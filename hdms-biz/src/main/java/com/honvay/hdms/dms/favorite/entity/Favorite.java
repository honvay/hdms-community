package com.honvay.hdms.dms.favorite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("hdms_favorite")
public class Favorite  {

	@TableId
	private Integer id;

	@NotNull
	private Integer documentId;
	
	private Integer userId;
	

	
}
