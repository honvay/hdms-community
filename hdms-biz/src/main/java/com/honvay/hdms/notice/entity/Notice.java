package com.honvay.hdms.notice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_notice")
public class Notice {

	@TableId
	private Integer id;

	@NotEmpty
	@Length(max = 100, min = 2)
	private String title;

	@NotEmpty
	@Length(max = 1000, min = 2)
	private String content;

	private Date startDate;

	private Date endDate;

	private boolean enable;

}
