package com.honvay.hdms.dms.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class MkdirRequest extends MountBaseRequest {

	@NotEmpty(message = "文件夹名称不能为空")
	private String name;

	private Integer parent;

}
