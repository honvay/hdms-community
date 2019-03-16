package com.honvay.hdms.dms.mount.service;

import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.framework.support.service.BaseService;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
public interface MountService extends BaseService<Mount,Integer> {
	Mount addUserMount();
}
