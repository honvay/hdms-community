/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;

import java.io.*;
import java.util.regex.Pattern;

public class JocConverter {

	/**
	 * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice
	 *
	 * @param sourceFile 源文件,绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,
	 *                   .docx, .xls, .xlsx, .ppt, .pptx等.
	 * @param destFile   目标文件.绝对路径.
	 * @throws OfficeException
	 */
	public static void word2pdf(String inputFilePath) throws OfficeException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		String outputFilePath = null;
		if (inputFilePath.endsWith(".doc")) {
			outputFilePath = inputFilePath.replaceAll(".doc", ".pdf");
		} else {
			outputFilePath = inputFilePath.replaceAll("docx", ".pdf");
		}
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
		}

		officeManager.stop();
	}

	public static void ppt2pdf(String inputFilePath) throws OfficeException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		String outputFilePath = null;
		if (inputFilePath.endsWith(".ppt")) {
			outputFilePath = inputFilePath.replaceAll(".ppt", ".pdf");
		} else {
			outputFilePath = inputFilePath.replaceAll(".pptx", ".pdf");
		}
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
		}

		officeManager.stop();
	}

	public static void visio2pdf(String inputFilePath) throws OfficeException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		String outputFilePath = null;
		if (inputFilePath.endsWith(".vsd")) {
			outputFilePath = inputFilePath.replaceAll(".vsd", ".pdf");
		} else {
			outputFilePath = inputFilePath.replaceAll(".vsdx", ".pdf");
		}
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
		}

		officeManager.stop();
	}

	public static void excel2pdf(String inputFilePath) throws OfficeException, FileNotFoundException, IOException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();
		String outputFilePath = null;
		if (inputFilePath.endsWith(".xls")) {
			outputFilePath = inputFilePath.replaceAll(".xls", ".pdf");
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(inputFilePath));
			int sheets = wb.getNumberOfSheets();
			for (int i = 0; i < sheets; i++) {
				HSSFSheet sheet = wb.getSheetAt(i); // 模版页
				HSSFPrintSetup setup = sheet.getPrintSetup();
				setup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
				setup.setFitWidth((short) 1);
				setup.setFitHeight((short) 0);
				sheet.setAutobreaks(true);
				sheet.setFitToPage(true);
			}
			FileOutputStream output = new FileOutputStream(inputFilePath);
			wb.write(output);
			wb.close();
			output.close();
		} else {
			outputFilePath = inputFilePath.replaceAll(".xlsx", ".pdf");
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(inputFilePath));
			int sheets = wb.getNumberOfSheets();
			for (int i = 0; i < sheets; i++) {
				XSSFSheet sheet = wb.getSheetAt(i); // 模版页
				XSSFPrintSetup setup = sheet.getPrintSetup();
				setup.setPaperSize(PaperSize.A4_PAPER);
				setup.setFitWidth((short) 1);
				setup.setFitHeight((short) 0);
				sheet.setAutobreaks(true);
				sheet.setFitToPage(true);
			}
			FileOutputStream output = new FileOutputStream(inputFilePath);
			wb.write(output);
			wb.close();
			output.close();
		}

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (outputFile.exists()) {
				outputFile.delete();
			}
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
		}
		officeManager.stop();
	}

	public static void convert(String path) throws Exception {
		if (path.endsWith(".doc") || path.endsWith(".docx")) {
			word2pdf(path);
		} else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
			excel2pdf(path);
		} else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
			ppt2pdf(path);
		} else if (path.endsWith(".vsd") || path.endsWith(".vsdx")) {
			visio2pdf(path);
		}
	}

	public static void txt2pdf(String inputFilePath) throws OfficeException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		String outputFilePath = null;
		outputFilePath = inputFilePath.replaceAll(".txt", ".pdf");
		File inputFile = new File(inputFilePath);
		if (inputFile.exists()) {// 找不到源文件, 则返回
			File outputFile = new File(outputFilePath);
			if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
				outputFile.getParentFile().mkdirs();
			}
			converter.convert(inputFile, outputFile);
		}

		officeManager.stop();
	}

	public static void multiple(String... inputFilePaths) throws OfficeException {

		DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();

		String officeHome = getOfficeHome();
		builder.setOfficeHome(officeHome);

		OfficeManager officeManager = builder.build();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		for (String inputFilePath : inputFilePaths) {
			String outputFilePath = null;
			if (inputFilePath.endsWith(".doc")) {
				outputFilePath = inputFilePath.replaceAll(".doc", ".pdf");
			} else {
				outputFilePath = inputFilePath.replaceAll("docx", ".pdf");
			}
			File inputFile = new File(inputFilePath);
			if (inputFile.exists()) {// 找不到源文件, 则返回
				File outputFile = new File(outputFilePath);
				if (!outputFile.getParentFile().exists()) { // 假如目标路径不存在, 则新建该路径
					outputFile.getParentFile().mkdirs();
				}
				System.out.println("开始转换:" + inputFilePath);
				converter.convert(inputFile, outputFile);
				System.out.println("转换完成:" + inputFilePath);
			}
		}
		officeManager.stop();
	}

	public static void main(String[] args) throws Exception {
		// JocConverter.excel2pdf("D:/基础设置-计量单位_功能设计说明_1.0.xls");
		//JocConverter.ppt2pdf("D:/数控MES下料MES项目工作总结.pptx");
		JocConverter.convert("D:/蓝凌EKP白皮书.docx");
		//JocConverter.txt2pdf("D:/新建文本文档.txt");
		//JocConverter.multiple("D:/条码硬件设备销售合同.doc","D:/HG-海格通信BPM项目-财务管控需求规格说明书V1.0.docx","D:/软件开发计划书20160629修改.doc");
	}

	public static String getOutputFilePath(String inputFilePath) {
		String outputFilePath = inputFilePath.replaceAll(".doc", ".pdf");
		return outputFilePath;
	}

	public static String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice.org3";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "D:/tools/OpenOffice 4";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "/Application/OpenOffice.org.app/Contents";
		}
		return null;
	}
}
