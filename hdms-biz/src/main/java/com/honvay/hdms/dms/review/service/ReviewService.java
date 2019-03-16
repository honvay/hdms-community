package com.honvay.hdms.dms.review.service;

import com.honvay.hdms.dms.review.dto.ReviewDto;
import com.honvay.hdms.dms.review.entity.Review;
import com.honvay.hdms.framework.support.service.BaseService;

import java.util.List;

/**
 * @author LIQIU
 */
public interface ReviewService extends BaseService<Review, Integer> {

	/**
	 * @param documentId
	 * @return
	 */
	List<ReviewDto> findByDocumentId(Integer documentId);

	void delete(Review review);
}
