package com.honvay.hdms.message.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_message")
public class Message  {

	@TableId
	private Integer id;

	private Integer receiver;
	
	private Integer documentId;
	
	private Integer sender;
	
	private String content;
	
	private Date sendDate;
	
	private boolean readed;
	
	private Date readDate;
}
