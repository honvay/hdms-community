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
