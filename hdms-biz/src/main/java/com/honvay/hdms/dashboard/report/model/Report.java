package com.honvay.hdms.dashboard.report.model;

import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/3/15
 **/
@Data
public class Report {

	private Long totalSize;

	private Long totalUser;

	private Long totalFile;

	private Long totalDepartment;

	private List<Space> spaces;
}
