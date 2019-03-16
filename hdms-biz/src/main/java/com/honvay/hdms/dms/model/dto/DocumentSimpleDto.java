package com.honvay.hdms.dms.model.dto;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
@Data
public class DocumentSimpleDto {

	private Integer id;

	private String name;

	private String accessibility;

	private String type;

	private Long size;

}
