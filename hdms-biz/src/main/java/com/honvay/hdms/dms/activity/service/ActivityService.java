package com.honvay.hdms.dms.activity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.model.dto.DocumentActivityDto;
import com.honvay.hdms.framework.support.service.BaseService;

import java.util.List;

/**
 * @author LIQIU
 */
public interface ActivityService extends BaseService<Activity, Integer> {
	List<Activity> findDirectoryActivityByPath(String path);

	List<Activity> findDocumentActivityById(Integer documentId);

	Page<DocumentActivityDto> search(ActivityQuery query);

	/*List<Result> findByDocumentId(Date start, Date end, String operator, String fileName, String operation);

	PageResult page(Date start, Date end, String operator, String fileName, String operation);*/

}
