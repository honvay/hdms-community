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
	@CachePut(cacheNames = "setting_cache",key = "#root.targetClass")
	public Setting save(Setting setting) {
		return super.save(setting);
	}
}
