/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
