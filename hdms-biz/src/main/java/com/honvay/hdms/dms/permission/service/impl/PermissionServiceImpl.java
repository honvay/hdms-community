package com.honvay.hdms.dms.permission.service.impl;

import com.honvay.hdms.dms.document.repository.DocumentRepository;
import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.dms.permission.service.PermissionService;
import com.honvay.hdms.framework.core.exception.ServiceException;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author LIQIU
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer> implements PermissionService {

	//private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private DocumentRepository documentRepository;

	@Override
	@Cacheable(value = "permissionCache")
	public Permission get(Integer pk) {
		return super.get(pk);
	}

	@Override
	@CacheEvict(value = "permissionCache", key = "#t.id")
	public void delete(Permission t) {
		if (!documentRepository.findByPermission(t.getId()).isEmpty()) {
			throw new ServiceException("000", "无法删除该权限，改权限已被使用");
		}
		super.delete(t.getId());
	}

	@Override
	@CachePut(value = "permissionCache", key = "#t.id")
	public Permission update(Permission t) {
		return super.update(t);
	}

}
