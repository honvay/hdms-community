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
