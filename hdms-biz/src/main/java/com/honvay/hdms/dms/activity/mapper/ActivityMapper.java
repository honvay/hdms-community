/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.model.dto.DocumentActivityDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
public interface ActivityMapper extends BaseMapper<Activity> {

	/**
	 * @param path
	 * @return
	 */
	List<Activity> findDirectoryActivityByPath(@Param("path") String path);

	/**
	 * @param documentId
	 * @return
	 */
	List<Activity> findDocumentActivityById(@Param("documentId") Integer documentId);

	/**
	 * @param query
	 * @return
	 */
	List<DocumentActivityDto> searchActivity(@Param("query") ActivityQuery query);

	/**
	 * @param query
	 * @return
	 */
	Integer countActivity(@Param("query") ActivityQuery query);


}
