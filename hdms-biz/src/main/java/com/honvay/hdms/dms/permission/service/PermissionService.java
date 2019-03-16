package com.honvay.hdms.dms.permission.service;


import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.framework.support.service.BaseService;
import org.springframework.cache.annotation.CacheEvict;

public interface PermissionService extends BaseService<Permission, Integer> {


	@CacheEvict(value="permissionCache",key = "#t.id")
	void delete(Permission t);
}
