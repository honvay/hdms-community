package com.honvay.hdms.model;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dms.mount.entity.Mount;
import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.notice.entity.Notice;
import com.honvay.hdms.user.model.UserSpace;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author LIQIU
 * created on 2019/2/24
 **/
@Data
public class Environment {

	private Map<String, Mount> mounts;

	private List<Permission> permissions;

	private AuthenticatedUser user;

	private UserSpace space;

	private AppSetting appSetting;

	private Notice notice;

}
