package com.honvay.hdms.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.config.properties.StorageConfig;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.mount.service.MountService;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.framework.utils.WebUtils;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.service.SettingService;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.enums.UserStatus;
import com.honvay.hdms.user.mapper.UserMapper;
import com.honvay.hdms.user.model.*;
import com.honvay.hdms.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author LIQIU
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService, ApplicationListener<AbstractAuthenticationEvent> {

	@Autowired
	private StorageConfig storageConfig;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MountService mountService;

	@Autowired
	private SettingService settingService;

	@Override
	public List<User> findByDepartmentId(Integer departmentId) {
		LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(User::getDepartmentId, departmentId);
		return this.userMapper.selectList(wrapper);
	}

	private int checkPasswordStrength(String password) {
		int strength = 0;
		if (password.matches(".*\\d+.*")) {
			strength++;
		}
		if (password.matches(".*[a-z]+.*")) {
			strength++;
		}
		if (password.matches(".*[A-Z]+.*")) {
			strength++;
		}
		if (password.matches(".*\\W+.*")) {
			strength++;
		}
		return strength;

	}

	@Override
	public List<User> search(String condition) {
		LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
		wrapper.like(User::getUsername, condition).or()
				.like(User::getName, condition).or()
				.like(User::getFullPinyin, condition).or()
				.like(User::getShortPinyin, condition);
		return this.userMapper.selectList(wrapper);
	}

	@Override
	public User findByUsername(String username) {
		LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(User::getUsername, username);
		return this.userMapper.selectOne(wrapper);
	}

	private User findByEmail(String email) {
		LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(User::getEmail, email);
		return this.userMapper.selectOne(wrapper);
	}

	private User findByPhoneNumber(String phoneNumber) {
		LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(User::getPhoneNumber, phoneNumber);
		return this.userMapper.selectOne(wrapper);
	}

	@Override
	public void add(User user) {

		Setting setting = this.settingService.get();

		User existsUsernameUser = this.findByUsername(user.getUsername());
		Assert.isTrue(existsUsernameUser == null, "用户名已存在");

		User existsEmailUser = this.findByEmail(user.getEmail());
		Assert.isTrue(existsEmailUser == null, "邮箱已存在");


		User existsPhoneNumberUser = this.findByPhoneNumber(user.getUsername());
		Assert.isTrue(existsPhoneNumberUser == null, "手机号已存在");

		user.setPassword(passwordEncoder.encode(setting.getDefaultPassword()));

		if (StringUtils.isEmpty(user.getRole())) {
			user.setRole(User.ROLE_USER);
		}
		user.setQuota(storageConfig.getUserQuota());
		user.setStatus(UserStatus.ACTIVE.getValue());
		user.setAvatar("/asset/img/avatar.png");
		user.setChangePassword(true);
		user.setMode(User.MODE_LIST);
		user.setSortDesc(false);
		user.setSortField("name");

		Mount mount = mountService.addUserMount();
		user.setMountId(mount.getId());

		this.save(user);

	}

	@Override
	public UserUpdateVo getFullById(Integer id) {
		return this.userMapper.getFullById(id);
	}

	@Override
	public void resetPassword(Integer id) {
		Setting setting = this.settingService.get();
		User user = this.get(id);
		Assert.notNull(user, "用户不存在");
		user.setPassword(passwordEncoder.encode(setting.getDefaultPassword()));
		user.setChangePassword(true);
		super.update(user);
	}


	@Override
	public User save(User user) {
		try {
			user.setFullPinyin(PinyinHelper.convertToPinyinString(user.getName(), "", PinyinFormat.WITHOUT_TONE));
			user.setShortPinyin(PinyinHelper.getShortPinyin(user.getName()));
		} catch (PinyinException e) {
			e.printStackTrace();
		}
		super.save(user);
		return user;
	}

	@Override
	public void updateAvatar(Integer id, String avatar) {
		User user = new User();
		user.setId(id);
		user.setAvatar(avatar);
		super.update(user);
	}


	@Override
	public void updateProfile(UpdateProfileDto profileDto) {

		User existsEmailUser = this.findByEmail(profileDto.getEmail());
		Assert.isTrue(existsEmailUser == null || existsEmailUser.getId().equals(profileDto.getId()), "邮箱已存在");

		User existsPhoneNumberUser = this.findByPhoneNumber(profileDto.getPhoneNumber());
		Assert.isTrue(existsPhoneNumberUser == null || existsPhoneNumberUser.getId().equals(profileDto.getId()), "手机号已存在");

		User user = new User();
		user.setId(profileDto.getId());
		user.setName(profileDto.getName());
		user.setEmail(profileDto.getEmail());
		user.setPhoneNumber(profileDto.getPhoneNumber());
		user.setMode(profileDto.getMode());
		user.setSortField(profileDto.getSortField());
		user.setSortDesc(profileDto.getSortDesc());

		this.update(user);
	}

	@Override
	public void updateQuota(UpdateQuotaDto quota) {
		User user = new User();
		user.setId(quota.getId());
		user.setQuota(quota.getQuota());
		super.update(user);
	}

	@Override
	public void updatePassword(UpdatePasswordDto updatePasswordDto) {
		Setting setting = this.settingService.get();
		User user = this.get(updatePasswordDto.getId());
		Assert.notNull(user, "用户不存在");
		int strength = this.checkPasswordStrength(updatePasswordDto.getNewPassword());
		Assert.isTrue(setting.getMinLengthOfPassword() <= updatePasswordDto.getNewPassword().length(), "新密码长度不够");
		Assert.isTrue(strength >= setting.getPasswordStrength(), "新密码强度不足");
		user.setChangePassword(false);
		Assert.isTrue(passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword()), "旧密码不匹配");

		User update = new User();
		update.setId(user.getId());
		update.setChangePassword(false);
		update.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));

		super.update(update);
	}


	@Override
	public void update(UserUpdateDto userDto) {

		User existsUsernameUser = this.findByUsername(userDto.getUsername());
		Assert.isTrue(existsUsernameUser == null || existsUsernameUser.getId().equals(userDto.getId()), "用户名已存在");

		User existsEmailUser = this.findByEmail(userDto.getEmail());
		Assert.isTrue(existsEmailUser == null || existsEmailUser.getId().equals(userDto.getId()), "邮箱已存在");


		User existsPhoneNumberUser = this.findByPhoneNumber(userDto.getUsername());
		Assert.isTrue(existsPhoneNumberUser == null || existsPhoneNumberUser.getId().equals(userDto.getId()), "手机号已存在");

		User user = new User();
		user.setId(userDto.getId());
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		user.setDepartmentId(userDto.getDepartmentId());
		user.setPhoneNumber(userDto.getPhoneNumber());
		this.update(user);
	}

	@Override
	public User update(User user) {
		try {
			user.setFullPinyin(PinyinHelper.convertToPinyinString(user.getName(), "", PinyinFormat.WITHOUT_TONE));
			user.setShortPinyin(PinyinHelper.getShortPinyin(user.getName()));
		} catch (PinyinException e) {
			e.printStackTrace();
		}
		return super.update(user);
	}


	@Override
	public User lock(Integer id) {
		// TODO Auto-generated method stub
		User user = this.get(id);
		user.setStatus(UserStatus.LOCKED.getValue());
		this.update(user);
		return user;
	}

	@Override
	public User unlock(Integer id) {
		User user = this.get(id);
		user.setStatus(UserStatus.ACTIVE.getValue());
		this.update(user);
		return user;
	}

	@Override
	public User disable(Integer id) {
		User user = this.get(id);
		user.setStatus(UserStatus.DISABLED.getValue());
		this.update(user);
		return user;
	}

	@Override
	public User enable(Integer id) {
		User user = this.get(id);
		user.setStatus(UserStatus.ACTIVE.getValue());
		this.update(user);
		return user;
	}

	@Override
	public void transferAdmin(Integer source, Integer target) {
		User user = this.get(source);
		Assert.isTrue(User.ROLE_SYS_ADMIN.equals(user.getRole()), "该用户没有管理权限");
		User user1 = this.get(target);
		Assert.isTrue(!User.ROLE_SYS_ADMIN.equals(user1.getRole()), "该用户已经拥有管理员权限");
		user1.setRole(User.ROLE_SYS_ADMIN);
		this.update(user1);
		user.setRole(User.ROLE_USER);
		this.update(user);
	}

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		if (AuthenticationFailureBadCredentialsEvent.class.equals(event.getClass())) {
			if (event.getAuthentication().getClass().equals(UsernamePasswordAuthenticationToken.class)) {
				User user = this.findByUsername(event.getAuthentication().getName());
				//TODO 判断配置来自动锁定
				Setting setting = settingService.get();
				Integer loginFailCount = user.getLoginFailCount();
				if (loginFailCount == null) {
					loginFailCount = 0;
				}
				User update = new User();
				update.setId(user.getId());
				if (loginFailCount + 1 >= setting.getLoginFailLimit()) {
					update.setStatus(UserStatus.LOCKED.getValue());
					update.setLoginFailCount(0);
				} else {
					update.setLoginFailCount(loginFailCount + 1);
				}
				super.update(update);
			}
		} else if (AuthenticationSuccessEvent.class.equals(event.getClass())) {
			Authentication authentication = event.getAuthentication();
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
			User update = new User();
			update.setId(authenticatedUser.getId());
			update.setLoginFailCount(0);
			update.setLastLoginDate(new Date());
			update.setLastLoginIp(WebUtils.getClientIp());
			super.update(update);
		}
	}
}
