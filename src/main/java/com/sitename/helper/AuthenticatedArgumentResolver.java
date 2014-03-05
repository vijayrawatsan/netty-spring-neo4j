package com.sitename.helper;
import java.lang.annotation.Annotation;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sitename.service.UserService;

public class AuthenticatedArgumentResolver implements HandlerMethodArgumentResolver {

	protected static Logger logger = Logger.getLogger("AuthenticatedArgumentResolver");
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		 return parameter.hasParameterAnnotation(Authenticated.class);
	}

	@Override
	public Object resolveArgument(MethodParameter param,
			ModelAndViewContainer mavContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		Annotation[] paramAnns = param.getParameterAnnotations();
		for (Annotation paramAnn : paramAnns) {
			if (Authenticated.class.isInstance(paramAnn)) {
				HttpServletRequest httprequest = (HttpServletRequest) request
						.getNativeRequest();
				String authHeaderValue = httprequest.getHeader("Authorization");
				Object result = null;
				if(!StringUtils.isEmpty(authHeaderValue)) {
					result = userService.findBySignature(authHeaderValue);
				}
				if (result == null) {
				    //TODO- May be redirect somewhere good
					logger.info("Authentication failed : " +httprequest.getPathInfo());
					throw new AuthenticationException("Authentication failed : " +httprequest.getPathInfo());
				}
				return result;
			}
		}
		return WebArgumentResolver.UNRESOLVED;
	}
	
}