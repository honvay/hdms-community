package com.honvay.hdms.dms.recent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.event.DeleteEvent;
import com.honvay.hdms.dms.recent.entity.Recent;
import com.honvay.hdms.dms.recent.mapper.RecentMapper;
import com.honvay.hdms.dms.recent.service.RecentService;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class RecentServiceImpl extends BaseServiceImpl<Recent, Integer> implements RecentService, ApplicationListener<DeleteEvent> {

	@Autowired
	private RecentMapper recentMapper;

	@Override
	public void add(Integer hcoId, Integer userId) {
		LambdaQueryWrapper<Recent> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Recent::getDocumentId, hcoId)
				.eq(Recent::getUserId, userId);
		Recent recent;
		recent = this.recentMapper.selectOne(wrapper);
		if (recent != null) {
			recent.setCollectDate(new Date());
			this.update(recent);
		} else {
			recent = new Recent();
			recent.setDocumentId(hcoId);
			recent.setUserId(userId);
			recent.setCollectDate(new Date());
			this.save(recent);
		}
	}

	@Override
	public void remove(List<Integer> documentIds, Integer userId) {
		Assert.isTrue(!documentIds.isEmpty(), "参数错误");
		Assert.isTrue(userId != null, "参数错误");
		LambdaUpdateWrapper<Recent> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(Recent::getUserId, userId)
				.in(Recent::getDocumentId, documentIds);
		this.recentMapper.delete(wrapper);
	}

	@Override
	public void clear(Integer userId) {
		Assert.notNull(userId, "参数错误");
		LambdaUpdateWrapper<Recent> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(Recent::getUserId, userId);
		this.recentMapper.delete(wrapper);
	}

	@Override
	public void onApplicationEvent(DeleteEvent deleteEvent) {
		Document document = (Document) deleteEvent.getSource();
		LambdaUpdateWrapper<Recent> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(Recent::getDocumentId, document.getId());
		this.recentMapper.delete(wrapper);
	}
}
