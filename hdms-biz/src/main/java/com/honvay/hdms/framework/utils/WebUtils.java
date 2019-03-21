/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 判断是否是异步的请求、AJAX请求
	 *
	 * @param request
	 * @return boolean
	 */
	public static boolean isAsynRequest(HttpServletRequest request) {

		String xRequestWith = request.getHeader("x-requested-with");
		if (StringUtils.isEmpty(xRequestWith)) {
			xRequestWith = request.getHeader("X-Requested-With");
		}
		return (StringUtils.isNotEmpty(xRequestWith) && xRequestWith.equalsIgnoreCase(
				"XMLHttpRequest"));
	}

	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		} else {
			return request.getRemoteAddr();
		}
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes.getRequest();
	}

	public static String getClientIp() {
		HttpServletRequest request = getRequest();
		return getClientIp(request);
	}


	public static String getUserAgent() {
		HttpServletRequest request = getRequest();
		return request.getHeader("User-Agent");
	}

	public static String getBasePath(HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath() + "/";
		return basePath;
	}

}
