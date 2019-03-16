package com.honvay.hdms.dms.document.service.impl;

import com.honvay.hdms.dms.document.service.DocumentReadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

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