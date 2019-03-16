/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
