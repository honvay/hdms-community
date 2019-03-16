/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.web;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.authentication.annotation.Authentication;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.service.DocumentWriteService;
import com.honvay.hdms.dms.model.request.*;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@RestController
@RequestMapping("/fs")
public class WriteController extends BaseController {

	@Autowired
	private DocumentWriteService documentWriteService;

	/**
	 * 创建文件夹
	 *
	 * @param request
	 * @param authenticatedUser
	 * @return
	 */
	@PostMapping("/createDirectory")
	public Result<String> createDirectory(@RequestBody @Valid MkdirRequest request,
										  @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.createDirectory(request);
		return this.success();
	}


	/**
	 * 复制文件
	 *
	 * @param request
	 * @param authenticatedUser
	 * @return
	 */
	@PostMapping("/copy")
	public Result<String> copy(@RequestBody @Valid CopyRequest request,
							   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.copy(request);
		return this.success();
	}

	/**
	 * 移动文件
	 *
	 * @param request
	 * @param authenticatedUser
	 * @return
	 */
	@PostMapping("/move")
	public Result<String> move(@RequestBody @Valid MoveRequest request,
							   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.move(request);
		return this.success();
	}

	@PostMapping("/transfer")
	@PreAuthorize(value = "hasRole('" + User.ROLE_SYS_ADMIN + "')")
	public Result<String> transfer(Integer source, Integer target) {
		this.documentWriteService.transfer(source, target);
		return this.success();
	}

	/**
	 * 设置标签
	 *
	 * @param request
	 * @param authenticatedUser
	 * @return
	 */
	@PostMapping("/tag")
	public Result<String> tag(@RequestBody @Valid UpdateTagRequest request,
							  @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.updateTags(request);
		return this.success();
	}

	/**
	 * 修改备注
	 *
	 * @param request
	 * @param authenticatedUser
	 * @return
	 */
	@PostMapping("/updateDescription")
	public Result<String> updateDesc(@RequestBody @Valid UpdateDescRequest request,
									 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.updateDesc(request);
		return this.success();
	}

	/**
	 * 删除文件
	 *
	 * @param request
	 * @param user
	 * @return
	 */
	@PostMapping("/remove")
	@Authentication(value = PermissionType.REMOVE, multiple = true)
	public Result<String> remove(@RequestBody @Valid RemoveRequest request,
								 @AuthenticationPrincipal AuthenticatedUser user) {
		request.setUser(user);
		this.documentWriteService.remove(request);
		return this.success();
	}

	/**
	 * 锁定文件
	 *
	 * @param request
	 * @param user
	 * @return
	 */
	@PostMapping("/lock")
	@Authentication(value = PermissionType.LOCK, multiple = true)
	public Result<String> lock(@RequestBody @Valid LockRequest request,
							   @AuthenticationPrincipal AuthenticatedUser user) {
		request.setUser(user);
		this.documentWriteService.lock(request);
		return this.success();
	}

	/**
	 * 解锁文件
	 *
	 * @param request
	 * @param user
	 * @return
	 */
	@PostMapping("/unlock")
	@Authentication(value = PermissionType.LOCK, multiple = true)
	public Result<String> unlock(@RequestBody @Valid UnLockRequest request,
								 @AuthenticationPrincipal AuthenticatedUser user) {
		request.setUser(user);
		this.documentWriteService.unlock(request);
		return this.success();
	}

	/**
	 * 重命名文件
	 *
	 * @param request
	 * @param user
	 * @return
	 */
	@PostMapping("/rename")
	@Authentication(PermissionType.RENAME)
	public Result<Document> rename(@RequestBody @Valid RenameRequest request,
								   @AuthenticationPrincipal AuthenticatedUser user) {
		request.setUser(user);
		return this.success(this.documentWriteService.rename(request));
	}


}
