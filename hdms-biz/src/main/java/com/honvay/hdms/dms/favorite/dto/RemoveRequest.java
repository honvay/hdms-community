package com.honvay.hdms.dms.favorite.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@Data
public class RemoveRequest {

	@NotEmpty
	private List<Integer> documentIds;

	private Integer userId;

}
