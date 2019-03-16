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
