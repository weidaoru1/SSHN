package com.fus.cocoon.utils.object;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Clazz
{
  @SuppressWarnings("unchecked")
  private Class clazz;
  private String className;
  private String classSimpleName;
  private String lowerCasePrefixClassName;
  private String primaryKey;
  private List<Field> classFields = new ArrayList<Field>();
  public String getClassName() {
    return this.className;
  }
  public void setClassName(String className) {
    this.className = className;
  }
  public String getClassSimpleName() {
    return this.classSimpleName;
  }
  public void setClassSimpleName(String classSimpleName) {
    this.classSimpleName = classSimpleName;
  }
  public String getLowerCasePrefixClassName() {
    return this.lowerCasePrefixClassName;
  }
  public void setLowerCasePrefixClassName(String lowerCasePrefixClassName) {
    this.lowerCasePrefixClassName = lowerCasePrefixClassName;
  }
  public List<Field> getClassFields() {
    return this.classFields;
  }
  public void addClassField(Field field) {
    this.classFields.add(field);
  }
  @SuppressWarnings("unchecked")
public void setClazz(Class clazz) {
    this.clazz = clazz;
  }
  @SuppressWarnings("unchecked")
public Class getClazz() {
    return this.clazz;
  }
  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }
  public String getPrimaryKey() {
    return this.primaryKey;
  }
}