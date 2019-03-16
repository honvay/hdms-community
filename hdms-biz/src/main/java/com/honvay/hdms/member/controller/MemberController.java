package com.honvay.hdms.member.controller;

import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.department.service.DepartmentService;
import com.honvay.hdms.member.model.MemberVo;
import com.honvay.hdms.user.model.UserVo;
import com.honvay.hdms.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author LIQIU
 * created on 2019/3/7
 **/
@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/list")
	public Result<MemberVo> list(Integer departmentId) {
		MemberVo memberVo = new MemberVo();
		memberVo.setUsers(this.userService.findByDepartmentId(departmentId)
				.stream().map(user -> {
					UserVo userVo = new UserVo();
					BeanUtils.copyProperties(user, userVo);
					return userVo;
				}).collect(Collectors.toList()));
		memberVo.setDepartments(departmentService.findByParent(departmentId));
		return Result.success(memberVo);
	}

	@RequestMapping("/search")
	public Result<MemberVo> search(String condition) {
		MemberVo memberVo = new MemberVo();
		memberVo.setUsers(this.userService.search(condition)
				.stream().map(user -> {
					UserVo userVo = new UserVo();
					BeanUtils.copyProperties(user, userVo);
					return userVo;
				}).collect(Collectors.toList()));
		memberVo.setDepartments(departmentService.search(condition));
		return Result.success(memberVo);
	}
}
