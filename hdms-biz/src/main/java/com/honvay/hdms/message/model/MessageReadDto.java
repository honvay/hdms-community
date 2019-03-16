package com.honvay.hdms.message.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Data
public class MessageReadDto {

	@NotEmpty
	private Integer[] messageIds;
}
