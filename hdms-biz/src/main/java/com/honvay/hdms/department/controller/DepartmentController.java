package com.honvay.hdms.department.controller;

import com.honvay.hdms.department.model.DepartmentDto;
import com.honvay.hdms.department.model.DepartmentVo;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.department.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/list")
	public List<DepartmentVo> findByParent(Integer id) {
		return this.departmentService.findByParent(id).stream()
				.map(department -> {
					DepartmentVo departmentVo = new DepartmentVo();
					BeanUtils.copyProperties(department, departmentVo);
					return departmentVo;
				}).collect(Collectors.toList());
	}

	@RequestMapping("/search")
	public Result<List<Department>> search(String condition) {
		return this.success(this.departmentService.search(condition));
	}

	@RequestMapping("/save")
	public Result<String> save(@RequestBody @Valid DepartmentDto departmentDto) {
		this.departmentService.save(departmentDto);
		return Result.success();
	}

	@RequestMapping("/update")
	public Result<String> update(@RequestBody @Valid DepartmentDto departmentDto) {
		this.departmentService.update(departmentDto);
		return this.success();
	}

	@RequestMapping("/delete/{id}")
	public Result<String> delete(@PathVariable Integer id) {
		this.departmentService.delete(id);
		return this.success();
	}


	@RequestMapping("/get/{id}")
	public Result<DepartmentDto> get(@PathVariable Integer id) {
		return this.success(this.departmentService.getFullById(id));
	}
}
