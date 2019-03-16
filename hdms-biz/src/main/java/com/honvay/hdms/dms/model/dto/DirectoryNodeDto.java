package com.honvay.hdms.dms.model.dto;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
@Data
public class DirectoryNodeDto {

	private Integer id;

	private String name;

	private String fullName;

	private Integer mount;

	private Integer createdBy;

	private String path;
}
