package com.fus.cocoon.base.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import com.fus.cocoon.dao.Model;
import com.opensymphony.xwork2.ActionSupport;
public  class BaseAction<T extends Model> extends ActionSupport{
	private static final long serialVersionUID = -480397177678080388L;
	 
	public HttpServletResponse getResponse() {
		HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
		return resp;
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		return request;
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public void putObjectIntoRequest(String key, Object value) {
		getRequest().setAttribute(key, value);
	}

	public void putObjectIntoSession(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}

	public Object getObjectFromRequest(String key) {
		return getRequest().getAttribute(key);
	}

	public void findReportInfo(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}

	public Object getObjectFromRequestParameter(String key) {
		return getRequest().getParameter(key);
	}

	public Object getObjectFromSession(String key) {
		return getRequest().getSession().getAttribute(key);
	}

	public Object getObjectFromApplication(String key) {
		return getRequest().getSession().getServletContext().getAttribute(key);
	}

	public void putObjectIntoApplication(String key, Object value) {
		getRequest().getSession().getServletContext().setAttribute(key, value);
	}
}
