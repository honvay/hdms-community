package com.honvay.hdms.dashboard;

import com.honvay.hdms.auth.core.AuthenticatedUser;
import com.honvay.hdms.dashboard.report.ReportService;
import com.honvay.hdms.dashboard.report.model.Report;
import com.honvay.hdms.framework.core.protocol.Result;
import com.honvay.hdms.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author LIQIU
 */
@Controller
public class DashboardController {

	@Autowired
	private ReportService reportService;


	@RequestMapping("/dashboard")
	@PreAuthorize("hasRole('" + User.ROLE_SYS_ADMIN + "')")
	public ModelAndView dashboard(ModelAndView modelAndView,
								  @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		modelAndView.addObject("user", authenticatedUser);
		modelAndView.setViewName("dashboard");
		return modelAndView;
	}

	@ResponseBody
	@GetMapping("/report")
	public Result<Report> report() {
		return Result.success(reportService.getReport());
	}
}
