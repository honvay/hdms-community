package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.event.CreateEvent;
import com.honvay.hdms.dms.event.UploadEvent;
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
