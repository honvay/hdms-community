/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
