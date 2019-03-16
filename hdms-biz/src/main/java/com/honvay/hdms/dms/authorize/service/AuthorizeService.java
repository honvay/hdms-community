/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.authorize.service;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.entity.Authorize;
import com.honvay.hdms.dms.authorize.model.AuthorizeVo;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.model.AuthorizedPermission;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import com.honvay.hdms.framework.support.service.BaseService;

import java.util.List;
import java.util.Set;

/**
 * @author LIQIU
 */
public interface AuthorizeService extends BaseService<Authorize, Integer> {

	/**
	 * @param user
	 * @param permission
	 * @return
	 */
	Set<String> findPathsByOwner(AuthenticatedUser user, String permission);

	/**
	 * @param documentId
	 * @return
	 */
	List<AuthorizeVo> findByDocumentId(Integer documentId);

	/**
	 * @param documentId
	 * @return
	 */
	List<Authorize> findByDocument(Integer documentId);

	/**
	 * @param id
	 * @param userId
	 */
	void delete(Integer id, Integer userId);

	/**
	 * @param documentId
	 */
	void deleteByDocumentId(Integer documentId);

	/**
	 * @param authorize
	 * @param permissionId
	 * @param userId
	 */
	void updatePermission(Authorize authorize, Integer permissionId, Integer userId);

	/**
	 * @param user
	 * @param documents
	 * @param permission
	 * @return
	 */
	List<DocumentFullDto> filterDocuments(AuthenticatedUser user, List<DocumentFullDto> documents, String permission);

	/**
	 * 过滤
	 *
	 * @param user
	 * @param directories
	 * @param permission
	 * @return
	 */
	List<DirectoryNodeDto> filterDirectories(AuthenticatedUser user, List<DirectoryNodeDto> directories, String permission);

	boolean checkPermission(AuthenticatedUser user, Document document, String permission);

	boolean checkPermission(AuthenticatedUser user, Integer documentId, Integer mountId, String permission);


	void add(Authorize authorize, Integer userId);

	List<AuthorizedPermission> findByOwner(AuthenticatedUser user);

	Set<String> findPermissionByDocumentIdAndOwner(AuthenticatedUser user, Document document);

	/**
	 * @param user
	 * @param path
	 * @param documentId
	 * @return
	 */
	Set<String> findPermissionByDocumentIdAndOwner(AuthenticatedUser user, String path, Integer documentId);

	/**
	 * @param source
	 * @param type
	 * @return
	 */
	List<Authorize> findAuthorizes(Integer source, Integer type);

	/**
	 * @param source
	 * @param target
	 * @param type
	 * @return
	 */
	Authorize getAuthorize(Integer source, Integer target, Integer type);

}
