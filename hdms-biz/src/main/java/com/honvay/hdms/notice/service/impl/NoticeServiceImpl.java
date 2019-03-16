/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.notice.service.impl;

import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.notice.entity.Notice;
import com.honvay.hdms.notice.mapper.NoticeMapper;
import com.honvay.hdms.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LIQIU
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice, Integer> implements NoticeService {

	private final static String NOTICE_CACHE_NAME = "noticeCache";

	private final static String NOTICE_CACHE_KEY = "targetClass";

	@Autowired
	private NoticeMapper noticeMapper;

	@Override
	@CachePut(cacheNames = NOTICE_CACHE_NAME, key = NOTICE_CACHE_KEY)
	public Notice save(Notice t) {
		return super.save(t);
	}

	@Override
	@Cacheable(cacheNames = NOTICE_CACHE_NAME, key = NOTICE_CACHE_KEY)
	public Notice get() {
		return noticeMapper.selectOne(null);
	}

	@Override
	@CachePut(cacheNames = NOTICE_CACHE_NAME, key = NOTICE_CACHE_KEY)
	public Notice update(Notice t) {
		return super.update(t);
	}

	@Override
	public Notice getAvailable() {
		Notice notice = this.get();
		if (notice != null && notice.isEnable()) {
			Date date = new Date();
			if (notice.getStartDate() != null && notice.getEndDate() != null) {
				if (notice.getStartDate().before(date) && notice.getEndDate().after(date)) {
					return notice;
				}
			} else if (notice.getStartDate() != null && notice.getEndDate() == null && notice.getStartDate().before(date)) {
				return notice;
			} else if (notice.getStartDate() == null && notice.getEndDate() != null && notice.getEndDate().after(date)) {
				return notice;
			}
		}
		return null;
	}

}
