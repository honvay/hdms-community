/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.authorize.authentication;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.authorize.authentication.annotation.Authentication;
import com.honvay.hdms.dms.authorize.authentication.annotation.AuthenticationMount;
import com.honvay.hdms.dms.authorize.authentication.annotation.AuthenticationSubject;
import com.honvay.hdms.dms.authorize.authentication.annotation.CompositionAuthentication;
import com.honvay.hdms.dms.authorize.service.AuthorizeService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Parameter;

@Service
public class AuthenticationInterceptor implements MethodInterceptor {

	@Autowired
	private AuthorizeService authorizeService;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Authentication authentication = AnnotationUtils.findAnnotation(invocation.getMethod(), Authentication.class);
		CompositionAuthentication compositionAuthentication = AnnotationUtils.findAnnotation(invocation.getMethod(), CompositionAuthentication.class);
		if (authentication != null) {
			//this.authenticate(authentication, invocation);
		} else if (compositionAuthentication != null) {
			Authentication[] authentications = compositionAuthentication.value();
			for (Authentication authentication2 : authentications) {
				//this.authenticate(authentication2, invocation);
			}
		}

		//TODO重写验证逻辑

		return invocation.proceed();
	}

	/**
	 * 认证权限
	 *
	 * @param authentication
	 * @param invocation
	 */
	public void authenticate(Authentication authentication, MethodInvocation invocation) {

		AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Parameter[] parameters = invocation.getMethod().getParameters();
		Integer subjectIndex = null;
		Integer mountIndex = null;
		int index = 0;
		for (Parameter parameter : parameters) {
			AuthenticationSubject id = parameter.getAnnotation(AuthenticationSubject.class);
			if (id != null && id.value().equals(authentication.group())) {
				subjectIndex = index;
			}
			AuthenticationMount mount = parameter.getAnnotation(AuthenticationMount.class);
			if (mount != null && mount.value().equals(authentication.group())) {
				mountIndex = index;
			}
			index++;
		}

		Assert.isTrue(subjectIndex != null || mountIndex != null, "权限校验参数名配置错误");

		Object[] arguments = invocation.getArguments();

		if (subjectIndex != null) {
			Assert.isTrue(subjectIndex < arguments.length, String.format("权限校验ID参数配置错误，参数下标：%d，实际参数长度：%d", subjectIndex, arguments.length));
		}
		if (mountIndex != null) {
			Assert.isTrue(mountIndex < arguments.length, String.format("权限校验挂载点参数配置错误，参数下标：%d，实际参数长度：%d", mountIndex, arguments.length));
		}


		String permission = authentication.value();
		String subject = subjectIndex != null ? String.valueOf(arguments[subjectIndex]) : null;
		String mount = mountIndex != null ? String.valueOf(arguments[mountIndex]) : null;

		subject = StringUtils.replace(subject, "null", "");
		mount = StringUtils.replace(mount, "null", "");
		/*if(subject == null || (subject != null && subject.equals("null"))) {
			if((mountValue != null &&  mountValue.equals(PrincipalUtils.getOrganization().getId()))){
				//企业文档下，普通用户只有查看权限
				if(permissionId.equals(Permission.VIEW) || permissionId.equals(Permission.DOWNLOAD)){
					//return ;
					//管理员有所有权限
				}else if(PrincipalUtils.isAdmin(PrincipalUtils.getUser())){
					return ;
				}
			}
			
			//个人文档目录下用户有所有权限
			if((mountValue != null &&  mountValue.equals(PrincipalUtils.getUser().getId()))){
				return ;
			}
		}*/

		if (authentication.multiple()) {
			String[] ids = subject.split(authentication.delimiter());
			for (String id : ids) {
				/*boolean passed = authorizeService.checkPermission(id, mount, permissionId, authenticatedUser);
				if (!passed) {
					throw new UnsupportedOperationException();
				}*/
			}
		} else {
			/*boolean passed = authorizeService.checkPermission(subject, mount, permissionId, authenticatedUser);
			if (!passed) {
				throw new UnsupportedOperationException();
			}*/
		}
	}
}
