/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.activity.mapper;

import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.document.service.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author LIQIU
 * created on 2019/3/13
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ActivityMapperTest {

	@Autowired
	private ActivityMapper activityMapper;

	@Test
	public void searchActivity() {
		ActivityQuery query = new ActivityQuery();
		query.setStart(0L);
		query.setEnd(20L);
		activityMapper.searchActivity(query);
	}
}