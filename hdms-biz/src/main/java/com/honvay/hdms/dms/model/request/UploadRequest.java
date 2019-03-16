package com.honvay.hdms.dms.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadRequest extends CreateRequest {

	private Integer chunk;

	private Integer chunks;
}
