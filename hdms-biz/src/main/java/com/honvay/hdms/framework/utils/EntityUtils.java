package com.honvay.hdms.framework.utils;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

/**
 * @author LIQIU
 */
public class EntityUtils {

	public static void merge(Object entity,Object form){
		if(entity.getClass() != form.getClass()){
			return ;
		}
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(form);
		for (PropertyDescriptor propertyDescriptor : descriptors) {
			String property = propertyDescriptor.getName();
			if("class".equals(property)) {
				continue;
			}
			try {
				Object value = PropertyUtils.getProperty(form, propertyDescriptor.getName());
				if(value != null){
					PropertyUtils.setProperty(entity, property, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
