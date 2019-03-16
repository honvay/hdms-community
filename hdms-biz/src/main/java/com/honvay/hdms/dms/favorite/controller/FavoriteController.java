package com.honvay.hdms.dms.favorite.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.favorite.dto.RemoveRequest;
import com.honvay.hdms.dms.favorite.entity.Favorite;
import com.honvay.hdms.dms.favorite.service.FavoriteService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController extends BaseController {

	@Autowired
	private FavoriteService favoriteService;

	@RequestMapping("/add")
	public Result<Integer> add(@RequestBody @Valid Favorite favorite,
							   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		favorite.setUserId(authenticatedUser.getId());
		this.favoriteService.save(favorite);
		return this.success(favorite.getId());
	}

	@RequestMapping("/remove")
	public Result<String> remove(@RequestBody @Valid RemoveRequest request,
								 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		request.setUserId(authenticatedUser.getId());
		this.favoriteService.remove(request);
		return this.success();
	}
}
