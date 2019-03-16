/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
