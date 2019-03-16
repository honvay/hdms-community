/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.dms.document.service;

import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.model.request.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DocumentWriteServiceTest {

	@Autowired
	private DocumentWriteService documentWriteService;

	@Autowired
	private DocumentReadService documentReadService;

	private Integer userId = 1;

	private Integer mount = 1;

	private Integer mountType = 1;

	private Integer documentId;

	private Integer directoryId;

	@Test
	public void createDirectory() {
		this.createDirectory("测试文件夹");
	}

	@Test
	public void createFile() {
		this.createDirectory();
		CreateRequest createRequest = new CreateRequest();
		createRequest.setName("测试.docx");
		createRequest.setContentType("application/docx");
		createRequest.setSize(1000000L);
		createRequest.setParent(directoryId);
		createRequest.setMd5("1231415123");
		createRequest.setExt("docx");
		createRequest.setMaster(null);
		createRequest.setMount(mount);
		Document document = this.documentWriteService.createFile(createRequest);

		documentId = document.getId();

		Assert.assertEquals("文件目录2", this.documentReadService.get(document.getParent()).getName());
	}


	@Test
	public void copy() {

		this.createFile();

		Document directory2 = this.createDirectory("测试文件夹2");
		CopyRequest copyRequest1 = new CopyRequest();
		copyRequest1.setDocumentIds(Collections.singletonList(documentId));
		copyRequest1.setParent(directory2.getId());
		this.documentWriteService.copy(copyRequest1);
	}

	@Test
	public void move() {
		this.createFile();
		Document directory2 = this.createDirectory("测试文件夹2");
		MoveRequest request = new MoveRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		request.setParent(directory2.getId());
		this.documentWriteService.move(request);
	}

	private Document createDirectory(String name) {
		MkdirRequest request = new MkdirRequest();
		request.setName(name);
		request.setMount(mount);
		Document document = this.documentWriteService.createDirectory(request);
		this.directoryId = document.getId();
		return document;
	}


	@Test
	public void lock() {
		this.createFile();
		LockRequest request = new LockRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		this.documentWriteService.lock(request);
		Document document = this.documentReadService.get(documentId);
		Assert.assertEquals(true, document.getLocked());
	}


	@Test
	public void unlock() {
		this.lock();
		UnLockRequest request = new UnLockRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		this.documentWriteService.unlock(request);
		Document document = this.documentReadService.get(documentId);
		Assert.assertEquals(false, document.getLocked());
	}


	@Test
	public void remove() {
		this.createFile();
		RemoveRequest request = new RemoveRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		this.documentWriteService.remove(request);
		Document document = this.documentReadService.get(documentId);
		Assert.assertEquals(true, document.getDeleted());
	}

	@Test
	public void revert() {
		this.remove();
		RevertRequest request = new RevertRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		this.documentWriteService.revert(request);
		Document document = this.documentReadService.get(documentId);
		Assert.assertEquals(false, document.getDeleted());
	}


	@Test
	public void delete() {
		this.remove();
		DeleteRequest request = new DeleteRequest();
		request.setDocumentIds(Collections.singletonList(documentId));
		this.documentWriteService.delete(request);
	}

	@Test
	public void rename() {
		this.createFile();
		RenameRequest request = new RenameRequest();
		request.setId(documentId);
		request.setName("测试文件.xls");
		this.documentWriteService.rename(request);
		Document document = this.documentReadService.get(documentId);
		Assert.assertEquals("测试文件.xls", document.getName());
	}

	@Test
	public void update() {
	}

	@Test
	public void deleteVersion() {
	}

	@Test
	public void transfer() {
	}


	@Test
	public void setTags() {
	}

}