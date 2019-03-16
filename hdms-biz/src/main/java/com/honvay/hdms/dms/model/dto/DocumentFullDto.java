package com.honvay.hdms.dms.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
@Data
public class DocumentFullDto {
	private Integer id;
	private String name;
	private String accessibility;
	private Integer mount;
	private Integer parent;
	private String tags;
	private String path;
	private String code;
	private Integer permission;
	private Integer root;
	private Long size;
	private String type;
	private Date createDate;
	private Date updateDate;
	private String fullName;
	private DocumentOperatorDto creationBy;
	private DocumentOperatorDto updatedBy;
	private DocumentOperatorDto lockedBy;
	private DocumentOperatorDto deleteBy;
	private Date lockDate;
	private Boolean locked;
	private Date deleteDate;
	private Boolean deleted;
	private Integer favoriteId;
	private String contentType;
	private String description;
}
