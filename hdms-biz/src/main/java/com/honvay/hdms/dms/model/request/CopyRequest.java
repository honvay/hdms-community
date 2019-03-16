package com.honvay.hdms.dms.model.request;

import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@Data
public class CopyRequest extends MountBaseRequest {

	private List<Integer> documentIds;

	private Integer parent;

}
