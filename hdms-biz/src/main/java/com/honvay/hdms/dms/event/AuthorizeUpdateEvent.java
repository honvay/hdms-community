package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizeUpdateEvent extends ApplicationEvent {

	private final Integer owner;
	private final Integer ownerType;
	private final String ownerName;
	private final Integer currentPermissionId;
	private final String currentPermissionName;
	private final Integer originalPermissionId;
	private final String originalPermissionName;
	private Integer userId;
	private final Document document;

	public AuthorizeUpdateEvent(Document document, Integer owner, Integer ownerType, String ownerName,
								Integer currentPermissionId, String currentPermissionName,
								Integer originalPermissionId, String originalPermissionName, Integer userId) {
		super(document);
		this.document = document;
		this.owner = owner;
		this.ownerType = ownerType;
		this.ownerName = ownerName;
		this.currentPermissionId = currentPermissionId;
		this.currentPermissionName = currentPermissionName;
		this.originalPermissionId = originalPermissionId;
		this.originalPermissionName = originalPermissionName;
		this.userId = userId;
	}
}
