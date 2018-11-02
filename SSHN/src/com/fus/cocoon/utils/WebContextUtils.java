package com.fus.cocoon.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fus.cocoon.dict.SessionLoginUser;
import com.tgb.entity.User;
public final class WebContextUtils {
	public static HttpServletRequest getRequest() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
			return request;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static String getRequestParams(HttpServletRequest request) {
		String requestParams = "";
		Map<String,Object> requestParamMap = request.getParameterMap();
		if ((requestParamMap != null) && (requestParamMap.size() > 0)) {
			for (String param : requestParamMap.keySet()) {
				Object obj = requestParamMap.get(param);
				if ((obj instanceof String[])) {
					String valueList = "";
					String[] values = (String[]) obj;
					for (String value : values) {
						valueList = valueList + value + ",";
					}
					if (StringUtils.isNotBlank(valueList)) {
						valueList = valueList.substring(0,valueList.length() - 1);
						requestParams = requestParams + param + "=" + valueList	+ "&";
					}
				}
			}
			requestParams = StringUtils.isNotBlank(requestParams) ? requestParams.substring(0, requestParams.length() - 1): requestParams;
		}
		return requestParams;
	}

	public static User getLoginUser() {
		HttpSession session = getSession();
		if (session != null) {
			return (User) session.getAttribute(SessionLoginUser.SESSION_LOGIN_USER.name());
		}
		return null;
	}
	public static HttpSession getSession() {
		HttpServletRequest req = getRequest();
		if (req != null) {
			return req.getSession();
		}
		return null;
	}

	public static String getLoginUserName() {
		HttpSession session = getSession();
		if (session != null) {
			return (String) session.getAttribute(SessionLoginUser.SESSION_LOGIN_USER_NAME.name());
		}
		return null;
	}
	public static String getLoginRealName(){
		HttpSession session = getSession();
		if (session != null) {
			return (String) session.getAttribute(SessionLoginUser.SESSION_LOGIN_REAL_NAME.name());
		}
		return null;
	}
}
