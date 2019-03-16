/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.model.dto.DirectoryNodeDto;
import com.honvay.hdms.dms.model.dto.DocumentFullDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/2/22
 **/
public interface DocumentMapper extends BaseMapper<Document> {

	/**
	 * @return
	 */
	@Select("select sum(size) from hdms_document t where t.type = 'file'")
	Long getTotalSize();

	/**
	 * @return
	 */
	@Select("select count(*) from hdms_document t where t.type = 'file'")
	Long getTotalFiles();

	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	List<DocumentFullDto> findFullDocument(@Param("parent") Integer parent, @Param("mount") Integer mount);

	/**
	 * @param paths
	 * @param userMount
	 * @param organizationMount
	 * @return
	 */
	List<DocumentFullDto> findDeletedFullDocument(@Param("paths") Set<String> paths,
												  @Param("userMount") Integer userMount,
												  @Param("organizationMount") Integer organizationMount);

	/**
	 * 搜索文档
	 *
	 * @param name
	 * @param paths
	 * @param start
	 * @param end
	 * @param userMount
	 * @param organizationMount
	 * @return
	 */
	List<DocumentFullDto> searchFullDocument(@Param("name") String name,
											 @Param("paths") Set<String> paths,
											 @Param("start") int start,
											 @Param("end") int end,
											 @Param("userMount") Integer userMount,
											 @Param("organizationMount") Integer organizationMount);

	/**
	 * @param name
	 * @param paths
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	Integer countFullDocument(@Param("name") String name, @Param("paths") Set<String> paths,
							  @Param("userId") Integer userId, @Param("departmentId") Integer departmentId);


	/**
	 * @param parent
	 * @param mount
	 * @return
	 */
	List<DirectoryNodeDto> findDirectory(@Param("parent") Integer parent, @Param("mount") Integer mount);

	/**
	 * @param userId
	 * @return
	 */
	List<DocumentFullDto> findFavorites(@Param("userId") Integer userId);

	/**
	 * @param userId
	 * @return
	 */
	List<DocumentFullDto> findRecent(@Param("userId") Integer userId);


	DocumentFullDto getFullDocument(@Param("id") Integer id, @Param("type") String type);


	/**
	 * @param id
	 * @param tags
	 */
	void updateTags(@Param("id") Integer id, @Param("tags") String tags, @Param("userId") Integer userId);


	/**
	 * @param id
	 * @param userId
	 */
	void lock(@Param("id") Integer id, @Param("userId") Integer userId);

	/**
	 * @param id
	 * @param userId
	 */
	void unlock(@Param("id") Integer id, @Param("userId") Integer userId);

	/**
	 * @param id
	 * @param userId
	 */
	void remove(@Param("id") Integer id, @Param("userId") Integer userId);

	/**
	 * @param id
	 * @param userId
	 * @param name
	 */
	void revert(@Param("id") Integer id, @Param("userId") Integer userId, @Param("name") String name);

	/**
	 * @param id
	 * @param userId
	 * @param name
	 */
	void rename(@Param("id") Integer id, @Param("userId") Integer userId, @Param("name") String name);

	/**
	 * @param mount
	 * @param oldPath
	 * @param pathCondition
	 * @param newPath
	 * @param oldFullName
	 * @param newFullName
	 */
	void batchUpdateChildren(@Param("mount") Integer mount,
							 @Param("oldPath") String oldPath,
							 @Param("pathCondition") String pathCondition,
							 @Param("newPath") String newPath,
							 @Param("oldFullName") String oldFullName,
							 @Param("newFullName") String newFullName);

	/**
	 * @param path
	 * @return
	 */
	List<String> findCodeByPath(@Param("path") String path);

	Long sumSizeByUserId(@Param("userId") Integer userId);
}
