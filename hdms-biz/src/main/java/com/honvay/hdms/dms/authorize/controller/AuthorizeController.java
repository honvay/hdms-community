/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.authorize.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.entity.Authorize;
import com.honvay.hdms.dms.authorize.enums.OwnerType;
import com.honvay.hdms.dms.authorize.model.AuthorizeDto;
import com.honvay.hdms.dms.authorize.model.AuthorizeUpdate;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/fs/authorize")
public class AuthorizeController extends BaseController {

	@Autowired
	private AuthorizeService authorizeService;

	@RequestMapping("/list")
	public Result<Object> list(Integer documentId) {
		return this.success(this.authorizeService.findByDocumentId(documentId));
	}

	@RequestMapping("/remove/{id}")
	public Result<String> remove(@PathVariable Integer id,
								 @AuthenticationPrincipal AuthenticatedUser user) {
		this.authorizeService.delete(id, user.getId());
		return this.success();
	}

	@RequestMapping("/update")
	public Result<String> update(@RequestBody @Valid AuthorizeUpdate authorizeUpdate,
								 @AuthenticationPrincipal AuthenticatedUser user) {
		Authorize authorize = this.authorizeService.get(authorizeUpdate.getId());
		this.authorizeService.updatePermission(authorize, authorizeUpdate.getPermissionId(), user.getId());
		return this.success();
	}

	@RequestMapping("/add")
	public Object add(@RequestBody @Valid AuthorizeDto authorizeDto,
					  @AuthenticationPrincipal AuthenticatedUser user) {

		Assert.isTrue(CollectionUtils.isNotEmpty(authorizeDto.getDepartmentIds()) || CollectionUtils.isNotEmpty(authorizeDto.getUserIds()), "参数错误");

		if (authorizeDto.getUserIds() != null) {
			for (Integer owner : authorizeDto.getUserIds()) {
				Authorize authorize = new Authorize();
				authorize.setOwner(owner);
				authorize.setOwnerType(OwnerType.USER.getCode());
				authorize.setPermissionId(authorizeDto.getPermission());
				authorize.setDocumentId(authorizeDto.getDocumentId());
				authorizeService.add(authorize, user.getId());
			}
		}
		if (authorizeDto.getDepartmentIds() != null) {
			for (Integer owner : authorizeDto.getDepartmentIds()) {
				Authorize authorize = new Authorize();
				authorize.setOwner(owner);
				authorize.setOwnerType(OwnerType.DEPARTMENT.getCode());
				authorize.setPermissionId(authorizeDto.getPermission());
				authorize.setDocumentId(authorizeDto.getDocumentId());
				authorizeService.add(authorize, user.getId());
			}
		}
		return this.success();
	}
}
