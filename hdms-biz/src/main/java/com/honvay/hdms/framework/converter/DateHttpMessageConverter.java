package com.honvay.hdms.framework.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHttpMessageConverter extends AbstractHttpMessageConverter<Date>{
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private String pattern = "yyyy-MM-dd";
	
	private SimpleDateFormat format;

	public DateHttpMessageConverter() {
		super(MediaType.ALL);
		format = new SimpleDateFormat(pattern);
	}
	@Override
	protected boolean supports(Class<?> clazz) {
		return Date.class.equals(clazz);
	}

	@Override
	protected Date readInternal(Class<? extends Date> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		try {
			return format.parse(FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), DEFAULT_CHARSET)));
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void writeInternal(Date t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		FileCopyUtils.copy(format.format(t), new OutputStreamWriter(outputMessage.getBody(), DEFAULT_CHARSET));
	}

}
