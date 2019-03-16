package com.honvay.hdms.dashboard.report;

import com.honvay.hdms.dashboard.report.model.Report;
import com.honvay.hdms.dashboard.report.model.Space;
import com.honvay.hdms.department.mapper.DepartmentMapper;
import com.honvay.hdms.dms.document.mapper.DocumentMapper;
import com.honvay.hdms.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LIQIU
 * created on 2019/3/15
 **/
@Service
public class ReportService {

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	public Report getReport() {
		Report report = new Report();
		report.setTotalSize(documentMapper.getTotalSize());
		report.setTotalFile(documentMapper.getTotalFiles());
		report.setTotalUser(userMapper.getTotalUser());
		report.setTotalDepartment(departmentMapper.getTotalDepartment());
		report.setSpaces(Stream.of(File.listRoots()).map(file -> {
			Space space = new Space();
			space.setName(file.getPath());
			space.setUsed(file.getUsableSpace());
			space.setTotal(file.getTotalSpace());
			return space;
		}).collect(Collectors.toList()));
		return report;
	}

}
