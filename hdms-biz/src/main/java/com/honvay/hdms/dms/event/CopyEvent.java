/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.event;

import com.honvay.hdms.dms.document.entity.Document;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * created on 2019/2/24
 **/
@Data
public class CopyEvent extends ApplicationEvent {

	private Document document;
	private final Document originalParent;
	private final Document currentParent;
	private final Integer originalMount;
	private final Integer currentMount;
	private Integer userId;

	public CopyEvent(Document document, Document originalParent, Document currentParent, Integer originalMount, Integer currentMount, Integer userId) {
		super(document);
		this.document = document;
		this.originalParent = originalParent;
		this.currentParent = currentParent;
		this.originalMount = originalMount;
		this.currentMount = currentMount;
		this.userId = userId;
	}
}
