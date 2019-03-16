package com.honvay.hdms.dms.mount.service.impl;

import com.honvay.hdms.dms.document.enums.MountType;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.mount.service.MountService;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LIQIU
 * created on 2019/3/9
 **/
@Service
@Transactional
public class MountServiceImpl extends BaseServiceImpl<Mount, Integer> implements MountService {

	@Override
	public Mount addUserMount() {
		Mount mount = new Mount();
		mount.setName("个人文档");
		mount.setType(MountType.MY.getValue());
		mount.setAlias(MountType.MY.getCode());
		this.save(mount);
		return mount;
	}

}
