/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.authorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.department.service.DepartmentService;
import com.honvay.hdms.dms.authorize.entity.Authorize;
import com.honvay.hdms.dms.authorize.enums.OwnerType;
import com.honvay.hdms.dms.authorize.mapper.AuthorizeMapper;
import com.honvay.hdms.dms.authorize.model.AuthorizeVo;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.event.AuthorizeAddEvent;
import com.honvay.hdms.dms.event.AuthorizeRemoveEvent;
import com.honvay.hdms.dms.event.AuthorizeUpdateEvent;
import com.honvay.hdms.dms.model.AuthorizedPermission;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.dms.model.dto.OwnerPermissionDto;
import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.dms.permission.service.PermissionService;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LIQIU
 */
@Service
public class AuthorizeServiceImpl extends BaseServiceImpl<Authorize, Integer> implements AuthorizeService {

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private AuthorizeMapper authorizeMapper;

	private static OwnerPermissionDto findPermission(Map.Entry<String, List<OwnerPermissionDto>> entry) {
		if (entry.getValue().size() > 1) {
			return entry.getValue().stream().max(Comparator.comparing(ownerPermissionDto ->
					ownerPermissionDto.getPath().length())).get();
		} else if (entry.getValue().size() == 1) {
			return entry.getValue().get(0);
		}
		return null;
	}

	private static AuthorizeVo findAuthorize(Map.Entry<String, List<AuthorizeVo>> authorizeEntry) {
		if (authorizeEntry.getValue().size() > 1) {
			//取最长的
			return authorizeEntry.getValue().stream().max(Comparator.comparing(authorizedPermission ->
					authorizedPermission.getPath().length())).get();
		} else if (authorizeEntry.getValue().size() == 1) {
			return authorizeEntry.getValue().get(0);
		}
		return null;
	}


	@Override
	public List<Authorize> findByDocument(Integer documentId) {
		LambdaQueryWrapper<Authorize> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Authorize::getDocumentId, documentId);
		return this.authorizeMapper.selectList(wrapper);
	}

	@Override
	public void delete(Integer id, Integer userId) {

		Authorize authorize = this.get(id);


		Permission permission = this.permissionService.get(authorize.getPermissionId());
		String permissionName = permission != null ? permission.getName() : null;

		Document document = this.documentReadService.get(authorize.getDocumentId());
		String ownerName = this.getOwnerName(authorize);

		super.delete(id);

		AuthorizeRemoveEvent event = new AuthorizeRemoveEvent(document, authorize.getOwner(), authorize.getOwnerType(), ownerName, permissionName, userId);
		applicationEventPublisher.publishEvent(event);
	}

	@Override
	public void deleteByDocumentId(Integer documentId) {
		LambdaUpdateWrapper<Authorize> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(Authorize::getDocumentId, documentId);
		this.authorizeMapper.delete(wrapper);
	}

	@Override
	public void updatePermission(Authorize authorize, Integer permissionId, Integer userId) {
		if (!authorize.getPermissionId().equals(permissionId)) {
			String ownerName;
			ownerName = getOwnerName(authorize);

			Document document = documentReadService.get(authorize.getDocumentId());

			Permission originalPermission = this.permissionService.get(authorize.getPermissionId());
			Assert.notNull(originalPermission, "权限不存在");

			Permission currentPermission = this.permissionService.get(permissionId);
			Assert.notNull(originalPermission, "权限不存在");

			authorize.setPermissionId(permissionId);

			AuthorizeUpdateEvent event = new AuthorizeUpdateEvent(document, authorize.getOwner(), authorize.getOwnerType(), ownerName,
					currentPermission.getId(), currentPermission.getName(),
					originalPermission.getId(), originalPermission.getName(), userId);
			applicationEventPublisher.publishEvent(event);
			this.update(authorize);
		}
	}

	@Override
	public List<DocumentFullDto> filterDocuments(AuthenticatedUser user, List<DocumentFullDto> documents, String permission) {
		List<AuthorizedPermission> permissions = this.findByOwner(user);
		documents = documents.stream().filter(document -> {
			if (DocumentType.isFile(document.getType())) {
				return true;
			}
			if (document.getCreationBy().getId().equals(user.getId())) {
				return true;
			} else {
				return permissions.stream()
						.filter(authorizedPermission -> document.getPath().startsWith(authorizedPermission.getPath()))
						.max(Comparator.comparing(authorizedPermission -> authorizedPermission.getPath().length()))
						.filter(authorizedPermission -> authorizedPermission.getPermissions().contains(permission))
						.isPresent();
			}
		}).collect(Collectors.toList());
		return documents;
	}

	@Override
	public List<DirectoryNodeDto> filterDirectories(AuthenticatedUser user, List<DirectoryNodeDto> directories, String permission) {
		List<AuthorizedPermission> permissions = this.findByOwner(user);
		directories = directories.stream().filter(document -> {
			if (document.getCreatedBy().equals(user.getId())) {
				return true;
			} else {
				return permissions.stream()
						.filter(authorizedPermission -> document.getPath().startsWith(authorizedPermission.getPath()))
						.max(Comparator.comparing(authorizedPermission -> authorizedPermission.getPath().length()))
						.filter(authorizedPermission -> authorizedPermission.getPermissions().contains(permission))
						.isPresent();
			}
		}).collect(Collectors.toList());
		return directories;
	}

	@Override
	public boolean checkPermission(AuthenticatedUser user, Document document, String permission) {
		if (user.getId().equals(document.getCreatedBy())) {
			return true;
		} else {
			Set<String> permissions = this.findPermissionByDocumentIdAndOwner(user, document.getPath(), document.getId());
			return permissions.contains(permission);
		}
	}

	@Override
	public boolean checkPermission(AuthenticatedUser user, Integer documentId, Integer mountId, String permission) {
		if (documentId == null) {
			if (user.getMountId().equals(mountId)) {
				return true;
			} else if (user.getOrganizationMount().getId().equals(mountId)) {
				if (permission.equals(PermissionType.CREATE)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			Document document = this.documentReadService.get(documentId);
			if (user.getId().equals(document.getCreatedBy())) {
				return true;
			} else {
				Set<String> permissions = this.findPermissionByDocumentIdAndOwner(user, document.getPath(), documentId);
				return permissions.contains(permission);
			}
		}
		return false;
	}

	private String getOwnerName(Authorize authorize) {
		String ownerName;
		if (authorize.getOwnerType().equals(OwnerType.USER.getCode())) {
			User user = this.userService.get(authorize.getOwner());
			Assert.notNull(user, "用户不存在");
			ownerName = user.getName();
		} else {
			Department department = this.departmentService.get(authorize.getOwner());
			Assert.notNull(department, "部门不存在");
			ownerName = department.getName();
		}
		return ownerName;
	}

	@Override
	public void add(Authorize authorize, Integer userId) {
		Authorize existsAuthorize = this.getAuthorize(authorize.getDocumentId(), authorize.getOwner(), authorize.getOwnerType());
		if (existsAuthorize != null) {
			this.updatePermission(authorize, authorize.getPermissionId(), userId);
		} else {
			this.save(authorize);
			Document document = documentReadService.get(authorize.getDocumentId());
			String ownerName = getOwnerName(authorize);

			Permission permission = this.permissionService.get(authorize.getPermissionId());
			Assert.notNull(permission, "权限不存在");

			AuthorizeAddEvent event = new AuthorizeAddEvent(document, authorize.getOwner(), authorize.getOwnerType(), ownerName,
					authorize.getPermissionId(), permission.getName(), userId);
			applicationEventPublisher.publishEvent(event);
		}
	}

	@Override
	public List<AuthorizedPermission> findByOwner(AuthenticatedUser user) {
		String path = user.getDepartment().getPath();
		List<Integer> departmentIds = Stream.of(path.split(",")).map(Integer::valueOf).collect(Collectors.toList());
		List<OwnerPermissionDto> ownerPermissions = authorizeMapper.findByOwner(user.getId(), departmentIds);

		return ownerPermissions.stream().filter(ownerPermissionDto -> StringUtils.isNotEmpty(ownerPermissionDto.getPermissions()))
				.map(ownerPermissionDto -> {
					AuthorizedPermission authorizedPermission = new AuthorizedPermission();
					authorizedPermission.setId(ownerPermissionDto.getId());
					authorizedPermission.setOwner(ownerPermissionDto.getOwner());
					authorizedPermission.setOwnerType(ownerPermissionDto.getOwnerType());
					authorizedPermission.setDocumentId(ownerPermissionDto.getDocumentId());
					authorizedPermission.setPath(ownerPermissionDto.getPath());
					authorizedPermission.setPermissions(new HashSet<>(Arrays.asList(ownerPermissionDto.getPermissions().split(","))));
					return authorizedPermission;
				}).collect(Collectors.toList());
	}

	@Override
	public Set<String> findPathsByOwner(AuthenticatedUser user, String permission) {
		String path = user.getDepartment().getPath();
		List<Integer> departmentIds = Stream.of(path.split(",")).map(Integer::valueOf).collect(Collectors.toList());
		List<OwnerPermissionDto> ownerPermissions = authorizeMapper.findByOwner(user.getId(), departmentIds);

		return ownerPermissions.stream().filter(ownerPermissionDto ->
				StringUtils.isNotEmpty(ownerPermissionDto.getPermissions()) && ownerPermissionDto.getPermissions().contains(permission))
				.map(OwnerPermissionDto::getPath).collect(Collectors.toSet());
	}


	@Override
	public List<AuthorizeVo> findByDocumentId(Integer documentId) {
		Document document = documentReadService.get(documentId);
		List<Integer> documentIds = Stream.of(document.getPath().split("/")).map(Integer::valueOf).collect(Collectors.toList());
		List<AuthorizeVo> authorizes = authorizeMapper.findByDocumentId(documentIds);

		Map<String, List<AuthorizeVo>> groupedAuthorizes = authorizes.stream().collect(Collectors.groupingBy(authorizedPermission -> authorizedPermission.getOwner() + ":" + authorizedPermission.getOwnerType()));
		// 4/6/13 -> 4/ 4/6/ 4/6/13 -> 4/6/13
		//取最长的
		authorizes = groupedAuthorizes.entrySet().stream().map(AuthorizeServiceImpl::findAuthorize).collect(Collectors.toList());
		authorizes.forEach(authorizeVo -> authorizeVo.setInherited(!authorizeVo.getDocumentId().equals(documentId)));
		return authorizes;
	}

	@Override
	public Set<String> findPermissionByDocumentIdAndOwner(AuthenticatedUser user, Document document) {
		return this.findPermissionByDocumentIdAndOwner(user, document.getPath(), document.getId());
	}

	@Override
	public Set<String> findPermissionByDocumentIdAndOwner(AuthenticatedUser user, String path, Integer documentId) {
		List<Integer> documentIds = Stream.of(path.split("/")).map(Integer::valueOf).collect(Collectors.toList());
		String departmentPath = user.getDepartment().getPath();
		List<Integer> departmentIds = Stream.of(departmentPath.split(",")).map(Integer::valueOf).collect(Collectors.toList());
		List<OwnerPermissionDto> authorizes = authorizeMapper.findByDocumentIdAndOwner(user.getId(), departmentIds, documentIds);
		Map<String, List<OwnerPermissionDto>> groupedAuthorizes = authorizes.stream().collect(Collectors.groupingBy(ownerPermissionDto -> ownerPermissionDto.getOwner() + ":" + ownerPermissionDto.getOwnerType()));

		authorizes = groupedAuthorizes.entrySet().stream().map(AuthorizeServiceImpl::findPermission).collect(Collectors.toList());
		return authorizes.stream().flatMap(permission -> {
					if (StringUtils.isNotEmpty(permission.getPermissions())) {
						return Arrays.stream(permission.getPermissions().split(","));
					} else {
						return Stream.empty();
					}
				}
		).collect(Collectors.toSet());
	}


	@Override
	public List<Authorize> findAuthorizes(Integer documentId, Integer ownerType) {
		LambdaQueryWrapper<Authorize> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Authorize::getDocumentId, documentId)
				.eq(Authorize::getOwnerType, ownerType);
		return this.authorizeMapper.selectList(wrapper);
	}

	@Override
	public Authorize getAuthorize(Integer source, Integer target, Integer type) {
		LambdaQueryWrapper<Authorize> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Authorize::getDocumentId, source)
				.eq(Authorize::getOwner, target)
				.eq(Authorize::getOwnerType, type);
		return this.authorizeMapper.selectOne(wrapper);
	}

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

}
