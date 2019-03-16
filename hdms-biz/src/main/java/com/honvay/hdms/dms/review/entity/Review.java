package com.honvay.hdms.dms.review.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_review")
public class Review {

	@TableId
	private Integer id;

	@NotNull
	public Integer documentId;

	@NotEmpty
	public String content;

	public Date reviewDate;

	public Integer userId;

	public Integer numberOfLike;

	public Integer numberOfHate;
}
