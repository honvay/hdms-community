/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.review.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.review.entity.Review;
import com.honvay.hdms.dms.review.service.ReviewService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/review")
public class ReviewController extends BaseController {

	@Autowired
	private ReviewService reviewService;

	@RequestMapping("/add")
	public Result<Review> add(@RequestBody @Valid Review review,
							  @AuthenticationPrincipal AuthenticatedUser user) {
		review.setReviewDate(new Date());
		review.setUserId(user.getId());
		reviewService.save(review);
		return this.success(review);
	}

	@RequestMapping("/delete/{id}")
	public Result<Object> delete(@PathVariable Integer id, @AuthenticationPrincipal AuthenticatedUser user) {
		Review review = this.reviewService.get(id);
		Assert.notNull(review, "评论不存在或已删除");
		Assert.isTrue(review.getUserId().equals(user.getId()), "只能删除自己的评论");
		reviewService.delete(review);
		return this.success();
	}
}
