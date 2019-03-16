package com.honvay.hdms.dms.activity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.model.dto.DocumentActivityDto;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/activity")
public class ActivityController extends BaseController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping("/search")
	public Result<Page<DocumentActivityDto>> search(ActivityQuery query){
		return this.success(this.activityService.search(query));
	}


}
