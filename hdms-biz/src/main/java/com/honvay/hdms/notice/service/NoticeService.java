package com.honvay.hdms.notice.service;


import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.notice.entity.Notice;

/**
 * @author LIQIU 
 */
public interface NoticeService extends BaseService<Notice, Integer> {

	/**
	 * @return
	 */
	Notice getAvailable();

	/**
	 * @return
	 */
	Notice get();
}
