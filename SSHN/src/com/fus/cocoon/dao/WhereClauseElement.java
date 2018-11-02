package com.fus.cocoon.dao;
final class WhereClauseElement
{
  private String clauseName;
  private String operator;
  private Object clauseValue;

  public WhereClauseElement()
  {
  }

  public WhereClauseElement(String clauseName, String operator, Object clauseValue)
  {
    this.clauseName = clauseName;
    this.operator = operator;
    this.clauseValue = clauseValue;
  }

  public String getClauseName() {
    return this.clauseName;
  }
  public void setClauseName(String clauseName) {
    this.clauseName = clauseName;
  }
  public String getOperator() {
    return this.operator;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public Object getClauseValue() {
    return this.clauseValue;
  }
  public void setClauseValue(Object clauseValue) {
    this.clauseValue = clauseValue;
  }
}