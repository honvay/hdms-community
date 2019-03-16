package com.honvay.hdms.login.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honvay.hdms.framework.support.service.BaseService;
import com.honvay.hdms.login.entity.LoginLog;
import com.honvay.hdms.login.model.LoginLogDto;
import com.honvay.hdms.login.model.LoginLogQuery;

/**
 * @author LIQIU
 */
public interface LoginLogService extends BaseService<LoginLog, Integer> {


	Page<LoginLogDto> search(LoginLogQuery query);
}
