package com.fus.cocoon.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fus.cocoon.annotation.ModelAttr;
@MappedSuperclass
public abstract class Model {
   @Transient
   private boolean isSqlForCount;
   @Transient
   private String modelIdentity;
   @Transient
   private Map<String, Object> extendAttributesMap = new LinkedHashMap<String, Object>();

   @Transient
   private Map<String, String> sortFieldMap = new LinkedHashMap<String, String>();

   @Transient
   private Map<String, String> customerQueryConditionMap = new HashMap<String, String>();

   @Transient
   private List<String> groupBy = new ArrayList<String>();

   @Transient
   private String having;

   @Transient
   private boolean useDataAccessControl = true;

   @ModelAttr(value = "creator")
   private String creator;

   @ModelAttr(value = "createTime")
   private Date createTime;

   @ModelAttr(value = "updator")
   private String updator;

   @ModelAttr(value = "updateTime")
   private Date updateTime;

   @ModelAttr(value = "state")
   private String state;

   @Transient
   private Integer version;
public Model(){
	   
}
public Model(String modelIdentity){
   this.modelIdentity = modelIdentity;
}
public boolean isSqlForCount() {
	return isSqlForCount;
}

public void setSqlForCount(boolean isSqlForCount) {
	this.isSqlForCount = isSqlForCount;
}

public String getModelIdentity() {
	return modelIdentity;
}

public void setModelIdentity(String modelIdentity) {
	this.modelIdentity = modelIdentity;
}

public Map<String, Object> getExtendAttributesMap() {
	return extendAttributesMap;
}

public void setExtendAttributesMap(Map<String, Object> extendAttributesMap) {
	this.extendAttributesMap = extendAttributesMap;
}

public Map<String, String> getSortFieldMap() {
	return sortFieldMap;
}

public void setSortFieldMap(Map<String, String> sortFieldMap) {
	this.sortFieldMap = sortFieldMap;
}

public Map<String, String> getCustomerQueryConditionMap() {
	return customerQueryConditionMap;
}

public void setCustomerQueryConditionMap(
		Map<String, String> customerQueryConditionMap) {
	this.customerQueryConditionMap = customerQueryConditionMap;
}

public List<String> getGroupBy() {
	return groupBy;
}

public void setGroupBy(List<String> groupBy) {
	this.groupBy = groupBy;
}

public String getHaving() {
	return having;
}

public void setHaving(String having) {
	this.having = having;
}

public void setUseDataAccessControl(boolean useDataAccessControl) {
    this.useDataAccessControl = useDataAccessControl;
  }

  public boolean getUseDataAccessControl() {
    return this.useDataAccessControl;
  }
public String getCreator() {
	return creator;
}
public void setCreator(String creator) {
	this.creator = creator;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public String getUpdator() {
	return updator;
}
public void setUpdator(String updator) {
	this.updator = updator;
}
public Date getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public Integer getVersion() {
	return version;
}

public void setVersion(Integer version) {
	this.version = version;
}
}
