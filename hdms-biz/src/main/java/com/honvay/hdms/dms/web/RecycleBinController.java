/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.web;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.authentication.annotation.Authentication;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.document.service.DocumentWriteService;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.dms.model.request.DeleteRequest;
import com.honvay.hdms.dms.model.request.RevertRequest;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/recycle")
public class RecycleBinController extends BaseController {

	@Autowired
	private DocumentWriteService documentWriteService;

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private AuthorizeService authorizeService;

	@RequestMapping("/list")
	public Result<List<DocumentFullDto>> recycle(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		Set<String> paths = authorizeService.findPathsByOwner(authenticatedUser, PermissionType.REMOVE);
		return this.success(this.documentReadService.findDeletedDocument(paths, authenticatedUser.getUserMount().getId(), authenticatedUser.getOrganizationMount().getId()));
	}

	@PostMapping("/revert")
	public Result<Object> revert(@RequestBody @Valid RevertRequest request,
								 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.revert(request);
		return this.success();
	}

	@PostMapping("/delete")
	@Authentication(value = PermissionType.REMOVE, multiple = true)
	public Result<Object> delete(@RequestBody @Valid DeleteRequest request,
								 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUser(authenticatedUser);
		this.documentWriteService.delete(request);
		return this.success();
	}

	@RequestMapping("/clear")
	public Result<String> clear(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		Set<String> paths = authorizeService.findPathsByOwner(authenticatedUser, PermissionType.REMOVE);
		List<DocumentFullDto> documents = this.documentReadService.findDeletedDocument(paths, authenticatedUser.getUserMount().getId(), authenticatedUser.getOrganizationMount().getId());
		List<Integer> documentIds = documents.stream().map(DocumentFullDto::getId).collect(Collectors.toList());
		DeleteRequest deleteRequest = new DeleteRequest();
		deleteRequest.setDocumentIds(documentIds);
		deleteRequest.setUser(authenticatedUser);
		this.documentWriteService.delete(deleteRequest);
		return this.success();
	}
}
