/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.member.controller;

import com.honvay.hdms.department.service.DepartmentService;
import com.honvay.hdms.framework.core.protocol.Result;
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
