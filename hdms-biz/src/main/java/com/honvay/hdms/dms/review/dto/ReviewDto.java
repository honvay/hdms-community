package com.honvay.hdms.dms.review.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
@Data
public class ReviewDto {

	private Integer id;

	private String content;

	private Integer reviewer;

	private Date reviewDate;

	private String name;

	private String avatar;

}
