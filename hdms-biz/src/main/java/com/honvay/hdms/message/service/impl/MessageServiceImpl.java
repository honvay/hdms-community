/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.message.service.impl;

import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.message.entity.Message;
import com.honvay.hdms.message.mapper.MessageMapper;
import com.honvay.hdms.message.model.MessageDto;
import com.honvay.hdms.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author LIQIU
 */
@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, Integer> implements MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public void add(Message message) {
		message.setReaded(false);
		this.save(message);
	}

	@Override
	public List<MessageDto> findUnread(Integer receiver) {
		return messageMapper.findUnreadByReceiver(receiver);
	}


	@Override
	public void read(Integer id) {
		Message message = this.get(id);
		message.setReaded(true);
		message.setReadDate(new Date());
		this.update(message);
	}
}
