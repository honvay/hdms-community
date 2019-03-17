/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.web;

import com.honvay.hdms.framework.core.protocol.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author LIQIU
 * created on 2019/3/10
 **/
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

	private final ErrorProperties errorProperties;

	/**
	 * Create a new {@link BasicErrorController} instance.
	 *
	 * @param errorAttributes    the error attributes
	 * @param serverProperties
	 * @param errorViewResolvers error view resolvers
	 */
	@Autowired
	public ErrorController(ErrorAttributes errorAttributes,
						   ServerProperties serverProperties,
						   List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorViewResolvers);
		Assert.notNull(serverProperties, "ErrorProperties must not be null");
		this.errorProperties = serverProperties.getError();
	}

	@Override
	public String getErrorPath() {
		return this.errorProperties.getPath();
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request,
								  HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
				request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView != null) ? modelAndView : new ModelAndView("error/" + status.value(), model);
	}

	@RequestMapping
	public ResponseEntity<Result<String>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		Result<String> result = Result.fail((String) body.get("message"));
		return new ResponseEntity<>(result, status);
	}

	/**
	 * Determine if the stacktrace attribute should be included.
	 *
	 * @param request  the source request
	 * @param produces the media type produced (or {@code MediaType.ALL})
	 * @return if the stacktrace attribute should be included
	 */
	protected boolean isIncludeStackTrace(HttpServletRequest request,
										  MediaType produces) {
		ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
		if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}

	/**
	 * Provide access to the error properties.
	 *
	 * @return the error properties
	 */
	protected ErrorProperties getErrorProperties() {
		return this.errorProperties;
	}

}
