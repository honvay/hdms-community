/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.setting.service.impl;

import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import com.honvay.hdms.setting.entity.Setting;
import com.honvay.hdms.setting.mapper.SettingMapper;
import com.honvay.hdms.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author LIQIU
 */
@Service
public class SettingServiceImpl extends BaseServiceImpl<Setting, Integer> implements SettingService {

	@Autowired
	private SettingMapper settingMapper;

	@Override
	@Cacheable(cacheNames = "setting_cache", key = "#root.targetClass")
	public Setting get() {
		return settingMapper.selectOne(null);
	}

	/**
	 * @param setting
	 * @return
	 */
	@Override
	@CachePut(cacheNames = "setting_cache", key = "#root.targetClass")
	public Setting save(Setting setting) {
		return super.save(setting);
	}
}
