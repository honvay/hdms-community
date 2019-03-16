package com.honvay.hdms.dms.model.dto;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.authorize.model.AuthorizeVo;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
@Data
public class DirectoryDetailDto {

	private List<DocumentFullDto> files;

	private List<DocumentSimpleDto> path;

	private DocumentFullDto current;

	private List<AuthorizeVo> authorizes;

	private List<Activity> activities;

	private Set<String> permissions;

}
