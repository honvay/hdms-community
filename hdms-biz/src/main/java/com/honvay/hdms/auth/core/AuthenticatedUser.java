/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.dms.model.AuthorizedPermission;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author LIQIU
 * created on 2018-11-16
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUser implements UserDetails, CredentialsContainer {

	private Integer id;

	/**
	 * 用户名
	 */
	private String username;

	private String name;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String phoneNumber;

	/**
	 * 头像
	 */
	private String avatar;

	private Department department;

	private Integer departmentId;

	private Department organization;

	private Integer quota;

	private Long used;

	private Boolean changePassword;

	private String role;

	private String mode;

	private String sortField;

	private Boolean sortDesc;

	private Mount userMount;

	private Mount organizationMount;

	private Integer mountId;

	private List<AuthorizedPermission> authorizes;

	private List<SimpleGrantedAuthority> authorities;

	public boolean isAdmin() {
		return role.equals(User.ROLE_DOC_ADMIN) || role.equals(User.ROLE_SYS_ADMIN);
	}

	public boolean isSysAdmin() {
		return role.equals(User.ROLE_SYS_ADMIN);
	}

	public boolean isDocAdmin() {
		return role.equals(User.ROLE_DOC_ADMIN);
	}


	@JsonIgnore
	private boolean locked;

	@JsonIgnore
	private boolean enable;

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			authorities = Arrays.asList(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return enable;
	}


}
