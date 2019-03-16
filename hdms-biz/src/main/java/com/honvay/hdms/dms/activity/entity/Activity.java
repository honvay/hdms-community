/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author LIQIU
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hdms_activity")
public class Activity {

	@TableId
	private Integer id;

	private Integer documentId;

	private Integer directoryId;

	private String operation;

	private String documentName;

	private String originalName;

	private String currentName;

	private Integer operator;

	private Date operateDate;

	private Integer scope;

	private String content;

	private Integer income;

	private Integer incomeMount;

	private String incomeName;

	private Integer outcome;

	private Integer outcomeMount;

	private String outcomeName;

	private Integer owner;

	private Integer ownerType;

	private String ownerName;

	private String currentPermission;

	private String originalPermission;

	private String currentDescription;

	private String originalDescription;

	private String currentTags;

	private String originalTags;

	private String path;

	private String documentType;

}
