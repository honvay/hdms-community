/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.department.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.department.model.DepartmentDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
public interface DepartmentMapper extends BaseMapper<Department> {

	List<DepartmentDto> findFullByParent(@Param("parent") Integer parentId);

	DepartmentDto getFullById(@Param("id") Integer id);

	@Select("select count(*) from hdms_department t")
	Long getTotalDepartment();

}
