package com.honvay.hdms.model;

import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/15
 **/
@Data
public class AppSetting {

	private List<String> includeExtensions;

	private List<String> excludeExtensions;

	private List<String> includeContentTypes;

	private List<String> excludeContentTypes;

	private Long multiPartSize;

	private Integer minLengthOfPassword;

	private Integer passwordStrength;

	private Long maxUploadFileSize;
}
