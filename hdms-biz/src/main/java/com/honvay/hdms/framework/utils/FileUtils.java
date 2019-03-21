/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

public class FileUtils extends org.apache.commons.io.FileUtils {

	public static final String getExtension(String name) {
		if (StringUtils.isEmpty("name")) {
			return "";
		}
		if (name.indexOf(".") < 0) {
			return "";
		}
		return name.substring(name.lastIndexOf(".") + 1, name.length());
	}

	public static final String formatSize(Long size) {
		Long G = Long.valueOf(1024 * 1024 * 1024);
		Long M = Long.valueOf(1024 * 1024);
		Long K = 1025l;
		BigDecimal decimal = new BigDecimal(size);

		if (size > G) {
			return decimal.divide(new BigDecimal(G), 2, BigDecimal.ROUND_HALF_UP) + "GB";
		} else if (size > M) {
			return decimal.divide(new BigDecimal(M), 2, BigDecimal.ROUND_HALF_UP) + "MB";
		} else if (size > K) {
			return decimal.divide(new BigDecimal(K), 2, BigDecimal.ROUND_HALF_UP) + "KB";
		}
		return size + "B";
	}

	public static final void addPdfWatermaker(InputStream input, OutputStream output, String waterMarkName) throws Exception {
		PdfContentByte content = null;
		BaseFont base = null;
		Rectangle pageRect = null;
		PdfGState gs = new PdfGState();
		PdfReader reader = new PdfReader(input);
		PdfStamper pdfStamper = new PdfStamper(reader, output);
		try {
			// 设置字体
			base = BaseFont.createFont("C:/WINDOWS/Fonts/simhei.ttf", "Identity-H", true);// 使用系统字体
			//base = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (base == null || pdfStamper == null) {
				return;
			}
			// 设置透明度为0.4
			gs.setFillOpacity(0.6f);
			gs.setStrokeOpacity(0.6f);
			int toPage = pdfStamper.getReader().getNumberOfPages();
			for (int i = 1; i <= toPage; i++) {
				pageRect = pdfStamper.getReader().getPageSizeWithRotation(i);
				// 计算水印X,Y坐标
				float x = pageRect.getWidth() / 2;
				float y = pageRect.getHeight() / 2;
				// 获得PDF最顶层
				content = pdfStamper.getOverContent(i);
				content.saveState();
				content.setGState(gs);
				content.beginText();
				content.setColorFill(BaseColor.GRAY);
				content.setFontAndSize(base, 15);
				// 水印文字成45度角倾斜
				String[] lines = waterMarkName.split("\r\n");
				for (int j = 0; j < lines.length; j++) {
					content.moveText(50 + j * 30, 100);
					content.showTextAligned(Element.ALIGN_LEFT, lines[j], 50 + j * 30, 100, 45);
					content.showTextAligned(Element.ALIGN_LEFT, lines[j], x + 50 + j * 30, 100, 45);

					content.showTextAligned(Element.ALIGN_LEFT, lines[j], 50 + j * 30, y, 45);
					content.showTextAligned(Element.ALIGN_LEFT, lines[j], x + 100 + j * 30, y, 45);

					content.showTextAligned(Element.ALIGN_LEFT, lines[j], 50 + j * 30, pageRect.getHeight() - 100, 45);
					content.showTextAligned(Element.ALIGN_LEFT, lines[j], x + 50 + j * 30, pageRect.getHeight() - 100, 45);
					//content.showTextAligned(Element.ALIGN_LEFT, lines[j], pageRect.getWidth() + j * 30, 0, 45);
				}
				/*content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, x, y + 200, 45);
				content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, x, y, 45);
				content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, x, y - 200, 45);*/
				content.endText();
			}
			pdfStamper.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			content = null;
			base = null;
			pageRect = null;
		}
	}

	public static void main(String[] args) {
		System.out.println(FileUtils.formatSize(129748176l));
	}
}
