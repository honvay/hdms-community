package com.honvay.hdms.framework.converter;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements WebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
                false));
    }
}
