/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.service.impl;

import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentAccessibility;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.document.repository.DocumentRepository;
import com.honvay.hdms.dms.document.service.DocumentWriteService;
import com.honvay.hdms.dms.event.*;
import com.honvay.hdms.dms.model.request.*;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.mount.service.MountService;
import com.honvay.hdms.dms.permission.enums.PermissionType;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.framework.core.exception.ServiceException;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.enums.UserStatus;
import com.honvay.hdms.user.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.*;

/**
 * @author LIQIU
 * created on 2019/2/28
 **/
@Validated
@Service
@Transactional
public class DocumentWriteServiceImpl implements DocumentWriteService, ApplicationListener {

	@Autowired
	private Storage storage;

	@Autowired
	private MountService mountService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 移动文件夹
	 */
	@Override
	public void move(MoveRequest request) {

		Document parent = null;

		if (request.getParent() != null) {
			parent = this.findById(request.getParent());
		}

		for (Integer documentId : request.getDocumentIds()) {
			Document document = this.findById(documentId);

			//TODO 判断子是否有锁定的文件
			Assert.isTrue(!document.getLocked(), "已锁定的文件不能移动");

			if (DocumentType.isFile(document.getType()) && parent == null && request.getMount().equals(request.getUser().getOrganizationMount().getId())) {
				throw new IllegalArgumentException("不能将文件移动到企业文档根目录");
			}

			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.REMOVE), "无权限复制文件到其他目录");

			if (parent != null) {
				if (DocumentType.isFile(document.getType())) {
					Assert.isTrue(authorizeService.checkPermission(request.getUser(), parent, PermissionType.CREATE), "无权权限创建文件夹");
				} else {
					Assert.isTrue(authorizeService.checkPermission(request.getUser(), parent, PermissionType.UPLOAD), "无权权限复制文件");
				}
				Assert.isTrue(!parent.getId().equals(document.getId()), "不能移动到原文件夹中");
			}

			Document originalParent = null;
			Integer originalMount = document.getMount();
			String originalPath = document.getPath();
			String originalFullName = document.getFullName();

			String originalParentName = null;
			//获取原父文件夹名称
			if (document.getParent() != -1) {
				originalParent = this.findById(document.getParent());
			}

			//移动到根目录下
			if (parent == null) {
				Assert.isTrue(!(request.getMount().equals(document.getMount())
						&& document.getParent() == -1), "不能移动到自身");
				document.setMount(request.getMount());
				document.setParent(-1);
			} else {
				Assert.isTrue(!parent.getId().equals(document.getId()), "不能移动到自身");
				document.setMount(parent.getMount());
				document.setParent(request.getParent());
			}

			//设置唯一的名称
			this.setUniqueName(document);

			//同步父目录的信息
			this.syncByParent(document, parent);
			this.documentRepository.update(document);

			if (DocumentType.isDirectory(document.getType())) {
				//this.moveChildren(document);
				//批量修改文件
				this.documentRepository.batchUpdateChildren(document.getMount(),
						originalPath, document.getPath(),
						originalFullName, document.getFullName());
			}
			MoveEvent moveEvent = new MoveEvent(document, originalParent, parent, originalMount, document.getMount(), request.getUser().getId());
			applicationEventPublisher.publishEvent(moveEvent);
		}
	}

	private void moveChildren(Document document) {
		List<Document> children = this.documentRepository.findByParent(document.getId());
		for (Document child : children) {
			child.setMount(document.getMount());
			this.syncByParent(child, document);
			this.documentRepository.update(child);
			if (DocumentType.isDirectory(child.getType())) {
				this.moveChildren(child);
			}
		}
	}

	@Override
	public void transfer(Integer source, Integer target) {
		User user = this.userService.get(Integer.valueOf(source));
		Assert.isTrue(UserStatus.DISABLED.getValue().equals(user.getStatus()), "只能移交已失效用户的文档!");

		Assert.isTrue(target != null, "参数错误，移交对象缺失");
		Assert.isTrue(!source.equals(target), "参数错误，移交人相同");
		List<Document> children = this.documentRepository.findByParent(source);
		for (Document document : children) {
			//提交到新的用户
			document.setMount(target);
			this.setUniqueName(document);
			this.syncByParent(document);
			this.documentRepository.update(document);
			if (DocumentType.isDirectory(document.getType())) {
				this.transferChildren(document);
			}
		}
	}


	@Override
	public void update(Document document) {
		this.documentRepository.update(document);
	}


	@Override
	public void lock(LockRequest request) {
		for (Integer id : request.getDocumentIds()) {
			Document document = this.findById(id);

			Assert.isTrue(!document.getLocked(), "该文件已经锁定");
			Assert.isTrue(!DocumentType.isDirectory(document.getType()), "不能锁定文件夹");
			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.LOCK), "无权限锁定文件");

			this.documentRepository.lock(id, request.getUser().getId());
			LockEvent lockEvent = new LockEvent(document, request.getUser().getId());
			applicationEventPublisher.publishEvent(lockEvent);
		}
	}

	@Override
	public void unlock(UnLockRequest request) {
		for (Integer id : request.getDocumentIds()) {
			Document document = this.findById(id);
			Assert.isTrue(document.getLockedBy().equals(request.getUser().getId()), "文件夹解锁只能锁定者操作");
			Assert.isTrue(document.getLocked(), "该文件没有锁定");
			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.LOCK), "无权限解锁文件");
			this.documentRepository.unlock(id, request.getUser().getId());
			UnlockEvent unlockEvent = new UnlockEvent(document, request.getUser().getId());
			applicationEventPublisher.publishEvent(unlockEvent);
		}

	}

	/**
	 * 复制文件夹
	 */
	@Override
	public void copy(CopyRequest request) {

		Document parent = null;
		if (request.getParent() != null && request.getParent() != -1) {
			parent = this.findById(request.getParent());
		}

		Assert.isTrue(request.getParent() != null || request.getMount() != null, "复制到目标参数为空");
		for (Integer id : request.getDocumentIds()) {

			Document document = this.findById(id);

			if (DocumentType.isFile(document.getType()) && parent == null && request.getMount().equals(request.getUser().getOrganizationMount().getId())) {
				throw new IllegalArgumentException("不能将文件复制到企业文档根目录");
			}

			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.COPY), "无权限复制文件到其他目录");

			if (parent != null) {
				if (DocumentType.isFile(document.getType())) {
					Assert.isTrue(authorizeService.checkPermission(request.getUser(), parent, PermissionType.CREATE), "无权权限创建文件夹");
				} else {
					Assert.isTrue(authorizeService.checkPermission(request.getUser(), parent, PermissionType.UPLOAD), "无权权限复制文件");
				}
				Assert.isTrue(!parent.getId().equals(document.getId()), "不能复制到原文件夹中");
			}

			Document newDocument = new Document();
			Document originalParent = null;
			Integer originalMount = document.getMount();
			String originalPath = document.getPath();
			String originalFullName = document.getFullName();
			BeanUtils.copyProperties(document, newDocument);

			//获取原父文件夹名称
			if (document.getParent() != -1) {
				originalParent = this.findById(document.getParent());
			}

			newDocument.setId(null);
			if (DocumentType.isFile(newDocument.getType())) {
				newDocument.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			if (parent != null) {
				newDocument.setParent(parent.getId());
				newDocument.setMount(parent.getMount());
			} else {
				newDocument.setParent(-1);
				newDocument.setMount(request.getMount());
			}

			newDocument.setCreateDate(new Date());
			newDocument.setCreatedBy(request.getUser().getId());
			newDocument.setLocked(false);
			newDocument.setLockedBy(null);
			newDocument.setLockDate(null);
			newDocument.setUpdateDate(new Date());
			newDocument.setUpdatedBy(request.getUser().getId());
			this.setUniqueName(newDocument);
			this.syncByParent(newDocument, parent);

			this.create(newDocument);
			if (DocumentType.isDirectory(document.getType())) {
				//复制文件夹内容的内容到新的文件夹中
				//copyChildren(document, newDocument);
				batchCopyChildren(newDocument, originalPath, originalFullName);
			} else {
				storage.copy(document.getCode(), newDocument.getCode());
			}

			CopyEvent unlockEvent = new CopyEvent(document, originalParent, parent, originalMount, document.getMount(), request.getUser().getId());
			applicationEventPublisher.publishEvent(unlockEvent);

		}
	}

	/**
	 * 批量复制文件
	 *
	 * @param target           新文件夹
	 * @param originalPath     原路径
	 * @param originalFullName 原全名
	 */
	private void batchCopyChildren(Document target, String originalPath, String originalFullName) {
		List<Document> documents = this.documentRepository.findByPath(originalPath);
		Map<String, String> codeMapping = new HashMap<>(documents.size());
		for (Document document : documents) {
			Document newDocument = new Document();
			BeanUtils.copyProperties(document, newDocument);

			newDocument.setId(null);
			if (DocumentType.isFile(newDocument.getType())) {
				newDocument.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			newDocument.setParent(target.getId());
			newDocument.setMount(target.getMount());

			newDocument.setCreateDate(new Date());
			newDocument.setCreatedBy(target.getCreatedBy());
			newDocument.setLocked(false);
			newDocument.setLockedBy(null);
			newDocument.setLockDate(null);
			newDocument.setUpdateDate(new Date());
			newDocument.setUpdatedBy(target.getUpdatedBy());

			newDocument.setPath(target.getPath() + document.getPath().substring(originalPath.length()));
			newDocument.setFullName(target.getFullName() + document.getFullName().substring(originalFullName.length()));

			//插入新文檔
			this.documentRepository.save(document);
			if (DocumentType.isFile(document.getType())) {
				codeMapping.put(document.getCode(), newDocument.getCode());
			}
		}

		//复制文件
		codeMapping.forEach((key, value) -> storage.copy(key, value));
	}


	/**
	 * 复制子内容
	 *
	 * @param source
	 * @param target
	 */
	private void copyChildren(Document source, Document target) {

		List<Document> children = this.documentRepository.findByParent(source.getId());
		for (Document child : children) {
			Document newDocument = new Document();
			BeanUtils.copyProperties(child, newDocument);
			//目标不为空
			newDocument.setId(null);
			newDocument.setMount(target.getMount());
			newDocument.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
			//根据上级节点设置路径和跟节点
			newDocument.setParent(target.getId());
			this.syncByParent(newDocument, target);
			this.create(newDocument);
			if (DocumentType.isDirectory(child.getType())) {
				//复制文件夹内容的内容到新的文件夹中
				copyChildren(child, newDocument);
			} else {
				storage.copy(child.getCode(), newDocument.getCode());
			}
		}
	}


	@Override
	public Document createFile(CreateRequest request) {
		Assert.isTrue(StringUtils.isNotEmpty(request.getName()), "名称不能为空");
		Assert.isTrue(request.getParent() != null || request.getMount() != null, "父目录不能为空");

		Document file = new Document();
		file.setName(request.getName());
		file.setMd5(request.getMd5());
		file.setContentType(request.getContentType());
		file.setSize(request.getSize());
		file.setParent(request.getParent());
		file.setMount(request.getMount());
		file.setExt(request.getExt());
		file.setCode(request.getCode());

		file.setType(DocumentType.FILE.getCode());
		file.setCreatedBy(request.getUser().getId());
		file.setUpdatedBy(request.getUser().getId());

		this.syncByParent(file);
		this.create(file);

		UploadEvent uploadEvent = new UploadEvent(file, request.getUser().getId());
		this.applicationEventPublisher.publishEvent(uploadEvent);

		return file;
	}

	@Override
	public Document createDirectory(MkdirRequest request) {

		Assert.isTrue(authorizeService.checkPermission(request.getUser(), request.getParent(), request.getMount(), PermissionType.CREATE), "无权限访问");
		Document document = new Document();
		document.setName(request.getName());
		document.setParent(request.getParent());
		document.setMount(request.getMount());
		document.setCreatedBy(request.getUser().getId());
		document.setUpdatedBy(request.getUser().getId());
		return this.createDirectory(document);
	}

	private Document createDirectory(Document document) {
		document.setType(DocumentType.DIRECTORY.getCode());
		Document parent = null;
		if (document.getParent() != null && document.getParent() != -1) {
			parent = this.findById(document.getParent());
		}
		this.syncByParent(document, parent);
		this.create(document);
		CreateEvent createEvent = new CreateEvent(document, parent != null ? parent.getName() : null, document.getCreatedBy());
		this.applicationEventPublisher.publishEvent(createEvent);
		//添加活动
		return document;
	}


	@Override
	public Document rename(RenameRequest request) {
		Document document = this.findById(request.getId());
		Assert.isTrue(!document.getLocked(), "已锁定的文件不能重命名");
		Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.EDIT), "无权限重命名文件");

		String originalName = document.getName();
		document.setName(request.getName());
		document.setUpdateDate(new Date());
		document.setUpdatedBy(request.getId());
		this.syncByParent(document);
		this.documentRepository.update(document);
		//更新子文件的路径
		if (DocumentType.isDirectory(document.getType())) {
			this.renameChildren(document);
		}
		RenameEvent renameEvent = new RenameEvent(document, originalName, request.getUser().getId());
		applicationEventPublisher.publishEvent(renameEvent);
		return document;
	}

	/**
	 * 重命名子文件
	 *
	 * @param document
	 */
	private void renameChildren(Document document) {
		//TODO 批量处理
		List<Document> children = this.documentRepository.findByParent(document.getId());
		for (Document child : children) {
			child.setFullName(document.getFullName() + "/" + child.getName());
			this.documentRepository.update(child);
			if (DocumentType.isDirectory(child.getType())) {
				this.renameChildren(child);
			}
		}
	}

	@Override
	public void remove(RemoveRequest request) {
		for (Integer id : request.getDocumentIds()) {
			Document document = this.findById(id);
			Assert.isTrue(!document.getLocked(), "已锁定的文件不能删除");
			Assert.isTrue(!document.getDeleted(), "已删除的文件不能删除");
			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.REMOVE), "无权限删除文件");
			this.documentRepository.remove(id, request.getUser().getId());
			RemoveEvent removeEvent = new RemoveEvent(document, request.getUser().getId());
			this.applicationEventPublisher.publishEvent(removeEvent);
		}
	}

	public boolean exists(String name, Integer parent, Integer mount, String type) {
		if (parent == null) {
			parent = -1;
		}
		return this.documentRepository.getDocument(name, parent, mount, type) != null;
	}

	public boolean exists(Document document) {
		return this.exists(document.getName(), document.getParent(), document.getMount(), document.getType());
	}

	private void setUniqueName(Document document) {
		String name = getUniqueName(document);
		document.setName(name);
	}

	private String getUniqueName(Document document) {
		return this.getUniqueName(document.getName(), document.getParent(), document.getMount(), document.getType());
	}


	/**
	 * 获取递增后的名称
	 *
	 * @param name
	 * @param parent
	 * @param mount
	 * @param type
	 * @return
	 */
	private String getUniqueName(String name, Integer parent, Integer mount, String type) {
		boolean exists = this.exists(name, parent, mount, type);
		int count = 0;
		while (exists) {
			count++;
			String newName;
			if (name.lastIndexOf(".") > 0) {
				String ext = name.substring(name.lastIndexOf("."), name.length());
				String simpleName = name.substring(0, name.lastIndexOf("."));
				newName = (simpleName + "(" + count + ")" + ext);
			} else {
				newName = (name + "(" + count + ")");
			}
			exists = this.exists(newName, parent, mount, type);
		}
		if (count > 0) {
			if (name.lastIndexOf(".") > 0) {
				String ext = name.substring(name.lastIndexOf("."), name.length());
				String simpleName = name.substring(0, name.lastIndexOf("."));
				return (simpleName + "(" + count + ")" + ext);
			} else {
				return (name + "(" + count + ")");
			}
		}
		return name;
	}


	@Override
	public void delete(DeleteRequest request) {
		for (Integer documentId : request.getDocumentIds()) {
			Document document = this.findById(documentId);
			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.REMOVE), "无权限彻底删除文件");
			this.delete(document);
			DeleteEvent deleteEvent = new DeleteEvent(document, request.getUser().getId());
			this.applicationEventPublisher.publishEvent(deleteEvent);

		}
	}

	private void delete(Document document) {

		Assert.isTrue(document.getDeleted(), "文件对象状态错误");

		documentRepository.delete(document.getId());

		if (DocumentType.isDirectory(document.getType())) {
			documentRepository.deleteByPath(document.getPath());
			this.authorizeService.deleteByDocumentId(document.getId());
			List<String> codes = this.documentRepository.findCodeByPath(document.getPath());
			codes.forEach(code -> storage.remove(code));
		} else {
			storage.remove(document.getCode());
		}
	}

	private void deleteChildren(Document document) {
		if (DocumentType.isDirectory(document.getType())) {
			return;
		}
		List<Document> children = this.documentRepository.findByParent(document.getId());
		for (Document child : children) {
			if (DocumentType.isDirectory(child.getType())) {
				this.deleteChildren(child);
				documentRepository.delete(child.getId());
			} else {
				storage.remove(child.getCode());
				documentRepository.delete(child.getId());
			}
		}
	}

	@Override
	public void revert(RevertRequest request) {
		for (Integer documentId : request.getDocumentIds()) {
			Document document = this.findById(documentId);
			Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.REMOVE), "无权限恢复文件");
			this.setUniqueName(document);
			this.documentRepository.revert(documentId, request.getUser().getId(), this.getUniqueName(document));
			RevertEvent removeEvent = new RevertEvent(document, request.getUser().getId());
			this.applicationEventPublisher.publishEvent(removeEvent);
		}
	}

	/**
	 * @param document
	 * @return
	 */
	private Document create(Document document) {
		Assert.isTrue(document.getMount() != null, "参数错误");
		this.setUniqueName(document);
		if (document.getParent() == null) {
			document.setParent(-1);
		}
		document.setCreateDate(new Date());
		document.setUpdateDate(new Date());
		document.setDeleted(false);
		document.setLocked(false);
		this.documentRepository.save(document);

		String path = StringUtils.isNotEmpty(document.getPath()) ? document.getPath() : "";
		path += document.getId() + "/";

		Document update = new Document();
		update.setId(document.getId());
		update.setPath(path);
		this.documentRepository.update(update);

		return document;
	}


	@Override
	public Document updateTags(UpdateTagRequest request) {
		Document document = this.findById(request.getId());
		Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.EDIT), "无权限修改文件标签");

		String originalTags = request.getTags();
		this.documentRepository.updateTags(request.getId(), request.getTags(), request.getUser().getId());

		document.setTags(request.getTags());
		SetTagEvent event = new SetTagEvent(document, originalTags, request.getUser().getId());
		this.applicationEventPublisher.publishEvent(event);
		return document;
	}

	@Override
	public Document updateDesc(UpdateDescRequest request) {
		Document document = this.findById(request.getId());
		Assert.isTrue(authorizeService.checkPermission(request.getUser(), document, PermissionType.EDIT), "无权限修改文件备注");

		String originalDescription = document.getDescription();

		Document update = new Document();
		update.setId(request.getId());
		update.setDescription(request.getDescription());
		update.setUpdatedBy(request.getUser().getId());
		update.setUpdateDate(new Date());
		document.setDescription(request.getDescription());
		this.documentRepository.update(update);

		UpdateDescEvent event = new UpdateDescEvent(document, originalDescription, request.getUser().getId());
		this.applicationEventPublisher.publishEvent(event);
		return document;
	}


	/**
	 * 交接子文档
	 *
	 * @param document
	 */
	private void transferChildren(Document document) {
		List<Document> children = this.documentRepository.findByParent(document.getId());
		for (Document child : children) {
			child.setFullName(document.getFullName() + "/" + child.getName());
			child.setMount(document.getMount());
			this.syncByParent(document);
			this.documentRepository.update(document);
			if (DocumentType.isDirectory(document.getType())) {
				this.transferChildren(document);
			}
		}
	}


	/**
	 * 与上级同步
	 *
	 * @param document
	 * @param parent
	 */
	public void syncByParent(Document document, Document parent) {
		String path = "";
		if (parent != null) {
			path = parent.getPath();
			document.setRoot(this.getRootByParent(parent));
			document.setFullName(parent.getFullName() + "/" + document.getName());
		} else {
			Mount mount = this.mountService.get(document.getMount());
			document.setFullName(mount.getName() + "/" + document.getName());
		}
		//修改的情况下，自身有ID
		if (document.getId() != null) {
			path += document.getId() + "/";
		}
		document.setPath(path);
	}

	/**
	 * 与上级同步
	 *
	 * @param document
	 */
	public void syncByParent(Document document) {
		if (document.getParent() != null && document.getParent() != -1) {
			Document parent = this.findById(document.getParent());
			this.syncByParent(document, parent);
		} else {
			this.syncByParent(document, null);
		}
	}

	/**
	 * 获取根节点
	 *
	 * @param parent
	 * @return
	 */
	private Integer getRootByParent(Document parent) {
		return parent.getRoot() != null ? parent.getRoot() : parent.getId();
	}

	@Override
	public void setAuthorize(Integer documentId, Integer userId) {
		//更新状态
		Document update = new Document();
		update.setId(documentId);
		update.setUpdateDate(new Date());
		update.setUpdatedBy(userId);
		update.setAccessibility(DocumentAccessibility.PRIVATE.getCode());
		this.documentRepository.update(update);

	}

	@Override
	public void clearAuthorize(Integer documentId, Integer userId) {
		//更新状态
		Document update = new Document();
		update.setId(documentId);
		update.setUpdateDate(new Date());
		update.setUpdatedBy(userId);
		update.setAccessibility(DocumentAccessibility.PUBLIC.getCode());
		this.documentRepository.update(update);

	}

	private Document findById(Integer id) {
		return this.documentRepository.get(id).orElseThrow(() -> new ServiceException("000", "文件不存在"));
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent.getClass().equals(AuthorizeAddEvent.class)) {
			AuthorizeAddEvent authorizeAddEvent = (AuthorizeAddEvent) applicationEvent;
			this.setAuthorize(authorizeAddEvent.getDocument().getId(), authorizeAddEvent.getUserId());
		}
		if (applicationEvent.getClass().equals(AuthorizeRemoveEvent.class)) {
			AuthorizeRemoveEvent authorizeRemoveEvent = (AuthorizeRemoveEvent) applicationEvent;
			if (CollectionUtils.isEmpty(this.authorizeService.findByDocument(authorizeRemoveEvent.getDocument().getId()))) {
				this.clearAuthorize(authorizeRemoveEvent.getDocument().getId(), authorizeRemoveEvent.getUserId());
			}
		}
	}
}
