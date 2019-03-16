/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.event.CreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
@Slf4j
@Component
public class CreateEventListener implements ApplicationListener<CreateEvent> {

	@Autowired
	private ActivityService activityService;

	@Override
	public void onApplicationEvent(CreateEvent event) {

		Document document = event.getDocument();

		if (log.isDebugEnabled()) {
			log.debug("Create directory:{} on {}", document.getId(), document.getParent());
		}

		Activity directoryActivity = Activity.builder()
				.documentName(document.getName())
				.documentId(document.getId())
				.documentType(document.getType())
				.path(document.getPath())
				.documentName(document.getName())
				.scope(ActivityScope.BOTH)
				.operator(event.getUserId())
				.operation(OperationType.CREATE)
				.build();
		activityService.save(directoryActivity);

	}
}
