package com.honvay.hdms.dms.document.service;

import com.honvay.hdms.dms.document.entity.Document;
import com.honvay.hdms.dms.model.request.*;

import javax.validation.Valid;

/**
 * @author LIQIU
 * created on 2019/2/28
 **/
public interface DocumentWriteService {


	/**
	 * 恢复文件
	 *
	 * @param request
	 */
	void revert(RevertRequest request);

	/**
	 * 修改文件名称
	 *
	 * @param request
	 * @return
	 */
	Document rename(RenameRequest request);

	/**
	 * 移动文档
	 *
	 * @param moveRequest
	 */
	void move(@Valid MoveRequest moveRequest);

	/**
	 * 创建文件夹
	 *
	 * @param request
	 * @return
	 */
	Document createDirectory(@Valid MkdirRequest request);

	/**
	 * 解锁文件
	 *
	 * @param request
	 */
	void unlock(UnLockRequest request);

	/**
	 * 复制文件
	 *
	 * @param request
	 */
	void copy(@Valid CopyRequest request);

	/**
	 * 创建文件
	 *
	 * @param request
	 * @return
	 */
	Document createFile(CreateRequest request);

	/**
	 * 删除文件
	 *
	 * @param request
	 */
	void remove(RemoveRequest request);


	/**
	 * 更新文档
	 *
	 * @param document
	 */
	void update(Document document);

	/**
	 * 移交文件
	 *
	 * @param source
	 * @param target
	 */
	void transfer(Integer source, Integer target);

	/**
	 * 锁定文件
	 *
	 * @param request
	 */
	void lock(LockRequest request);

	/**
	 * 设置标签
	 *
	 * @param request
	 * @return
	 */
	Document updateTags(UpdateTagRequest request);


	/**
	 * 删除文档
	 *
	 * @param request
	 */
	void delete(DeleteRequest request);

	/**
	 * 修改备注
	 * @param request
	 * @return
	 */
	Document updateDesc(UpdateDescRequest request);

	void setAuthorize(Integer documentId, Integer userId);

	void clearAuthorize(Integer documentId, Integer userId);
}
