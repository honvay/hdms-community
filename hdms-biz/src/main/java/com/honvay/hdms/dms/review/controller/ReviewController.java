package com.honvay.hdms.dms.review.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.review.entity.Review;
import com.honvay.hdms.dms.review.service.ReviewService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
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

	@RequestMapping("/delete")
	public Result<Object> delete(Integer id, @AuthenticationPrincipal AuthenticatedUser user) {
		Review review = this.reviewService.get(id);
		io.jsonwebtoken.lang.Assert.notNull(review, "评论不存在或已删除");
		Assert.isTrue(review.getUserId().equals(user.getId()), "只能删除自己的评论");
		reviewService.delete(review);
		return this.success();
	}
}
