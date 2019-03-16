package com.honvay.hdms.message.model;

import com.honvay.hdms.dms.model.dto.DocumentOperatorDto;
import com.honvay.hdms.dms.model.dto.DocumentSimpleDto;
import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Data
public class MessageDto {

	private Integer id;

	private String content;

	private String sendDate;

	private DocumentOperatorDto sender;

	private DocumentSimpleDto document;
}
