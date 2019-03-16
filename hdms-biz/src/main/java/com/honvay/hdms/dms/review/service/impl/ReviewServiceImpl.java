/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.review.service.impl;

import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.document.service.DocumentReadService;
import com.honvay.hdms.dms.event.ReviewAddEvent;
import com.honvay.hdms.dms.event.ReviewRemoveEvent;
import com.honvay.hdms.dms.review.dto.ReviewDto;
import com.honvay.hdms.dms.review.entity.Review;
import com.honvay.hdms.dms.review.mapper.ReviewMapper;
import com.honvay.hdms.dms.review.service.ReviewService;
import com.honvay.hdms.framework.support.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LIQIU
 */
@Service
@Transactional
public class ReviewServiceImpl extends BaseServiceImpl<Review, Integer> implements ReviewService {

	@Autowired
	private ReviewMapper reviewMapper;

	@Autowired
	private DocumentReadService documentReadService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public List<ReviewDto> findByDocumentId(Integer documentId) {
		return this.reviewMapper.findByDocumentId(documentId);
	}

	@Override
	public void delete(Review review) {
		Document document = this.documentReadService.get(review.getDocumentId());
		ReviewRemoveEvent event = new ReviewRemoveEvent(document, review.getContent(), review.getUserId());
		applicationEventPublisher.publishEvent(event);
		super.delete(review.getId());
	}

	@Override
	public Review save(Review entity) {
		Document document = this.documentReadService.get(entity.getDocumentId());
		ReviewAddEvent event = new ReviewAddEvent(document, entity.getContent(), entity.getUserId());
		applicationEventPublisher.publishEvent(event);

		return super.save(entity);
	}
}
