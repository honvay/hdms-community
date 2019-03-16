package com.honvay.hdms.dms.recent.service;

import com.honvay.hdms.dms.recent.entity.Recent;
import com.honvay.hdms.framework.support.service.BaseService;

import java.util.List;

/**
 * @author LIQIU
 */
public interface RecentService extends BaseService<Recent, Integer> {

	/**
	 * 删除最近的文件记录
	 * @param documentIds
	 * @param userId
	 */
	void remove(List<Integer> documentIds, Integer userId);
	/**
	 * @param documentId
	 * @param userId
	 */
	void add(Integer documentId, Integer userId);

	/**
	 * @param userId
	 */
	void clear(Integer userId);


}
