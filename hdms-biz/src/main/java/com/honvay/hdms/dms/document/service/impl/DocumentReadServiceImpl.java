/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.repository.DocumentRepository;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.dms.model.dto.DocumentSimpleDto;
import com.honvay.hdms.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LIQIU
 */
@Service
public class DocumentReadServiceImpl implements DocumentReadService {

	@Autowired
	private DocumentRepository documentRepository;

	@Override
	public Long getUsedSpace(Integer userId) {
		return documentRepository.sumSizeByUserId(userId);
	}

	@Override
	public List<DocumentFullDto> findFavorites(Integer userId) {
		return documentRepository.findFavorites(userId);
	}

	@Override
	public List<DocumentFullDto> findRecent(Integer userId) {
		return documentRepository.findRecent(userId);
	}

	/**
	 * 获取可回收的文件
	 *
	 * @param paths
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	@Override
	public List<DocumentFullDto> findDeletedDocument(Set<String> paths, Integer userId, Integer departmentId) {
		//TODO 处理回收站权限
		return this.documentRepository.findDeletedDocument(paths.stream().map(path -> path + "%").collect(Collectors.toSet()), userId, departmentId);
	}

	@Override
	public Page<DocumentFullDto> search(String name, Set<String> paths, Integer userMount, Integer organizationMount, int current, int size) {
		return this.documentRepository.searchFullDocument(name, paths.stream().map(path -> path + "%").collect(Collectors.toSet()),
				userMount, organizationMount,
				current, size);
	}

	@Override
	public Document get(Integer id) {
		return this.findById(id);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentFullDto> findFullDocument(Integer parent, Integer mount) {
		return this.documentRepository.findFullDocument(parent != null ? parent : -1, mount);
	}

	@Override
	public List<DirectoryNodeDto> findDirectory(Integer parent, Integer mount) {
		return this.documentRepository.findDirectory(parent, mount);
	}


	private Document findById(Integer id) {
		return this.documentRepository.get(id).orElseThrow(() -> new ServiceException("000", "文件不存在或已删除"));
	}

	/**
	 * 根据ID获取文档全部信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public DocumentFullDto getFullDocument(Integer id, String type) {
		return this.documentRepository.getFullDocument(id, type);
	}


	@Override
	public List<Document> findByParent(Integer parent) {
		return this.documentRepository.findByParent(parent);
	}


	@Override
	public List<DocumentSimpleDto> findSimpleDocument(List<Integer> documentIds) {
		List<Document> documents = this.documentRepository.selectByIds(documentIds);
		return documents.stream().map(document -> {
			DocumentSimpleDto simpleDto = new DocumentSimpleDto();
			simpleDto.setId(document.getId());
			simpleDto.setName(document.getName());
			simpleDto.setAccessibility(document.getAccessibility());
			return simpleDto;
		}).sorted(Comparator.comparingInt(DocumentSimpleDto::getId))
				.collect(Collectors.toList());
	}

	@Override
	public Document getByMd5(String md5) {
		List<Document> documents = this.documentRepository.findByMd5(md5);
		if (documents != null && documents.size() > 0) {
			return documents.get(0);
		}
		return null;
	}

	public Document getDirectoryByName(String name, Integer parent, Integer mount) {
		return this.documentRepository.getDirectoryByName(parent, mount, name);
	}
}
