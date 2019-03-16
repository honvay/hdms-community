package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizeRemoveEvent extends ApplicationEvent {

	private final Integer owner;
	private final Integer ownerType;
	private final String ownerName;
	private String permissionName;
	private Integer userId;
	private final Document document;

	public AuthorizeRemoveEvent(Document document, Integer owner, Integer ownerType, String ownerName, String permissionName,Integer userId) {
		super(document);
		this.document = document;
		this.owner = owner;
		this.ownerType = ownerType;
		this.ownerName = ownerName;
		this.permissionName = permissionName;
		this.userId = userId;
	}

}
