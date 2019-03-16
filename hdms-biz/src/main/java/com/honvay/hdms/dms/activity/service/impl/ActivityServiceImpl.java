package com.honvay.hdms.dms.activity.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.mapper.ActivityMapper;
import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.model.dto.DocumentActivityDto;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author LIQIU
 */
@Service
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Integer> implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Override
	public List<Activity> findDirectoryActivityByPath(String path) {
		return activityMapper.findDirectoryActivityByPath(path + "%");
	}

	@Override
	public List<Activity> findDocumentActivityById(Integer documentId) {
		return activityMapper.findDocumentActivityById(documentId);
	}

	@Override
	public Activity save(Activity activity) {
		activity.setOperateDate(new Date());
		activity.setOperateDate(new Date());
		super.save(activity);
		return activity;
	}

	@Override
	public Page<DocumentActivityDto> search(ActivityQuery query) {
		query.setStart((query.getPage() - 1) * query.getSize());
		query.setEnd(query.getPage() * query.getSize());
		if (StringUtils.isNotEmpty(query.getDocumentName() )) {
			query.setDocumentName("%" + query.getDocumentName() + "%");
		}
		Page<DocumentActivityDto> page = new Page<>();
		page.setRecords(this.activityMapper.searchActivity(query));
		page.setTotal(this.activityMapper.countActivity(query));
		return page;
	}
}
