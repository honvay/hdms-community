package com.honvay.hdms.department.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author LIQIU
 * created on 2019/3/12
 **/
@Data
public class DepartmentVo {
	protected Integer id;

	private String name;

	private String code;

	private Integer orderNo;

}
