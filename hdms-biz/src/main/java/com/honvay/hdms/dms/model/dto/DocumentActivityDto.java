package com.honvay.hdms.dms.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.honvay.hdms.dms.activity.model.AuthorizeActivityDto;
import com.honvay.hdms.dms.activity.model.DescActivityDto;
import com.honvay.hdms.dms.activity.model.TagsActivityDto;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
@Data
public class DocumentActivityDto {

	private Integer id;

	private String operation;

	private Date operateDate;

	private String content;

	private DocumentOperatorDto operator;

	/**
	 * 入方
	 */
	private DirectoryMountDto income;

	/**
	 * 出方
	 */
	private DirectoryMountDto outcome;

	/**
	 * 变更
	 */
	private DocumentChangeDto change;

	/**
	 * 文件快照
	 */
	private DocumentSnapshotDto document;

	/**
	 * 当前文档
	 */
	private DocumentSnapshotDto directory;

	private AuthorizeActivityDto authorize;

	private DescActivityDto description;

	private TagsActivityDto tags;

}
