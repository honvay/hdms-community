package com.honvay.hdms.setting.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LIQIU
 */
@Data
@TableName("hdms_setting")
public class Setting {

	@TableId
	private Integer id;

	private Integer loginFailLimit;

	private String defaultPassword;

	private Boolean showCaptcha;

	private Boolean showCaptchaOnError;

	private Integer captchaLength;

	private Integer minLengthOfPassword;

	private Integer passwordStrength;

	private Long maxUploadFileSize;

	private Long multiPartSize;

	private String includeExtensions;

	private String includeContentTypes;

	private String excludeContentTypes;

	private String excludeExtensions;

	private Boolean enableWatermark;

	private String watermarkProperties;
}
