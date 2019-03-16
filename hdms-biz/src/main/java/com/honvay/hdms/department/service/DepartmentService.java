package com.honvay.hdms.department.service;

import com.honvay.hdms.department.model.DepartmentDto;
import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.department.domain.Department;

import java.util.List;

/**
 * @author LIQIU
 */
public interface DepartmentService extends BaseService<Department, Integer> {

	/**
	 * @param parent
	 * @return
	 */
	List<Department> findByParent(Integer parent);

	/**
	 * @param id
	 * @return
	 */
	DepartmentDto getFullById(Integer id);

	/**
	 * @param condition
	 * @return
	 */
	List<Department> search(String condition);

	/**
	 * @return
	 */
	Department getRootDepartment();

	/**
	 * @param departmenDto
	 */
	void save(DepartmentDto departmenDto);

	/**
	 * @param departmentDto
	 * @return
	 */
	Department update(DepartmentDto departmentDto);

	Department findByCode(String code);
}
