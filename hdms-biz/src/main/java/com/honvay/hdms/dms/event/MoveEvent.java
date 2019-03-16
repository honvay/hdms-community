package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * created on 2019/2/24
 **/
@Data
public class MoveEvent extends ApplicationEvent {

	private Document document;

	private final Document originalParent;
	private final Document currentParent;
	private final Integer originalMount;
	private final Integer currentMount;
	private Integer userId;

	public MoveEvent(Document document, Document originalParent, Document currentParent, Integer originalMount, Integer currentMount, Integer userId) {
		super(document);
		this.document = document;
		this.originalParent = originalParent;
		this.currentParent = currentParent;
		this.originalMount = originalMount;
		this.currentMount = currentMount;
		this.userId = userId;
	}
}
