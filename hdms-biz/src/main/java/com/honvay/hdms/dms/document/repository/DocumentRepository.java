/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.enums.DocumentType;
import com.honvay.hdms.dms.document.mapper.DocumentMapper;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
@Repository
public class DocumentRepository {

	@Autowired
	private DocumentMapper documentMapper;

	public void save(Document document) {
		documentMapper.insert(document);
	}

	/**
	 * @param ids
	 * @return
	 */
	public List<Document> selectByIds(List<Integer> ids) {
		return this.documentMapper.selectBatchIds(ids);
	}

	/**
	 * @param id
	 * @return
	 */
	public Optional<Document> get(Integer id) {
		return Optional.ofNullable(documentMapper.selectById(id));
	}

	/**
	 * @param userId
	 * @return
	 */
	public List<DocumentFullDto> findFavorites(Integer userId) {
		return this.documentMapper.findFavorites(userId);
	}

	/**
	 * @param userId
	 * @return
	 */
	public List<DocumentFullDto> findRecent(Integer userId) {
		return this.documentMapper.findRecent(userId);
	}

	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	public List<DocumentFullDto> findFullDocument(Integer parent, Integer mount) {
		return this.documentMapper.findFullDocument(parent, mount);
	}

	/**
	 * @param paths
	 * @param userMount
	 * @param organizationMount
	 * @return
	 */
	public List<DocumentFullDto> findDeletedDocument(Set<String> paths,
													 Integer userMount,
													 Integer organizationMount) {
		return this.documentMapper.findDeletedFullDocument(paths, userMount, organizationMount);
	}

	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	public List<DirectoryNodeDto> findDirectory(Integer parent, Integer mount) {
		return this.documentMapper.findDirectory(parent, mount);
	}

	/**
	 * @param id
	 * @param type
	 * @return
	 */
	public DocumentFullDto getFullDocument(Integer id, String type) {
		return this.documentMapper.getFullDocument(id, type);
	}

	/**
	 * @param name
	 * @param paths
	 * @param userMount
	 * @param organizationMount
	 * @param current
	 * @param size
	 * @return
	 */
	public Page<DocumentFullDto> searchFullDocument(String name, Set<String> paths,
													Integer userMount, Integer organizationMount,
													int current, int size) {
		Page<DocumentFullDto> page = new Page<>();
		int start = (current - 1) * size;
		int end = current * size;
		name = "%" + name + "%";
		page.setRecords(this.documentMapper.searchFullDocument(name, paths, start, end, userMount, organizationMount));
		page.setTotal(this.documentMapper.countFullDocument(name, paths, userMount, organizationMount));
		return page;
	}


	/**
	 * @param parent
	 * @return
	 */
	public List<Document> findByParent(Integer parent) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Document::getParent, parent);
		return documentMapper.selectList(wrapper);
	}


	/**
	 * @param md5
	 * @return
	 */
	public List<Document> findByMd5(String md5) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Document::getMd5, md5);
		return documentMapper.selectList(wrapper);
	}

	/**
	 * @param parent
	 * @param mount
	 * @param name
	 * @return
	 */
	public Document getDirectoryByName(Integer parent, Integer mount, String name) {
		return this.getDocument(name, parent, mount, DocumentType.DIRECTORY.getCode());
	}

	/**
	 * @param name
	 * @param parent
	 * @param mount
	 * @param type
	 * @return
	 */
	public Document getDocument(String name, Integer parent, Integer mount, String type) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Document::getParent, parent)
				.eq(Document::getMount, mount)
				.eq(Document::getName, name)
				.eq(Document::getType, type)
				.eq(Document::getDeleted, false);
		return documentMapper.selectOne(wrapper);
	}


	/**
	 * @param id
	 * @param tags
	 * @param userId
	 */
	public void updateTags(Integer id, String tags, Integer userId) {
		documentMapper.updateTags(id, tags, userId);
	}


	/**
	 * @param document
	 */
	public void update(Document document) {
		this.documentMapper.updateById(document);
	}

	/**
	 * @param id
	 */
	public void delete(Integer id) {
		this.documentMapper.deleteById(id);
	}

	/**
	 * @param permission
	 * @return
	 */
	public List<Document> findByPermission(Integer permission) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Document::getPermission, permission);
		return documentMapper.selectList(wrapper);
	}

	/**
	 * @param path
	 */
	public void deleteByPath(String path) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.likeRight(Document::getPath, path);
		documentMapper.delete(wrapper);
	}

	/**
	 * @param path
	 * @return
	 */
	public List<String> findCodeByPath(String path) {
		return this.documentMapper.findCodeByPath(path + "%");
	}

	/**
	 * @param path
	 * @return
	 */
	public List<Document> findByPath(String path) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.likeRight(Document::getPath, path);
		return documentMapper.selectList(wrapper);
	}

	/**
	 * @param path
	 * @return
	 */
	public List<Document> findLockedByPath(String path) {
		LambdaQueryWrapper<Document> wrapper = Wrappers.lambdaQuery();
		wrapper.likeRight(Document::getPath, path)
				.eq(Document::getLocked, true);
		return documentMapper.selectList(wrapper);
	}

	/**
	 * @param id
	 * @param userId
	 */
	public void lock(Integer id, Integer userId) {
		this.documentMapper.lock(id, userId);
	}

	/**
	 * @param id
	 * @param userId
	 */
	public void unlock(Integer id, Integer userId) {
		this.documentMapper.unlock(id, userId);
	}

	/**
	 * @param id
	 * @param userId
	 */
	public void remove(Integer id, Integer userId) {
		this.documentMapper.remove(id, userId);
	}

	/**
	 * @param id
	 * @param userId
	 * @param name
	 */
	public void revert(Integer id, Integer userId, String name) {
		this.documentMapper.revert(id, userId, name);
	}


	/**
	 * @param mount
	 * @param oldPath
	 * @param newPath
	 * @param oldFullName
	 * @param newFullName
	 */
	public void batchUpdateChildren(Integer mount, String oldPath, String newPath,
									String oldFullName, String newFullName) {
		this.documentMapper.batchUpdateChildren(mount, oldPath, oldPath + "%", newPath, oldFullName, newFullName);
	}

	/**
	 * @param userId
	 * @return
	 */
	public Long sumSizeByUserId(Integer userId) {
		return this.documentMapper.sumSizeByUserId(userId);
	}


}
