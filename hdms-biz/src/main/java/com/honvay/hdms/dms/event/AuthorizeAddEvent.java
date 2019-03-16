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
public class AuthorizeAddEvent extends ApplicationEvent {

	private final Integer owner;
	private final Integer ownerType;
	private final String ownerName;
	private final Integer permissionId;
	private final String permissionName;
	private Integer userId;
	private final Document document;

	public AuthorizeAddEvent(Document document, Integer owner, Integer ownerType, String ownerName, Integer permissionId, String permissionName, Integer userId) {
		super(document);
		this.document = document;
		this.owner = owner;
		this.ownerType = ownerType;
		this.ownerName = ownerName;
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.userId = userId;
	}
}
