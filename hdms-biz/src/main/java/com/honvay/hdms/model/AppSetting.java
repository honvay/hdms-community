/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
