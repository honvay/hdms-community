package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
public class RenameEvent extends ApplicationEvent {

	private Document document;

	private String originalName;
	private Integer userId;

	public RenameEvent(Document document, String originalName, Integer userId) {
		super(document);
		this.document = document;
		this.originalName = originalName;
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

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
}
