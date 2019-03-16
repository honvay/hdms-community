/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.notice.controller;

import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.framework.utils.EntityUtils;
import com.honvay.hdms.notice.entity.Notice;
import com.honvay.hdms.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

	@Autowired
	private NoticeService noticeService;

	@RequestMapping(method = RequestMethod.POST)
	public Object save(@RequestBody @Valid Notice notice) {
		Notice notice2 = noticeService.get();
		if (notice2 != null) {
			EntityUtils.merge(notice2, notice);
			noticeService.update(notice2);
		} else {
			this.noticeService.save(notice);
		}
		return success();
	}

	@RequestMapping(method = RequestMethod.GET)
	public Object get() {
		return success(noticeService.get());
	}
}
