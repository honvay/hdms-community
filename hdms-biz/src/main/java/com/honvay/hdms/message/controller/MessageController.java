/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.message.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.message.entity.Message;
import com.honvay.hdms.message.model.MessageDto;
import com.honvay.hdms.message.model.MessageReadDto;
import com.honvay.hdms.message.model.MessageSendDto;
import com.honvay.hdms.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

	@Autowired
	private MessageService messageService;

	@RequestMapping("/send")
	public Result<String> sendFile(@RequestBody @Valid MessageSendDto messageSendDto,
								   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		for (Integer file : messageSendDto.getFiles()) {
			for (Integer receiver : messageSendDto.getReceivers()) {
				Message message = new Message();
				message.setDocumentId(file);
				message.setReceiver(receiver);
				message.setContent(messageSendDto.getContent());
				message.setSendDate(new Date());
				message.setSender(authenticatedUser.getId());
				this.messageService.add(message);
			}
		}
		return this.success();
	}

	@RequestMapping("/read")
	public Result<String> read(@RequestBody @Valid MessageReadDto messageReadDto) {
		for (Integer messageId : messageReadDto.getMessageIds()) {
			this.messageService.read(messageId);
		}
		return this.success();
	}

	@RequestMapping("/list")
	public Result<List<MessageDto>> list(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		return this.success(this.messageService.findUnread(authenticatedUser.getId()));
	}
}
