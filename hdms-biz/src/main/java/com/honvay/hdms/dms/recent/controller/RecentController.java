package com.honvay.hdms.dms.recent.controller;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.recent.service.RecentService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/recent")
public class RecentController extends BaseController {

	@Autowired
	private RecentService recentService;

	@RequestMapping("/remove")
	public Result<String> remove(@RequestParam String documentId, @AuthenticationPrincipal AuthenticatedUser user) {
		List<Integer> documentIds = Stream.of(documentId.split(",")).map(Integer::valueOf).collect(Collectors.toList());
		this.recentService.remove(documentIds, user.getId());
		return this.success();
	}

	@RequestMapping("/clear")
	public Result<String> clear(@AuthenticationPrincipal AuthenticatedUser user) {
		this.recentService.clear(user.getId());
		return this.success();
	}
}
