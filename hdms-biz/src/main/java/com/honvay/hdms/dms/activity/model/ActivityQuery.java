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
