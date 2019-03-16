/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.event.CopyEvent;
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
public class CopyEventListener implements ApplicationListener<CopyEvent> {

	@Autowired
	private ActivityService activityService;

	@Override
	public void onApplicationEvent(CopyEvent event) {

		Document document = event.getDocument();

		if (log.isDebugEnabled()) {
			log.debug("Copy file:{}, from:{} to:{}", document.getId(), event.getOriginalParent(), document.getParent());
		}

		if (DocumentType.isFile(document.getType())) {
			Activity activity = Activity.builder()
					.documentId(document.getId())
					.documentType(document.getType())
					.path(document.getPath())
					.outcomeMount(event.getOriginalMount())
					.incomeMount(document.getMount())
					.outcomeMount(event.getOriginalMount())
					.operator(event.getUserId())
					.scope(ActivityScope.FILE)
					.operation(OperationType.COPY)
					.build();
			if (event.getOriginalParent() != null) {
				activity.setOutcome(event.getOriginalParent().getId());
				activity.setOutcomeName(event.getOriginalParent().getName());
			}
			if (event.getCurrentParent() != null) {
				activity.setIncome(event.getCurrentParent().getId());
				activity.setIncomeName(event.getCurrentParent().getName());
			}
			activityService.save(activity);
		}
		if (event.getCurrentParent() != null) {
			Activity activity = Activity.builder()
					.directoryId(document.getParent())
					.path(event.getCurrentParent().getPath())
					.documentId(document.getId())
					.documentName(document.getName())
					.documentType(document.getType())
					.outcomeMount(event.getOriginalMount())
					.operator(event.getUserId())
					.scope(ActivityScope.DIRECTORY)
					.operation(OperationType.COPY_IN)
					.build();
			if (event.getOriginalParent() != null) {
				activity.setOutcome(event.getOriginalParent().getId());
				activity.setOutcomeName(event.getOriginalParent().getName());
			}
			activityService.save(activity);
		}

		if (event.getOriginalParent() != null) {
			Activity activity = Activity.builder()
					.directoryId(event.getOriginalParent().getId())
					.path(event.getOriginalParent().getPath())
					.documentId(document.getId())
					.documentName(document.getName())
					.documentType(document.getType())
					.incomeMount(document.getMount())
					.scope(ActivityScope.DIRECTORY)
					.operation(OperationType.COPY_OUT)
					.build();
			if (event.getCurrentParent() != null) {
				activity.setIncome(event.getCurrentParent().getId());
				activity.setIncomeName(event.getCurrentParent().getName());
			}
			activityService.save(activity);
		}
	}
}
