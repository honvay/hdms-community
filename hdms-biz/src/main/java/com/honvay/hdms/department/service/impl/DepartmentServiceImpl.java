package com.honvay.hdms.department.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.honvay.hdms.department.model.DepartmentDto;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.department.mapper.DepartmentMapper;
import com.honvay.hdms.department.service.DepartmentService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LIQIU
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Integer> implements DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public List<Department> findByParent(Integer parent) {
		if (parent == null) {
			parent = -1;
		}
		LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Department::getParent, parent)
				.orderByAsc(Department::getOrderNo);
		return this.departmentMapper.selectList(wrapper);
	}

	@Override
	public DepartmentDto getFullById(Integer id) {
		return this.departmentMapper.getFullById(id);
	}

	@Override
	public List<Department> search(String condition) {
		LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
		wrapper.like(Department::getName, condition).or()
				.like(Department::getFullPinyin, condition).or()
				.like(Department::getShortPinyin, condition);
		return this.departmentMapper.selectList(wrapper);
	}


	@Override
	public Department getRootDepartment() {
		LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Department::getParent, -1);
		return this.departmentMapper.selectOne(wrapper);

	}

	@Override
	public void save(DepartmentDto departmentDto) {

		Assert.notNull(departmentDto.getParent(), "上级部门不能为空");

		Department department = new Department();
		department.setName(departmentDto.getName());
		department.setCode(departmentDto.getCode());
		department.setOrderNo(departmentDto.getOrderNo());
		department.setParent(departmentDto.getParent().getId());
		this.save(department);
	}

	@Override
	public Department save(Department t) {

		try {
			t.setFullPinyin(PinyinHelper.convertToPinyinString(t.getName(), "", PinyinFormat.WITHOUT_TONE));
			t.setShortPinyin(PinyinHelper.getShortPinyin(t.getName()));
		} catch (PinyinException e) {

		}

		super.save(t);
		Department parent = this.get(t.getParent());
		t.setPath(parent.getPath() + (t.getId() + ","));
		return super.update(t);
	}

	@Override
	public Department update(DepartmentDto departmentDto) {

		Department department = this.get(departmentDto.getId());
		department.setName(departmentDto.getName());

		try {
			department.setFullPinyin(PinyinHelper.convertToPinyinString(departmentDto.getName(), "", PinyinFormat.WITHOUT_TONE));
			department.setShortPinyin(PinyinHelper.getShortPinyin(departmentDto.getName()));
		} catch (PinyinException e) {
		}

		department.setCode(departmentDto.getCode());
		department.setOrderNo(departmentDto.getOrderNo());

		if (department.getParent() == -1 && departmentDto.getParent() != null) {
			throw new IllegalArgumentException("根部门不能设置上级部门");
		}

		if (department.getParent() != -1 && department.getParent() == null) {
			throw new IllegalArgumentException("上级不能为空");
		}
		if (department.getParent() != -1) {
			Integer parent = department.getParent();
			department.setParent(departmentDto.getParent().getId());
			if (!parent.equals(department.getParent())) {

				Department newParent = this.get(department.getParent());
				if (newParent.getPath().startsWith(department.getPath())) {
					throw new IllegalArgumentException("不能将上级部门设置为当前部门或者当前部门的下级部门");
				}

				department.setPath(newParent.getPath() + department.getId() + ",");
				LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
				wrapper.likeRight(Department::getPath, department.getPath());

				List<Department> children = this.departmentMapper.selectList(wrapper);
				for (Department child : children) {
					String oldPath = child.getPath();
					String path = oldPath.substring(0, department.getPath().length());
					child.setPath(department.getPath() + path);
					super.update(child);
				}
			}
		}

		this.departmentMapper.updateById(department);
		return department;

	}

	@Override
	public Department findByCode(String code) {
		LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
		wrapper.like(Department::getCode, code);
		return this.departmentMapper.selectOne(wrapper);
	}
}
