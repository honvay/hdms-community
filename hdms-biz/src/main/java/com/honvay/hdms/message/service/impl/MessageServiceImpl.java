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
