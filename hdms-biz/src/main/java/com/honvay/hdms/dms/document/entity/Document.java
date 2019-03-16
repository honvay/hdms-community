/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Honvay Cloud Object
 *
 * @author LIQIU
 */
@Data
@TableName("hdms_document")
public class Document {

	@TableId
	private Integer id;

	private String code;

	private String name;

	private String icon;

	private String path;

	private Long size;

	private String contentType;

	private Boolean deleted;

	private Integer parent;

	private String md5;

	private String type;

	private Integer mount;

	private Integer createdBy;

	private Integer updatedBy;

	private Date createDate;

	private Date updateDate;

	private Integer deletedBy;

	private Date deleteDate;

	private Integer root;

	private Integer permission;

	private String accessibility;

	private String fullName;

	private Boolean locked;

	private Integer lockedBy;

	private Date lockDate;

	private String tags;

	private String ext;

	private String description;

}
