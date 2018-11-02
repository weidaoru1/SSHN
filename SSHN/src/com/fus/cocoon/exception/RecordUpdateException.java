package com.fus.cocoon.exception;

import org.apache.commons.lang3.StringUtils;

public class RecordUpdateException extends RuntimeException{
	private static final long serialVersionUID = 5779290680423769430L;
	private String message;
	public RecordUpdateException()
	{
	  super("数据更新异常");
	}
	public RecordUpdateException(String message) {
	    super(message);
	    this.message = message;
	}

	public RecordUpdateException(String message, Throwable cause) {
	  super(message, cause);
	}

	public RecordUpdateException(Throwable cause) {
	  super(cause);
	}

	public String getMessage() {
	   return StringUtils.isBlank(this.message) ? super.getMessage() : this.message;
	}	
}
