package com.honvay.hdms.dms.model.response;

import lombok.Data;

import java.util.Map;

/**
 * @author LIQIU
 * created on 2019/3/3
 **/
@Data
public class VerifyResult {

	private Boolean uploadedByMd5;

	private Map<String,Long> uploadedParts;

}
