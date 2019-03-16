package com.honvay.hdms.user.service;

import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.model.*;

import java.util.List;

/**
 * @author LIQIU
 */
public interface UserService extends BaseService<User, Integer> {

	/**
	 * 添加用户
	 *
	 * @param user
	 */
	void add(User user);


	/**
	 * @param id
	 * @param avatar
	 */
	void updateAvatar(Integer id, String avatar);

	/**
	 * @param userDto
	 */
	void update(UserUpdateDto userDto);

	/**
	 * 锁定
	 *
	 * @param id
	 */
	User lock(Integer id);

	/**
	 * 解锁
	 *
	 * @param id
	 */
	User unlock(Integer id);

	/**
	 * 禁用
	 *
	 * @param id
	 */
	User disable(Integer id);

	/**
	 * 启用
	 *
	 * @param id
	 */
	User enable(Integer id);

	/**
	 * 重置密码
	 *
	 * @param id
	 */
	void resetPassword(Integer id);

	UserUpdateVo getFullById(Integer id);

	/**
	 * @param departmentId
	 * @return
	 */
	List<User> findByDepartmentId(Integer departmentId);

	/**
	 * @param condition
	 * @return
	 */
	List<User> search(String condition);

	/**
	 * @param source
	 * @param target
	 */
	void transferAdmin(Integer source, Integer target);

	/**
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * @param user
	 */
	void updateProfile(UpdateProfileDto user);

	void updateQuota(UpdateQuotaDto quota);

	/**
	 * @param updatePasswordDto
	 */
	void updatePassword(UpdatePasswordDto updatePasswordDto);
}
