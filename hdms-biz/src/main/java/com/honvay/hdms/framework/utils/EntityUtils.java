/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.utils;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

/**
 * @author LIQIU
 */
public class EntityUtils {

	public static void merge(Object entity, Object form) {
		if (entity.getClass() != form.getClass()) {
			return;
		}
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(form);
		for (PropertyDescriptor propertyDescriptor : descriptors) {
			String property = propertyDescriptor.getName();
			if ("class".equals(property)) {
				continue;
			}
			try {
				Object value = PropertyUtils.getProperty(form, propertyDescriptor.getName());
				if (value != null) {
					PropertyUtils.setProperty(entity, property, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
