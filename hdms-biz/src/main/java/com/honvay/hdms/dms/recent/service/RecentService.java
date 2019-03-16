/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
	 *
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
