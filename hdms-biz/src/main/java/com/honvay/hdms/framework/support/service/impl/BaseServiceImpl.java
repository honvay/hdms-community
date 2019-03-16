/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.support.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.framework.support.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
public class BaseServiceImpl<T, PK extends Serializable>
		implements BaseService<T, PK> {

	@Autowired
	private BaseMapper<T> mapper;

	@Override
	public List<T> list() {
		return mapper.selectList(null);
	}

	@Override
	public T save(T entity) {
		mapper.insert(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		mapper.updateById(entity);
		return entity;
	}


	@Override
	public T get(PK id) {
		return this.mapper.selectById(id);
	}

	@Override
	public void delete(PK pk) {
		this.mapper.deleteById(pk);
	}
}
