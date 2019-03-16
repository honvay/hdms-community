package com.honvay.hdms.dms.favorite.service;

import com.honvay.hdms.dms.favorite.dto.RemoveRequest;
import com.honvay.hdms.dms.favorite.entity.Favorite;
import com.honvay.hdms.framework.support.service.BaseService;

import java.util.List;

/**
 * @author LIQIU
 */
public interface FavoriteService extends BaseService<Favorite,Integer> {

	/**
	 * @param request
	 */
	void remove(RemoveRequest request);

	/**
	 * @param documentId
	 * @param userId
	 * @return
	 */
	Favorite get(Integer documentId, Integer userId);
}
