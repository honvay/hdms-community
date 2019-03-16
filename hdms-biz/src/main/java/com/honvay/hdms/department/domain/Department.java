package com.honvay.hdms.department.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 *
 */
@Data
@TableName("hdms_department")
public class Department implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Integer id;

	private String name;

	@Length(min = 2)
	private String code;

	private Integer orderNo;

	private Integer parent;

	private String path;

	private String fullPinyin;

	private String shortPinyin;

	private Integer mountId;
}
