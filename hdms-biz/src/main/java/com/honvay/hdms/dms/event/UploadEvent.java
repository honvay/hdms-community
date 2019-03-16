package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
public class UploadEvent extends ApplicationEvent {

	private Document document;

	private Integer userId;

	public UploadEvent(Document document, Integer userId) {
		super(document);
		this.document = document;
		this.userId = userId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
