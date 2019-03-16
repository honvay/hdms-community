/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.user.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.enums.UserRole;
import com.honvay.hdms.user.model.UpdateQuotaDto;
import com.honvay.hdms.user.model.UserUpdateDto;
import com.honvay.hdms.user.model.UserUpdateVo;
import com.honvay.hdms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/12
 **/
@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('" + UserRole.ROLE_SYS_ADMIN + "')")
public class UserManageController extends BaseController {

	@Autowired
	private UserService userService;


	@RequestMapping("/findByDepartment")
	public Result<List<User>> findByDepartment(Integer departmentId) {
		return Result.success(this.userService.findByDepartmentId(departmentId));
	}

	@RequestMapping("/get/{id}")
	public Result<UserUpdateVo> get(@PathVariable Integer id) {
		return this.success(this.userService.getFullById(id));
	}

	@RequestMapping("/save")
	public Result<String> save(@RequestBody @Valid User user) {
		this.userService.add(user);
		return this.success();
	}

	@PostMapping("/lock/{id}")
	public Result<Integer> lock(@PathVariable Integer id) {
		return this.success(this.userService.lock(id).getStatus());
	}

	@PostMapping("/unlock/{id}")
	public Result<Integer> unlock(@PathVariable Integer id) {
		return this.success(this.userService.unlock(id).getStatus());
	}

	@PostMapping("/disable/{id}")
	public Result<Integer> disable(@PathVariable Integer id) {
		return this.success(this.userService.disable(id).getStatus());
	}

	@PostMapping("/enable/{id}")
	public Result<Integer> enable(@PathVariable Integer id) {
		return this.success(this.userService.enable(id).getStatus());
	}

	@PostMapping("/set/quota")
	public Result<String> setQuota(@RequestBody @Valid UpdateQuotaDto quota) {
		this.userService.updateQuota(quota);
		return this.success();
	}

	@PostMapping("/update")
	public Result<String> update(@RequestBody @Valid UserUpdateDto user) {
		this.userService.update(user);
		return this.success();
	}

	@RequestMapping("/admin/transfer")
	public Result<String> transferAdmin(Integer id, @AuthenticationPrincipal AuthenticatedUser user,
										HttpSession httpSession) {
		this.userService.transferAdmin(user.getId(), id);
		httpSession.invalidate();
		return this.success();
	}

	@PostMapping("/reset/{id}")
	public Result<String> reset(@PathVariable Integer id) {
		this.userService.resetPassword(id);
		return this.success();
	}
}
