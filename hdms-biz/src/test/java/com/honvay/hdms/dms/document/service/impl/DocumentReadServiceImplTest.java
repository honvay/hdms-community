/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.service.impl;

import com.honvay.hdms.dms.document.service.DocumentReadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/3/8
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class DocumentReadServiceImplTest {

	@Autowired
	private DocumentReadService documentReadService;

	@Test
	public void search() {

		Set<String> documentIds = new HashSet<>(Arrays.asList("64", "32"));

		documentReadService.search("EKYC", documentIds, 1, 1, 1, 20);

	}
}