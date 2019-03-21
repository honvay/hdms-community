/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.activity.service.ActivityService;
import com.honvay.hdms.dms.authorize.authentication.annotation.AuthenticationSubject;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.model.dto.DirectoryDetailDto;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentDetailDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.dms.recent.service.RecentService;
import com.honvay.hdms.dms.review.service.ReviewService;
import com.honvay.hdms.dms.token.AccessTokenStore;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping(value = {"/fs", "/api"})
public class ReadController extends BaseController {

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private RecentService recentService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private AccessTokenStore accessTokenStore;


	@RequestMapping("/search")
	public Result<Page<DocumentFullDto>> search(String keyword, Integer page, Integer size,
												@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		Set<String> paths = authorizeService.findPathsByOwner(authenticatedUser, PermissionType.VIEW);
		Page<DocumentFullDto> pageResult = this.documentReadService.search(keyword, paths,
				authenticatedUser.getUserMount().getId(), authenticatedUser.getOrganizationMount().getId(), page, size);
		return this.success(pageResult);
	}

	@RequestMapping("/directory")
	public Result<DirectoryDetailDto> load(Integer id, Integer mount,
										   @AuthenticationPrincipal AuthenticatedUser user) {

		Assert.isTrue(id != null || mount != null, "参数错误");

		DirectoryDetailDto directoryDetailDto = new DirectoryDetailDto();
		if (id != null) {
			Document document = this.documentReadService.get(id);
			Assert.isTrue(document != null && !document.getDeleted(), "文件不存在或者已被删除");
			Assert.isTrue(authorizeService.checkPermission(user, document, PermissionType.VIEW), "无权限访问该文件夹");

			DocumentFullDto documentFullDto = this.documentReadService.getFullDocument(id, DocumentType.DIRECTORY.getCode());
			Assert.notNull(documentFullDto, "文件夹不存在或已删除");
			directoryDetailDto.setCurrent(documentFullDto);
			directoryDetailDto.setAuthorizes(this.authorizeService.findByDocumentId(id));
			directoryDetailDto.setActivities(this.activityService.findDirectoryActivityByPath(documentFullDto.getPath()));
			directoryDetailDto.setPermissions(this.authorizeService.findPermissionByDocumentIdAndOwner(user, document));
			List<Integer> documentIds = Stream.of(documentFullDto.getPath().split("/")).map(Integer::valueOf).collect(Collectors.toList());
			//排除自己
			documentIds.remove(documentIds.size() - 1);
			if (documentIds.size() > 0) {
				directoryDetailDto.setPath(this.documentReadService.findSimpleDocument(documentIds));
			}
		}

		//过滤权限
		List<DocumentFullDto> documents = this.documentReadService.findFullDocument(id, mount);
		//企业文档需要判断权限
		if (!user.getMountId().equals(mount)) {
			documents = this.authorizeService.filterDocuments(user, documents, PermissionType.VIEW);
		}

		directoryDetailDto.setFiles(documents);

		return Result.success(directoryDetailDto);
	}

	@RequestMapping("/folder")
	public List<DirectoryNodeDto> folder(Integer id, Integer mount, String excludes, String permission,
										 @AuthenticationPrincipal AuthenticatedUser user) {
		if (StringUtils.isEmpty(permission)) {
			permission = PermissionType.CREATE;
		}

		if (id == null && mount == null) {
			List<DirectoryNodeDto> directories = new ArrayList<>();
			DirectoryNodeDto enterprise = new DirectoryNodeDto();
			Mount organizationMount = user.getOrganizationMount();
			enterprise.setName(organizationMount.getName());
			enterprise.setFullName(organizationMount.getName());
			enterprise.setMount(organizationMount.getId());

			DirectoryNodeDto my = new DirectoryNodeDto();
			Mount userMount = user.getUserMount();
			my.setName(userMount.getName());
			my.setFullName(userMount.getName());
			my.setMount(userMount.getId());

			directories.add(enterprise);
			directories.add(my);
			return directories;
		} else {
			id = id != null ? id : -1;
			List<DirectoryNodeDto> directories = this.documentReadService.findDirectory(id, mount);
			if (!user.getMountId().equals(mount)) {
				directories = authorizeService.filterDirectories(user, directories, permission);
			}
			return directories;
		}
	}

	@RequestMapping("/favorites")
	public Result<List<DocumentFullDto>> favorites(@AuthenticationPrincipal AuthenticatedUser user) {
		return this.success(this.documentReadService.findFavorites(user.getId()));
	}

	@RequestMapping("/recent")
	public Result<List<DocumentFullDto>> recent(@AuthenticationPrincipal AuthenticatedUser user) {
		return this.success(this.documentReadService.findRecent(user.getId()));
	}


	@RequestMapping("/document/{id}")
	public Result<DocumentDetailDto> get(@AuthenticationSubject @PathVariable Integer id,
										 @AuthenticationPrincipal AuthenticatedUser user) {

		Assert.isTrue(authorizeService.checkPermission(user, id, null, PermissionType.VIEW), "无权限访问");

		Document document = this.documentReadService.get(id);
		Assert.isTrue(document != null && !document.getDeleted(), "文件不存在或者已被删除");
		if (DocumentType.isFile(document.getType())) {
			//收集最近的文档
			recentService.add(id, user.getId());
		}
		DocumentDetailDto documentDetailDto = new DocumentDetailDto();
		DocumentFullDto documentFullDto = this.documentReadService.getFullDocument(id, DocumentType.FILE.getCode());
		Assert.notNull(documentFullDto, "文件不存在或者已被删除");
		documentDetailDto.setDocument(documentFullDto);
		documentDetailDto.setPermissions(authorizeService.findPermissionByDocumentIdAndOwner(user, document));
		documentDetailDto.setActivities(this.activityService.findDirectoryActivityByPath(documentFullDto.getPath()));
		documentDetailDto.setReviews(this.reviewService.findByDocumentId(id));
		documentDetailDto.setToken(accessTokenStore.put(document.getCode()));
		return this.success(documentDetailDto);
	}
}
