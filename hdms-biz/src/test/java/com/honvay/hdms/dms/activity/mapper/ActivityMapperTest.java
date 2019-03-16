package com.honvay.hdms.dms.activity.mapper;

import com.honvay.hdms.dms.activity.model.ActivityQuery;
import com.honvay.hdms.dms.document.service.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

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