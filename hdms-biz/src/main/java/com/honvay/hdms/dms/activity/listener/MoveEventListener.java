package com.honvay.hdms.dms.activity.listener;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.enums.ActivityScope;
import com.honvay.hdms.dms.activity.enums.OperationType;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.event.DownloadEvent;
import com.honvay.hdms.dms.event.MoveEvent;
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
public class MoveEventListener implements ApplicationListener<MoveEvent> {

	@Autowired
	private ActivityService activityService;

	@Override
	public void onApplicationEvent(MoveEvent event) {

		Document document = event.getDocument();

		if (log.isDebugEnabled()) {
			log.debug("Move documentId:{},from:{},to:{}", document.getId(), event.getOriginalParent(), document.getParent());
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
					.operation(OperationType.MOVE)
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
			Activity moveInActivity = Activity.builder()
					.directoryId(document.getParent())
					.path(event.getCurrentParent().getPath())
					.documentId(document.getId())
					.documentName(document.getName())
					.outcomeMount(event.getOriginalMount())
					.operator(event.getUserId())
					.scope(ActivityScope.DIRECTORY)
					.operation(OperationType.MOVE_IN)
					.build();
			if (event.getOriginalParent() != null) {
				moveInActivity.setOutcome(event.getOriginalParent().getId());
				moveInActivity.setOutcomeName(event.getOriginalParent().getName());
			}
			activityService.save(moveInActivity);
		}

		if (event.getOriginalParent() != null) {
			Activity moveOutActivity = Activity.builder()
					.directoryId(event.getOriginalParent().getId())
					.path(event.getOriginalParent().getPath())
					.documentId(document.getId())
					.documentName(document.getName())
					.incomeMount(document.getMount())
					.scope(ActivityScope.DIRECTORY)
					.operation(OperationType.MOVE_OUT)
					.build();
			if (event.getCurrentParent() != null) {
				moveOutActivity.setIncome(event.getCurrentParent().getId());
				moveOutActivity.setIncomeName(event.getCurrentParent().getName());
			}
			activityService.save(moveOutActivity);
		}
	}
}
