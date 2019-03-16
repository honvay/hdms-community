package com.honvay.hdms.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.message.entity.Message;
import com.honvay.hdms.message.model.MessageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
public interface MessageMapper extends BaseMapper<Message> {

	/**
	 * @param receiver
	 * @return
	 */
	List<MessageDto> findUnreadByReceiver(@Param("receiver") Integer receiver);

}
