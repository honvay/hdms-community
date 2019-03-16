/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
