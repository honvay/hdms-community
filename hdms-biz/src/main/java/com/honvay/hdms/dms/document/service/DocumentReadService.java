/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.dms.model.dto.DocumentSimpleDto;

import java.util.List;
import java.util.Set;

/**
 * @author LIQIU
 */
public interface DocumentReadService {

	/**
	 * @param userId
	 * @return
	 */
	Long getUsedSpace(Integer userId);


	/**
	 * 获取已收藏的文件
	 *
	 * @param userId
	 * @return
	 */
	List<DocumentFullDto> findFavorites(Integer userId);

	/**
	 * 获取最近打开的文件
	 *
	 * @param userId
	 * @return
	 */
	List<DocumentFullDto> findRecent(Integer userId);


	/**
	 * 获取回收站文件
	 *
	 * @param paths
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	List<DocumentFullDto> findDeletedDocument(Set<String> paths, Integer userId, Integer departmentId);

	/**
	 * @param name
	 * @param paths
	 * @param userMount
	 * @param organizationMount
	 * @param current
	 * @param size
	 * @return
	 */
	Page<DocumentFullDto> search(String name, Set<String> paths, Integer userMount, Integer organizationMount, int current, int size);

	/**
	 * 获取文档
	 *
	 * @param id
	 * @return
	 */
	Document get(Integer id);


	/**
	 * 查询路径
	 *
	 * @param documentIds
	 * @return
	 */
	List<DocumentSimpleDto> findSimpleDocument(List<Integer> documentIds);

	/**
	 * 根据MD5值获取文件
	 *
	 * @param md5
	 * @return
	 */
	Document getByMd5(String md5);


	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	List<DocumentFullDto> findFullDocument(Integer parent, Integer mount);

	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	List<DirectoryNodeDto> findDirectory(Integer parent, Integer mount);

	/**
	 * @param id
	 * @param type
	 * @return
	 */
	DocumentFullDto getFullDocument(Integer id, String type);


	/**
	 * 根据上级查找文档
	 *
	 * @param parent
	 * @return
	 */
	List<Document> findByParent(Integer parent);

}
