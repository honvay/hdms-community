/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
