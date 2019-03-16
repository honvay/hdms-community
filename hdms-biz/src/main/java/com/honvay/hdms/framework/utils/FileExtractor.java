/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileExtractor {

	public static final List<String> supportExtension;

	static {
		//OFFICE预览服务器
		supportExtension = Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx");
	}


	public static String extract(String name, InputStream input) throws Exception {
		if (name.endsWith(".pdf")) {
			return extractPdf(input);
		} else if (name.endsWith(".doc") || name.endsWith(".docx")) {
			return extractWord(name, input);
		} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
			return extractExcel(name, input);
		} else if (name.endsWith(".ppt") || name.endsWith(".pptx")) {
			return extractPowerPoint(name, input);
		} else if (name.endsWith(".txt")) {
			return extractTxt(input);
		}
		return null;
	}

	public static String extractTxt(InputStream input) throws Exception {
		List<String> lines = IOUtils.readLines(input, "utf-8");
		String content = "";
		for (String line : lines) {
			content += line;
		}
		return content;
	}

	public static String extractPdf(InputStream input) {
		String content = "";
		int pageNum = 0;
		try {
			PdfReader reader = new PdfReader(input);
			// 获得页数
			pageNum = reader.getNumberOfPages();
			for (int i = 1; i < pageNum; i++) {
				content += PdfTextExtractor.getTextFromPage(reader, i);
			}
			reader.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String extractWord(String name, InputStream input) throws Exception {
		if (name.endsWith(".doc")) {
			WordExtractor extractor = new WordExtractor(input);
			String content = extractor.getText();
			extractor.close();
			return content;
		} else {
			XWPFDocument doc = new XWPFDocument(input);
			XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
			String content = extractor.getText();
			extractor.close();
			return content;
		}

	}

	public static String extractExcel(String name, InputStream input) throws Exception {
		if (name.endsWith(".xls")) {
			HSSFWorkbook workbook = new HSSFWorkbook(input);
			ExcelExtractor extractor = new ExcelExtractor(workbook);
			String content = extractor.getText();
			extractor.close();
			return content;
		} else {
			XSSFWorkbook workbook = new XSSFWorkbook(input);
			XSSFExcelExtractor extractor = new XSSFExcelExtractor(workbook);
			String content = extractor.getText();
			extractor.close();
			return content;
		}
	}

	public static String extractPowerPoint(String name, InputStream input) throws Exception {
		if (name.endsWith(".ppt")) {
			PowerPointExtractor extractor = new PowerPointExtractor(input);
			String content = extractor.getText();
			extractor.close();
			return content;
		} else {
			XMLSlideShow doc = new XMLSlideShow(input);
			XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(doc);
			String content = extractor.getText();
			extractor.close();
			return content;
		}

	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:/条码方案_汇报版V15.0927 - Liuy.pptx");
		String content = FileExtractor.extract(file.getName(), new FileInputStream(file));
		System.out.println(content);
	}
}
