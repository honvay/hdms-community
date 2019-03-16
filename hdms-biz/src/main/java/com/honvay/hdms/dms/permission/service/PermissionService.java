/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.permission.service;


import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.framework.support.service.BaseService;
import org.springframework.cache.annotation.CacheEvict;

public interface PermissionService extends BaseService<Permission, Integer> {


	@CacheEvict(value = "permissionCache", key = "#t.id")
	void delete(Permission t);
}
