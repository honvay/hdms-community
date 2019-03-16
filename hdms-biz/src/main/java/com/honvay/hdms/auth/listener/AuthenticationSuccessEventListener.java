/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.auth.listener;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.department.service.DepartmentService;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import com.honvay.hdms.dms.mount.service.MountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * created on 2018-11-19
 **/
@Component
@Slf4j
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private MountService mountService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
		authenticatedUser.setOrganization(departmentService.getRootDepartment());
		authenticatedUser.setDepartment(departmentService.get(authenticatedUser.getDepartmentId()));
		authenticatedUser.setAuthorizes(authorizeService.findByOwner(authenticatedUser));
		authenticatedUser.setUserMount(mountService.get(authenticatedUser.getMountId()));
		authenticatedUser.setOrganizationMount(mountService.get(authenticatedUser.getOrganization().getMountId()));
		if (log.isDebugEnabled()) {
			log.debug("Authentication success:" + authentication.getName() + " ," + AuthenticationSuccessEvent.class);
		}
	}
}
