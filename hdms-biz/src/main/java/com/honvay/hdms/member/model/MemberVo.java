package com.honvay.hdms.member.model;

import com.honvay.hdms.department.domain.Department;
import com.honvay.hdms.user.model.UserVo;
import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/7
 **/
@Data
public class MemberVo {

	private List<Department> departments;

	private List<UserVo> users;
}
