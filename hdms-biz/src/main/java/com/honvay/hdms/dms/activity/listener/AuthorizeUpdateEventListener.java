package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.event.AuthorizeUpdateEvent;
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
public class AuthorizeUpdateEventListener implements ApplicationListener<AuthorizeUpdateEvent> {

	@Autowired
	private ActivityService activityService;

	@Override
	public void onApplicationEvent(AuthorizeUpdateEvent event) {

		Document document = event.getDocument();

		if (log.isDebugEnabled()) {
			log.debug("Rename documentId:{} ", document.getId());
		}

		Activity activity = Activity.builder()
				.documentId(document.getId())
				.documentName(document.getName())
				.documentType(document.getType())
				.path(document.getPath())
				.scope(ActivityScope.BOTH)
				.owner(event.getOwner())
				.ownerType(event.getOwnerType())
				.ownerName(event.getOwnerName())
				.currentPermission(event.getCurrentPermissionName())
				.originalPermission(event.getOriginalPermissionName())
				.operator(event.getUserId())
				.operation(OperationType.AUTHORIZE_UPDATE)
				.build();
		activityService.save(activity);

	}
}
