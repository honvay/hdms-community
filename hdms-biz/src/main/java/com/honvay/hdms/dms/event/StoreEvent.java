package com.honvay.hdms.dms.event;

import org.springframework.context.ApplicationEvent;

public class StoreEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StoreEvent(Object source) {
		super(source);
	}

}
