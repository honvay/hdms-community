/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.user.controller;

import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.department.service.DepartmentService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.framework.utils.DateUtils;
import com.honvay.hdms.framework.utils.ServletUtils;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.enums.UserRole;
import com.honvay.hdms.user.service.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIQIU
 * created on 2019/3/12
 **/
@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('" + UserRole.ROLE_SYS_ADMIN + "')")
public class UserImportController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	/**
	 * @param excel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/import")
	public Result<Map<String, Object>> importExcel(MultipartFile excel) throws Exception {
		Assert.isTrue(excel.getContentType().contains("excel"), "上传的文件不是Excel文件");
		HSSFWorkbook workbook = new HSSFWorkbook(excel.getInputStream());
		Assert.isTrue(workbook.getNumberOfSheets() > 0, "没有找到数据");
		HSSFSheet sheet = workbook.getSheetAt(0);
		//创建结果列
		HSSFRow titleRow = sheet.getRow(0);
		int resultCellIndex = titleRow.getLastCellNum();
		titleRow.createCell(titleRow.getLastCellNum()).setCellValue("导入结果");
		int messageCellIndex = titleRow.getLastCellNum();
		titleRow.createCell(titleRow.getLastCellNum()).setCellValue("失败原因");

		HSSFCellStyle successCellStyle = workbook.createCellStyle();
		successCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());

		HSSFCellStyle failureCellStyle = workbook.createCellStyle();
		failureCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());

		int successCount = 0;
		int failureCount = 0;

		int rows = sheet.getLastRowNum();
		for (int i = 0; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			boolean isDirty = false;
			User user = new User();
			HSSFCell resultCell = row.createCell(resultCellIndex);
			HSSFCell messageCell = row.createCell(messageCellIndex);
			String message = "";
			HSSFCell username = row.getCell(0);
			if (username == null || StringUtils.isEmpty(username.getStringCellValue())) {
				isDirty = true;
				message += "用户名为空\n";
			} else {
				user.setUsername(username.getStringCellValue());
				/*if (!this.userService.checkUsername(user.getUsername(), null)) {
					isDirty = true;
					message += "用户名已存在\n";
				}*/
			}

			HSSFCell name = row.getCell(1);
			if (name == null || StringUtils.isEmpty(name.getStringCellValue())) {
				isDirty = true;
				message += "姓名为空\n";
			} else {
				user.setName(name.getStringCellValue());
			}

			HSSFCell email = row.getCell(2);
			if (email == null || StringUtils.isEmpty(email.getStringCellValue())) {
				isDirty = true;
				message += "邮箱为空\n";
			} else {
				user.setEmail(email.getStringCellValue());
			}

			HSSFCell phoneNumber = row.getCell(3);
			if (phoneNumber == null || StringUtils.isEmpty(phoneNumber.getStringCellValue())) {
				isDirty = true;
				message += "手机号为空\n";
			} else {
				user.setPhoneNumber(phoneNumber.getStringCellValue());
			}

			HSSFCell departmentCell = row.getCell(4);
			if (departmentCell == null || StringUtils.isEmpty(departmentCell.getStringCellValue())) {
				isDirty = true;
				message += "所属部门为空\n";
			} else {
				Department department = this.departmentService.findByCode(departmentCell.getStringCellValue());
				if (department == null) {
					isDirty = true;
					message += String.format("编号为%s的部门不存在\n", departmentCell.getStringCellValue());
				}
				user.setDepartmentId(department.getId());
			}

			HSSFCell quota = row.getCell(4);
			if (departmentCell != null && StringUtils.isEmpty(quota.getStringCellValue())) {
				try {
					user.setQuota(Integer.parseInt(quota.getStringCellValue()));
				} catch (Exception e) {
					isDirty = true;
					message += "空间配额不是有效字段";
				}
			}
			if (isDirty) {
				resultCell.setCellStyle(failureCellStyle);
				resultCell.setCellValue("导入失败");
				messageCell.setCellValue(message);
				failureCount++;
			} else {
				try {
					this.userService.save(user);
					resultCell.setCellStyle(successCellStyle);
					resultCell.setCellValue("导入成功");
					successCount++;
				} catch (Exception e) {
					failureCount++;
					resultCell.setCellStyle(failureCellStyle);
					resultCell.setCellValue(e.getMessage());
				}
			}
		}
		String fileName = String.valueOf(System.currentTimeMillis());
		File file = new File(System.getProperty("java.io.tmpdir"), fileName);
		workbook.write(file);
		workbook.close();
		Map<String, Object> result = new HashMap<>(4);
		result.put("total", rows);
		result.put("success", successCount);
		result.put("fail", failureCount);
		result.put("file", fileName);
		return Result.success(result);
	}

	@RequestMapping("/downloadResult")
	public void downloadImportResult(String fileName, HttpServletResponse response) throws IOException {
		File file = new File(System.getProperty("java.io.tmpdir"), fileName);
		if (file.exists()) {
			long timeStamp = Long.parseLong(fileName);
			ServletUtils.setFileDownloadHeader(response, "用户导入结果-" + DateUtils.formatDate(new Date(timeStamp)) + ".xls");
			FileInputStream inputStream = new FileInputStream(file);
			IOUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();
			file.delete();
		}
	}
}
