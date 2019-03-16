package com.honvay.hdms.department.model;

import lombok.Data;

/**
 * @author LIQIU
 * created on 2019/3/11
 **/
@Data
public class DepartmentDto {

	private Integer id;

	private String name;

	private String code;

	private Integer orderNo;

	private DepartmentDto parent;

}
