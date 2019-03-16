package com.honvay.hdms.dms.authorize.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.dms.authorize.entity.Authorize;
import com.honvay.hdms.dms.authorize.model.AuthorizeVo;
import com.honvay.hdms.dms.model.dto.OwnerPermissionDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
public interface AuthorizeMapper extends BaseMapper<Authorize> {

	List<OwnerPermissionDto> findByOwner(@Param("userId") Integer userId, @Param("departmentIds") List<Integer> departmentIds);

	/**
	 * @param documentIds
	 * @return
	 */
	List<AuthorizeVo> findByDocumentId(@Param("documentIds") List<Integer> documentIds);

	/**
	 * @param documentIds
	 * @return
	 */
	List<OwnerPermissionDto> findByDocumentIdAndOwner(@Param("userId") Integer userId, @Param("departmentIds") List<Integer> departmentIds, @Param("documentIds") List<Integer> documentIds);

}
