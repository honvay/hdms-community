package com.honvay.hdms.dms.event;

import org.springframework.context.ApplicationEvent;

public class ChangeEvent  extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangeEvent(Object source) {
		super(source);
	}
}
