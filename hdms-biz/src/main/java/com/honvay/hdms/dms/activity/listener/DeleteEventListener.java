package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.event.DeleteEvent;
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
public class DeleteEventListener implements ApplicationListener<DeleteEvent> {

	@Autowired
	private ActivityService activityService;

	@Override
	public void onApplicationEvent(DeleteEvent event) {

		Document document = event.getDocument();

		if (log.isDebugEnabled()) {
			log.debug("Delete file:{}", document.getId());
		}

		Activity activity = Activity.builder()
				.documentId(document.getId())
				.documentName(document.getName())
				.documentType(document.getType())
				.path(document.getPath())
				.scope(ActivityScope.BOTH)
				.operator(event.getUserId())
				.operation(OperationType.DELETE)
				.build();
		activityService.save(activity);


	}
}
