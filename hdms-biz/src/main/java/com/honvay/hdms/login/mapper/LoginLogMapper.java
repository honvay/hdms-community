package com.honvay.hdms.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.login.entity.LoginLog;
import com.honvay.hdms.login.model.LoginLogDto;
import com.honvay.hdms.login.model.LoginLogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/13
 **/
public interface LoginLogMapper extends BaseMapper<LoginLog> {

	/**
	 * @param query
	 * @return
	 */
	List<LoginLogDto> searchLoginLog(@Param("query") LoginLogQuery query);

	/**
	 * @param query
	 * @return
	 */
	Integer countLoginLog(@Param("query") LoginLogQuery query);
}
