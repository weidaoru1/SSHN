package com.fus.cocoon.dao;

import org.hibernate.type.Type;

public class SqlParamsObject {
	private String sql;
	  private Object[] params;
	  private String[] extendAttrNames;
	  private Type[] extendAttrTypes;

	  public String getSql()
	  {
	    return this.sql;
	  }
	  public void setSql(String sql) {
	    this.sql = sql;
	  }
	  public Object[] getParams() {
	    return this.params;
	  }
	  public void setParams(Object[] params) {
	    this.params = params;
	  }
	  public void setExtendAttrNames(String[] extendAttrNames) {
	    this.extendAttrNames = extendAttrNames;
	  }
	  public String[] getExtendAttrNames() {
	    return this.extendAttrNames;
	  }
	  public void setExtendAttrTypes(Type[] extendAttrTypes) {
	    this.extendAttrTypes = extendAttrTypes;
	  }
	  public Type[] getExtendAttrTypes() {
	    return this.extendAttrTypes;
	  }
}
