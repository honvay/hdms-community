/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.user.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.storage.Storage;
import com.honvay.hdms.dms.storage.StorageDirectory;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.model.UpdatePasswordDto;
import com.honvay.hdms.user.model.UpdateProfileDto;
import com.honvay.hdms.user.model.UserVo;
import com.honvay.hdms.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private Storage storage;

	@RequestMapping(value = "/list")
	public Result<List<UserVo>> list(Integer departmentId) {
		return this.success(this.userService.findByDepartmentId(departmentId)
				.stream().map(user -> {
					UserVo userVo = new UserVo();
					BeanUtils.copyProperties(user, userVo);
					return userVo;
				}).collect(Collectors.toList()));
	}

	@RequestMapping("/search")
	public Result<List<User>> search(String condition) {
		return this.success(this.userService.search(condition));
	}

	@RequestMapping("/updateProfile")
	public Result<String> updateProfile(@RequestBody @Valid UpdateProfileDto user, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		user.setId(authenticatedUser.getId());
		this.userService.updateProfile(user);
		authenticatedUser.setName(user.getName());
		authenticatedUser.setMode(user.getMode());
		authenticatedUser.setPhoneNumber(user.getPhoneNumber());
		authenticatedUser.setSortField(user.getSortField());
		authenticatedUser.setSortDesc(user.getSortDesc());
		return this.success();
	}

	@RequestMapping("/upload/avatar")
	public Result<String> upload(MultipartFile avatar, @AuthenticationPrincipal AuthenticatedUser user) throws IOException {
		storage.store(StorageDirectory.AVATAR, String.valueOf(user.getId()), avatar.getInputStream(), null);
		String avatarPath = "/resource/" + StorageDirectory.AVATAR + "/" + user.getId();
		if (!avatarPath.equals(user.getAvatar())) {
			this.userService.updateAvatar(user.getId(), avatarPath);
		}
		user.setAvatar(avatarPath);
		return this.success();
	}

	@RequestMapping(value = "/change/password", method = RequestMethod.POST)
	public Result<String> changePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto,
										 @AuthenticationPrincipal AuthenticatedUser user) {
		updatePasswordDto.setId(user.getId());
		this.userService.updatePassword(updatePasswordDto);
		user.setChangePassword(false);
		return this.success();
	}

}
