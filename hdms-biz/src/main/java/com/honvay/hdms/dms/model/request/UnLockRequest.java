package com.honvay.hdms.dms.model.request;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/1
 **/
@Data
public class UnLockRequest {

	private AuthenticatedUser user;

	private List<Integer> documentIds;

}
