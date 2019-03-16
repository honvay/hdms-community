package com.honvay.hdms.setting.service;


import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.setting.entity.Setting;

/**
 * @author LIQIU
 */
public interface SettingService extends BaseService<Setting, Integer> {

	/**
	 * @return
	 */
	Setting get();
}
