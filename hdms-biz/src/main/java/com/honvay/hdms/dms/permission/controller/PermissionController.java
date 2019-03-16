package com.honvay.hdms.dms.permission.controller;

import com.honvay.hdms.dms.permission.entity.Permission;
import com.honvay.hdms.dms.permission.service.PermissionService;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.framework.support.controller.BaseController;
import com.honvay.hdms.framework.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author LIQIU
 */
@RestController
@RequestMapping("/conf/permission")
@PreAuthorize("hasAnyRole('SYS_ADMIN')")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	@RequestMapping(method = RequestMethod.GET)
	public Result<List<Permission>> list() {
		return this.success(this.permissionService.list());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Result<Permission> get(@PathVariable Integer id) {
		return this.success(this.permissionService.get(id));
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> save(@RequestBody @Valid Permission permissionConfig) {
		this.permissionService.save(permissionConfig);
		return this.success();
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Result<String> update(@RequestBody @Valid Permission permissionConfig) {
		Permission permissionConfig2 = this.permissionService.get(permissionConfig.getId());
		EntityUtils.merge(permissionConfig2, permissionConfig);
		this.permissionService.update(permissionConfig2);
		return this.success();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public Result<String> delete(@PathVariable Integer id) {
		this.permissionService.delete(id);
		return this.success();
	}
}
