package com.honvay.hdms.dms.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.model.dto.DocumentActivityDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
