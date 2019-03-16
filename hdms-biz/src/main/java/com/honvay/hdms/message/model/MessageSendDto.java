package com.honvay.hdms.message.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Data
public class MessageSendDto {
	@NotEmpty
	private List<Integer> files;
	@NotEmpty
	private List<Integer> receivers;
	@NotEmpty
	private String content;

}
