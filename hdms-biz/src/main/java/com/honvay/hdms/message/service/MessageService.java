package com.honvay.hdms.message.service;


import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.message.entity.Message;
import com.honvay.hdms.message.model.MessageDto;

import java.util.List;

/**
 * @author LIQIU
 */
public interface MessageService extends BaseService<Message, Integer> {

	/**
	 * @param message
	 */
	void add(Message message);

	/**
	 * @param receiver
	 * @return
	 */
	List<MessageDto> findUnread(Integer receiver);

	void read(Integer id);
}
