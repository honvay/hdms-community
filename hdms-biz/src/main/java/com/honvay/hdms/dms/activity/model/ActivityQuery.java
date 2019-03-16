/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/3/13
 **/
@Data
public class ActivityQuery {

	private Date startDate;

	private Date endDate;

	private String documentName;

	private String operatorName;

	private String operation;

	@NotNull
	@Min(1)
	@Max(100)
	private Long page;

	@NotNull
	@Min(20)
	@Max(200)
	private Integer size;

	private Long start;

	private Long end;

}
