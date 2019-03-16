package com.honvay.hdms.framework.utils;

import org.apache.commons.lang.StringUtils;
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
