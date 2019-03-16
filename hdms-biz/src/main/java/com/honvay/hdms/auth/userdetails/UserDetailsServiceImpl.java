package com.honvay.hdms.auth.userdetails;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.enums.UserStatus;
import com.honvay.hdms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * @date 21.5.18
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " can not be found");
		}

		//TODO 查询顶级部门
		return AuthenticatedUser.builder()
				.id(user.getId())
				.username(user.getUsername())
				.name(user.getName())
				.password(user.getPassword())
				.phoneNumber(user.getPhoneNumber())
				.changePassword(user.getChangePassword())
				.email(user.getEmail())
				.avatar(user.getAvatar())
				.quota(user.getQuota())
				.role(user.getRole())
				.mountId(user.getMountId())
				.departmentId(user.getDepartmentId())
				.locked(UserStatus.LOCKED.getValue().equals(user.getStatus()))
				.enable(UserStatus.ACTIVE.getValue().equals(user.getStatus()))
				.build();
	}

}
