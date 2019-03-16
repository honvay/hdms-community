package com.honvay.hdms.framework.support.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
public interface BaseService<T, PK extends Serializable> {

	List<T> list();

	/**
	 * 保存实体对象，并触发事件
	 *
	 * @param entity
	 * @return
	 */
	T save(T entity);

	/**
	 * 更新实体对象，并触发事件
	 *
	 * @param entity
	 * @return
	 */
	T update(T entity);

	/**
	 * 通过主键获取对象
	 *
	 * @param id
	 * @return
	 */
	T get(PK id);

	/**
	 * 根据主键删除对象
	 *
	 * @param pk
	 */
	void delete(PK pk);

}
